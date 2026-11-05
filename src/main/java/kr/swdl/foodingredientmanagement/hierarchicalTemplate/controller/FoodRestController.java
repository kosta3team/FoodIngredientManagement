package kr.swdl.foodingredientmanagement.hierarchicalTemplate.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 2차 스프린트 임시 JDBC API.
 * Service/Repository/Entity를 아직 만들지 않되 MariaDB의 실제 데이터로 화면을 시험한다.
 * 추후 Service/Repository를 구현하면 아래 SQL을 해당 계층으로 이동한다.
 * [주의] 로그인/권한 검사는 아직 없으므로 개발 환경에서만 사용해야 한다.
 */
@RestController
@RequestMapping("/api")
public class FoodRestController {

    private static final String STORE_NAME = "서울 한국대점";
    private final JdbcTemplate jdbc;
    private final TransactionTemplate transaction;

    public FoodRestController(JdbcTemplate jdbc, PlatformTransactionManager manager) {
        this.jdbc = jdbc;
        this.transaction = new TransactionTemplate(manager);
    }

    // DB 테이블과 화면 JSON의 필드명을 동일하게 쓰도록 조회한다.
    @GetMapping("/ingredients")
    public List<Map<String, Object>> ingredients() {
        return jdbc.queryForList("""
            SELECT code, name, category, unit, storage_type AS storageType,
                   unit_price AS price, shelf_life_days AS shelfLifeDays,
                   safety_days AS safetyDays
            FROM sprint2_ingredient ORDER BY name
            """);
    }

    @GetMapping("/ingredients/{code}")
    public Map<String, Object> ingredient(@PathVariable String code) {
        return one("""
            SELECT code, name, category, unit, storage_type AS storageType,
                   unit_price AS price, shelf_life_days AS shelfLifeDays,
                   safety_days AS safetyDays
            FROM sprint2_ingredient WHERE code = ?
            """, code);
    }

    // 이미지 파일 저장은 이번 스프린트 범위가 아니므로 일반 입력값만 DB에 저장한다.
    @PostMapping("/ingredients")
    public Map<String, String> addIngredient(@RequestBody Map<String, Object> input) {
        String name = required(input, "name");
        String category = required(input, "category");
        String unit = required(input, "unit");
        String storage = required(input, "storageType");
        int price = nonNegative(input, "price");
        int life = positive(input, "shelfLifeDays");
        int safety = nonNegative(input, "safetyDays");
        String code = "F" + UUID.randomUUID().toString().replace("-", "").substring(0, 9);
        jdbc.update("""
            INSERT INTO sprint2_ingredient
              (code, name, category, unit, storage_type, unit_price, shelf_life_days, safety_days)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """, code, name, category, unit, storage, price, life, safety);
        return Map.of("code", code);
    }

    @GetMapping("/inventory")
    public List<Map<String, Object>> inventory() {
        return jdbc.queryForList("""
            SELECT v.id, v.location, i.code, i.name, i.storage_type AS storageType,
                   i.unit, i.unit_price AS price, v.quantity,
                   DATE_FORMAT(v.received_at, '%Y-%m-%d') AS receivedDate,
                   DATE_FORMAT(v.expiry_at, '%Y-%m-%d') AS expiryDate,
                   v.manager_name AS manager
            FROM sprint2_inventory v JOIN sprint2_ingredient i ON v.ingredient_code = i.code
            ORDER BY v.id DESC
            """);
    }

    @GetMapping("/inventory/{id}")
    public Map<String, Object> inventoryDetail(@PathVariable long id) {
        Map<String, Object> data = one("""
            SELECT v.id, v.location, i.code, i.name, i.storage_type AS storageType,
                   i.unit, i.unit_price AS price, v.quantity,
                   DATE_FORMAT(v.received_at, '%Y-%m-%d') AS receivedDate,
                   DATE_FORMAT(v.expiry_at, '%Y-%m-%d') AS expiryDate,
                   v.manager_name AS manager
            FROM sprint2_inventory v JOIN sprint2_ingredient i ON v.ingredient_code = i.code
            WHERE v.id = ?
            """, id);
        data.put("history", jdbc.queryForList("""
            SELECT id, quantity, disposal_type AS disposalType,
                   DATE_FORMAT(disposed_at, '%Y-%m-%d %H:%i') AS datetime
            FROM sprint2_disposal WHERE inventory_id = ? ORDER BY id DESC
            """, id));
        return data;
    }

