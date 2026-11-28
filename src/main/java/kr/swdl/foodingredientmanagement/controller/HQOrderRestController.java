package kr.swdl.foodingredientmanagement.controller;

import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.controller.request.OrderDetailDateUpdateDTO;
import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import kr.swdl.foodingredientmanagement.security.AppUserDetails;
import kr.swdl.foodingredientmanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HQOrderRestController {
    private final OrderService orderService;

    @GetMapping("/api/v1/hq/orders/status")
    public ResponseEntity<?> getOrderStatusCount() {
        return ResponseEntity.ok(orderService.countOrderStatus());
    }

    @GetMapping("/api/v1/hq/orders/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderDetail(@PathVariable("orderId") Long orderId){
        log.info("orderId : {}",orderId);
        OrderInfoVO orderInfo = orderService.getOrderInfoByOrderId(orderId);
        log.info("orderInfo: {}",orderInfo);
        List<OrderDetailListVO> orderDetails = orderService.getOrderDetail(orderId);
        log.info("orderDetail : {}",orderDetails);
        Map<String, Object> result = new HashMap<>();
        result.put("info",orderInfo);
        result.put("items",orderDetails);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/v1/hq/orders")
    public ResponseEntity<PageInfo<OrderRequestDTO>> getOrderList(
            @ModelAttribute OrderSearchDTO search,
            @AuthenticationPrincipal AppUserDetails userDetails) {

        // 로그인한 본사 직원 ID
        Long userId = userDetails.getUserId();

        String storeName = null;
        Long orderId = null;

        String searchType = search.getSearchType();
        String searchKeyword = search.getSearchKeyword();

        // 검색어가 입력된 경우
        if (searchKeyword != null && !searchKeyword.isBlank()) {

            searchKeyword = searchKeyword.trim();

            // 발주번호 검색
            if ("orderId".equals(searchType)) {

                try {
                    orderId = Long.valueOf(searchKeyword);

                } catch (NumberFormatException e) {
                    log.warn("잘못된 발주번호 검색: {}", searchKeyword);
                    return ResponseEntity.badRequest().build();
                }

                // 지점명 검색
            } else if ("storeName".equals(searchType)) {

                storeName = searchKeyword;

                // 전체 검색
            } else if ("all".equals(searchType)) {

                storeName = searchKeyword;

                // 숫자로만 구성된 경우 발주번호 검색도 함께 수행
                if (searchKeyword.matches("\\d+")) {
                    try {
                        orderId = Long.valueOf(searchKeyword);

                    } catch (NumberFormatException e) {
                        log.warn("발주번호 범위 초과: {}", searchKeyword);
                    }
                }
            }
        }

        // 검색 조건 로그
        log.info("===== 본사 발주 목록 조회 =====");
        log.info("로그인 본사 직원 ID : {}", userId);
        log.info("searchType : {}", searchType);
        log.info("searchKeyword : {}", searchKeyword);
        log.info("storeName : {}", storeName);
        log.info("orderId : {}", orderId);
        log.info("page : {}", search.getPage());
        log.info("size : {}", search.getSize());
        log.info("startDate : {}", search.getStartDate());
        log.info("endDate : {}", search.getEndDate());
        log.info("orderStatus : {}", search.getOrderStatus());

        // 로그인한 본사 직원 기준 발주 목록 조회
        PageInfo<OrderRequestDTO> result = orderService.getOrderList(
                userId,
                search.getPage(),
                search.getSize(),
                search.getStartDate(),
                search.getEndDate(),
                search.getOrderStatus(),
                storeName,
                orderId,
                search.getSearchType()
        );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/v1/store/orders")
    public ResponseEntity<PageInfo<OrderRequestDTO>> getStoreOrderList(
            @ModelAttribute OrderStoreSearchDTO search,
            @AuthenticationPrincipal AppUserDetails userDetails) {

        // 로그인한 사용자의 매장 ID 적용
        search.setStoreId(userDetails.getStoreId());

        PageInfo<OrderRequestDTO> result =
                orderService.getStoreOrderList(search);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/v1/store/orders/progress")
    public ResponseEntity<List<OrderProgressVO>> getOrderProgress(
            @AuthenticationPrincipal AppUserDetails userDetails) {

        Long storeId = userDetails.getStoreId();

        log.info("===== 로그인 매장 발주 진행 현황 조회 =====");
        log.info("회원 ID: {}", userDetails.getUserId());
        log.info("매장 ID: {}", storeId);

        List<OrderProgressVO> result =
                orderService.getOrderProgress(storeId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/v1/store/orders/{orderId}/items")
    public ResponseEntity<List<Map<String, Object>>> getStoreOrderDetailItems(
            @PathVariable Long orderId,
            @AuthenticationPrincipal AppUserDetails userDetails) {

        Long storeId = userDetails.getStoreId();

        log.info("===== 로그인 매장 발주 품목 상세 조회 =====");
        log.info("발주 번호: {}", orderId);
        log.info("로그인 매장 ID: {}", storeId);

        List<Map<String, Object>> result =
                orderService.getOrderDetailItems(orderId, storeId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/v1/store/ingredients")
    public ResponseEntity<List<OrderIngredientVO>> getOrderIngredientList() {

        log.info("===== 발주 요청용 식재료 목록 조회 =====");

        List<OrderIngredientVO> result =
                orderService.getOrderIngredientList();

        log.info("조회된 식재료 개수: {}", result.size());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/v1/store/orders")
    public ResponseEntity<Map<String, Object>> registerStoreOrder(
            @RequestBody OrderRegisterRequestDTO request,
            @AuthenticationPrincipal AppUserDetails userDetails) {

        log.info("===== 로그인 매장 발주 등록 API =====");
        log.info("로그인 회원 ID: {}", userDetails.getUserId());
        log.info("로그인 매장 ID: {}", userDetails.getStoreId());

        List<OrderDetailRequestDTO> items = request.getItems();

        if (items == null || items.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "발주 품목은 필수입니다.")
            );
        }

        // 로그인 사용자 기준 발주 정보 생성
        OrderRequestDTO order = OrderRequestDTO.builder()
                .orderAt(Timestamp.valueOf(LocalDateTime.now()))
                .appAt(null)
                .totalPrice(0)
                .userId(userDetails.getUserId())
                .storeId(userDetails.getStoreId())
                .build();

        Long orderId = orderService.registerOrder(order, items);

        log.info("발주 등록 완료");
        log.info("생성된 발주 번호: {}", orderId);

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("message", "발주 등록 완료");

        return ResponseEntity.ok(result);
    }

    @PutMapping("/api/v1/hq/orders/{orderId}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody List<OrderStatusDTO> items
    ) {
        String orderStatus =
                orderService.updateOrderDetailStatus(orderId, items);

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("orderStatus", orderStatus);
        result.put("message", "발주 처리 결과가 저장되었습니다.");

        return ResponseEntity.ok(result);
    }


    @PutMapping("/api/v1/store/orders/dates")
    public ResponseEntity<Map<String, Object>> updateOrderDate(
            @RequestBody OrderDetailDateUpdateDTO dto) {

        log.info("===== 직영점 발주 품목 날짜 수정 API =====");
        log.info("발주 상세 번호: {}", dto.getOrderDetailId());

        int result = orderService.updateOrderDate(dto);

        Map<String, Object> response = new HashMap<>();

        if (result == 1) {
            response.put("success", true);
            response.put("message", "제조일자 및 입고일자가 수정되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "날짜 수정에 실패했습니다.");
        }

        return ResponseEntity.ok(response);
    }
}