    // 발주 목록은 본사·직영점이 동일한 DB 테이블을 조회한다.
    @GetMapping("/orders")
    public List<Map<String, Object>> orders(@RequestParam(defaultValue = "hq") String scope) {
        String sql = """
            SELECT order_number, location, status, COALESCE(reason, '') AS reason,
                   DATE_FORMAT(created_at, '%Y-%m-%d %H:%i:%s') AS createdAt
            FROM sprint2_order
            """;
        List<Map<String, Object>> list;
        if ("store".equals(scope)) {
            list = jdbc.queryForList(sql + " WHERE location = ? ORDER BY created_at DESC", STORE_NAME);
        } else {
            list = jdbc.queryForList(sql + " ORDER BY created_at DESC");
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : list) result.add(toOrder(row));
        return result;
    }

    @GetMapping("/orders/{id}")
    public Map<String, Object> order(@PathVariable String id) {
        Map<String, Object> row = one("""
            SELECT order_number, location, status, COALESCE(reason, '') AS reason,
                   DATE_FORMAT(created_at, '%Y-%m-%d %H:%i:%s') AS createdAt
            FROM sprint2_order WHERE order_number = ?
            """, id);
        return toOrder(row);
    }

    // 서버 기준 단가를 사용해 주문 항목을 저장한다. 브라우저가 보낸 가격은 신뢰하지 않는다.
    @PostMapping("/store/orders")
    public Map<String, String> addOrder(@RequestBody Map<String, Object> input) {
        List<Map<String, Object>> items = checkItems(input);
        String number = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + UUID.randomUUID().toString().substring(0, 5);
        transaction.executeWithoutResult(tx -> {
            jdbc.update("INSERT INTO sprint2_order(order_number, location, status) VALUES (?, ?, 'REQUESTED')",
                    number, STORE_NAME);
            insertItems(number, items);
        });
        return Map.of("id", number);
    }

    // 수정 가능 상태(REQUESTED)인지 DB에서 다시 검사한다.
    @PutMapping("/store/orders/{id}")
    public Map<String, String> editOrder(@PathVariable String id, @RequestBody Map<String, Object> input) {
        List<Map<String, Object>> items = checkItems(input);
        transaction.executeWithoutResult(tx -> {
            int count = jdbc.update("""
                UPDATE sprint2_order SET modified_at = NOW()
                WHERE order_number = ? AND location = ? AND status = 'REQUESTED'
                """, id, STORE_NAME);
            if (count != 1) throw new IllegalStateException("수정할 수 없는 발주입니다.");
            jdbc.update("DELETE FROM sprint2_order_item WHERE order_number = ?", id);
            insertItems(id, items);
        });
        return Map.of("id", id);
    }

    @DeleteMapping("/store/orders/{id}")
    public Map<String, String> deleteOrder(@PathVariable String id) {
        int count = jdbc.update("""
            DELETE FROM sprint2_order
            WHERE order_number = ? AND location = ? AND status = 'REQUESTED'
            """, id, STORE_NAME);
        if (count != 1) throw new IllegalStateException("이미 처리된 발주는 삭제할 수 없습니다.");
        return Map.of("id", id);
    }

    // 100건을 한번에 처리하되 사유가 다른 소수 건만 reason을 다르게 전송한다.
    // 한 건이라도 상태 충돌이 있으면 전체 처리를 취소한다.
    @PatchMapping("/hq/orders/status")
    public Map<String, Integer> changeOrderStatus(@RequestBody Map<String, Object> input) {
        List<Map<String, Object>> items = checkItems(input, false);
        transaction.executeWithoutResult(tx -> {
            for (Map<String, Object> item : items) {
                String id = required(item, "orderId");
                String target = required(item, "status");
                String reason = String.valueOf(item.getOrDefault("reason", "")).trim();
                if (!List.of("APPROVED", "PENDING", "REJECTED").contains(target)) {
                    throw new IllegalArgumentException("올바르지 않은 상태입니다.");
                }
                if (!"APPROVED".equals(target) && (reason.isBlank() || "null".equals(reason))) {
                    throw new IllegalArgumentException("대기/거절 사유를 입력해야 합니다.");
                }
                int count = jdbc.update("""
                    UPDATE sprint2_order SET status = ?, reason = ?
                    WHERE order_number = ? AND
                          (status = 'REQUESTED' OR (status = 'PENDING' AND ? IN ('APPROVED', 'REJECTED')))
                    """, target, "APPROVED".equals(target) ? null : reason, id, target);
                if (count != 1) throw new IllegalStateException(id + " 상태 충돌: 목록을 새로고침해 주세요.");
            }
        });
        return Map.of("updated", items.size());
    }

    // 본사에서는 모든 지점, 직영점에서는 해당 지점의 폐기 기록을 조회한다.
    @GetMapping("/disposals")
    public List<Map<String, Object>> disposals(@RequestParam(defaultValue = "hq") String scope) {
        String sql = """
            SELECT d.id, d.inventory_id AS inventoryId, d.location, i.name,
                   d.quantity, i.unit, d.disposal_type AS type, d.disposal_method AS method,
                   d.reason, d.manager_name AS manager,
                   DATE_FORMAT(d.disposed_at, '%Y-%m-%d %H:%i:%s') AS datetime
            FROM sprint2_disposal d
            JOIN sprint2_inventory v ON d.inventory_id = v.id
            JOIN sprint2_ingredient i ON v.ingredient_code = i.code
            """;
        if ("store".equals(scope)) {
            return jdbc.queryForList(sql + " WHERE d.location = ? ORDER BY d.id DESC", STORE_NAME);
        }
        return jdbc.queryForList(sql + " ORDER BY d.id DESC");
    }

    @GetMapping("/disposals/{id}")
    public Map<String, Object> disposal(@PathVariable long id) {
        return one("""
            SELECT d.id, d.inventory_id AS inventoryId, d.location, i.name,
                   d.quantity, i.unit, d.disposal_type AS type, d.disposal_method AS method,
                   d.reason, d.manager_name AS manager,
                   DATE_FORMAT(d.disposed_at, '%Y-%m-%d %H:%i:%s') AS datetime
            FROM sprint2_disposal d
            JOIN sprint2_inventory v ON d.inventory_id = v.id
            JOIN sprint2_ingredient i ON v.ingredient_code = i.code
            WHERE d.id = ?
            """, id);
    }

    // 폐기는 재고 차감과 기록 생성이 함께 성공해야 한다(트랜잭션).
    @PostMapping("/disposals")
    public Map<String, String> addDisposal(@RequestBody Map<String, Object> input) {
        long inventoryId = positive(input, "inventoryId");
        BigDecimal quantity = new BigDecimal(required(input, "quantity"));
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("폐기수량은 0보다 커야 합니다.");
        }
        String location = required(input, "location");
        String type = required(input, "type");
        String method = required(input, "method");
        String reason = required(input, "reason");
        String manager = required(input, "manager");
        // 입력한 폐기일을 그대로 기록한다. 날짜 형식은 서버에서 검사한다.
        String disposedAt = required(input, "disposedAt");
        try { java.time.LocalDate.parse(disposedAt); }
        catch (java.time.format.DateTimeParseException error) {
            throw new IllegalArgumentException("올바른 폐기일자를 입력해 주세요.");
        }
        String scope = String.valueOf(input.getOrDefault("scope", "store"));
        if ("store".equals(scope) && !STORE_NAME.equals(location)) {
            throw new IllegalArgumentException("직영점은 자기 매장만 등록할 수 있습니다.");
        }
        transaction.executeWithoutResult(tx -> {
            int updated = jdbc.update("""
                UPDATE sprint2_inventory SET quantity = quantity - ?
                WHERE id = ? AND location = ? AND quantity >= ?
                """, quantity, inventoryId, location, quantity);
            if (updated != 1) throw new IllegalStateException("해당 재고가 없거나 폐기 수량이 재고보다 많습니다.");
            jdbc.update("""
                INSERT INTO sprint2_disposal
                (inventory_id, location, quantity, disposal_type, disposal_method, reason, manager_name, disposed_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, STR_TO_DATE(?, '%Y-%m-%d'))
                """, inventoryId, location, quantity, type, method, reason, manager, disposedAt);
        });
        return Map.of("result", "saved");
    }

    // ---- 아래 함수들은 현재 Controller 내부의 임시 DB 보조 함수다. ----

    private Map<String, Object> one(String sql, Object id) {
        List<Map<String, Object>> rows = jdbc.queryForList(sql, id);
        if (rows.isEmpty()) throw new EmptyResultDataAccessException(1);
        return new LinkedHashMap<>(rows.get(0));
    }

    private Map<String, Object> toOrder(Map<String, Object> row) {
        String id = String.valueOf(row.get("order_number"));
        Map<String, Object> order = new LinkedHashMap<>();
        order.put("id", id);
        order.put("date", row.get("createdAt"));
        order.put("location", row.get("location"));
        order.put("status", row.get("status"));
        order.put("reason", row.get("reason"));
        order.put("items", jdbc.queryForList("""
            SELECT oi.ingredient_code AS code, i.name, oi.quantity, oi.unit_price AS price
            FROM sprint2_order_item oi
            JOIN sprint2_ingredient i ON oi.ingredient_code = i.code
            WHERE oi.order_number = ? ORDER BY oi.id
            """, id));
        return order;
    }

    private void insertItems(String number, List<Map<String, Object>> items) {
        for (Map<String, Object> item : items) {
            String code = required(item, "code");
            int quantity = positive(item, "quantity");
            Integer price = jdbc.queryForObject(
                "SELECT unit_price FROM sprint2_ingredient WHERE code = ?", Integer.class, code);
            if (price == null) throw new IllegalArgumentException("존재하지 않는 식재료입니다.");
            jdbc.update("""
                INSERT INTO sprint2_order_item(order_number, ingredient_code, quantity, unit_price)
                VALUES (?, ?, ?, ?)
                """, number, code, quantity, price);
        }
    }

    private List<Map<String, Object>> checkItems(Map<String, Object> input) {
        return checkItems(input, true);
    }

    private List<Map<String, Object>> checkItems(Map<String, Object> input, boolean uniqueCodes) {
        Object raw = input.get("items");
        if (!(raw instanceof List<?> list) || list.isEmpty() || list.size() > 100) {
            throw new IllegalArgumentException("1~100개의 품목/발주를 입력해 주세요.");
        }
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> codes = new ArrayList<>();
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> map)) throw new IllegalArgumentException("잘못된 항목입니다.");
            Map<String, Object> copy = new LinkedHashMap<>();
            map.forEach((key, value) -> copy.put(String.valueOf(key), value));
            if (uniqueCodes) {
                String code = required(copy, "code");
                positive(copy, "quantity");
                if (codes.contains(code)) throw new IllegalArgumentException("중복 식재료는 입력할 수 없습니다.");
                codes.add(code);
            }
            result.add(copy);
        }
        return result;
    }

    private String required(Map<String, Object> input, String key) {
        Object value = input.get(key);
        if (value == null || String.valueOf(value).trim().isEmpty()) {
            throw new IllegalArgumentException(key + " 값이 없습니다.");
        }
        return String.valueOf(value).trim();
    }

    private int positive(Map<String, Object> input, String key) {
        int value = nonNegative(input, key);
        if (value == 0) throw new IllegalArgumentException(key + " 값은 1 이상이어야 합니다.");
        return value;
    }

    private int nonNegative(Map<String, Object> input, String key) {
        int value;
        try {
            value = Integer.parseInt(required(input, key));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException(key + " 값은 정수여야 합니다.");
        }
        if (value < 0) throw new IllegalArgumentException(key + " 값은 음수일 수 없습니다.");
        return value;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException error) {
        return ResponseEntity.badRequest().body(Map.of("message", error.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflict(IllegalStateException error) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", error.getMessage()));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Map<String, String>> notFound(EmptyResultDataAccessException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "데이터가 없습니다."));
    }
}
