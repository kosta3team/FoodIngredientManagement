-- ========================================================================
-- 2차 스프린트 단일 MariaDB 초기화 SQL
-- Spring Boot 시작 -> application.yaml -> spring.sql.init -> 이 파일 자동 실행
-- 수동으로 SQL 콘솔에서 실행해도 "No database selected"가 나지 않도록 DB 선택.
-- ========================================================================

-- 연결 대상 DB를 명시적으로 선택한다.
-- 데이터베이스 mydb 자체가 없으면 application.yaml의
-- createDatabaseIfNotExist=true 옵션으로 생성 시도한다 (권한 필요).
USE mydb;

-- 테이블은 존재하지 않을 때만 만들며, 기존 테이블을 삭제하지 않는다.
-- 예시 데이터는 INSERT IGNORE로 중복된 키를 건너뛴다.
-- 주의: 재시작 시 삭제했던 예시 데이터가 다시 삽입될 수 있다.

-- 1. 식재료 마스터: 발주와 재고가 공통으로 참조하는 식재료 목록
CREATE TABLE IF NOT EXISTS sprint2_ingredient (
                                                  code VARCHAR(12) PRIMARY KEY,             -- 식재료 코드
                                                  name VARCHAR(100) NOT NULL,               -- 식재료명
                                                  category VARCHAR(60) NOT NULL,            -- 품목 분류
                                                  unit VARCHAR(40) NOT NULL,                -- 입고/발주 단위
                                                  storage_type VARCHAR(20) NOT NULL,        -- 냉장 / 냉동 / 실온
                                                  unit_price INT NOT NULL DEFAULT 0,        -- 예시 단가
                                                  shelf_life_days INT NOT NULL DEFAULT 7,   -- 보관 기간(일)
                                                  safety_days INT NOT NULL DEFAULT 3        -- 사용기한 사전 경고 일수
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 사업장별 재고: 품목 코드로 식재료 마스터와 연결
CREATE TABLE IF NOT EXISTS sprint2_inventory (
                                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,              -- 재고 ID
                                                 location VARCHAR(80) NOT NULL,                    -- 본사 창고 또는 직영점명
                                                 ingredient_code VARCHAR(12) NOT NULL,             -- 식재료 코드
                                                 quantity DECIMAL(12,2) NOT NULL DEFAULT 0,        -- 현재 재고 수량
                                                 received_at DATE NOT NULL,                       -- 입고일
                                                 expiry_at DATE NOT NULL,                         -- 사용기한
                                                 manager_name VARCHAR(50) NOT NULL,                -- 재고 담당자
                                                 INDEX idx_sprint2_inventory_location (location),
                                                 CONSTRAINT fk_sprint2_inventory_ingredient
                                                     FOREIGN KEY (ingredient_code) REFERENCES sprint2_ingredient(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 발주 헤더: 본사 심사 상태와 처리 사유를 관리
-- 상태: REQUESTED(요청접수), PENDING(대기), APPROVED(승인), REJECTED(거절)
CREATE TABLE IF NOT EXISTS sprint2_order (
                                             order_number VARCHAR(24) PRIMARY KEY,              -- 발주번호
                                             location VARCHAR(80) NOT NULL,                     -- 요청 직영점
                                             status VARCHAR(20) NOT NULL DEFAULT 'REQUESTED',   -- 상태값
                                             reason VARCHAR(500),                               -- 본사 처리 사유
                                             created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                             INDEX idx_sprint2_order_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 발주 품목: 발주 한 건에 여러 식재료가 들어갈 수 있도록 별도 분리
CREATE TABLE IF NOT EXISTS sprint2_order_item (
                                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,   -- 발주 품목 ID
                                                  order_number VARCHAR(24) NOT NULL,     -- 소속 발주번호
                                                  ingredient_code VARCHAR(12) NOT NULL,  -- 식재료 코드
                                                  quantity INT NOT NULL,                 -- 요청 수량
                                                  unit_price INT NOT NULL,               -- 요청 시점 단가
                                                  UNIQUE KEY uq_sprint2_item (order_number, ingredient_code),
                                                  CONSTRAINT fk_sprint2_item_order
                                                      FOREIGN KEY (order_number) REFERENCES sprint2_order(order_number)
                                                          ON DELETE CASCADE,
                                                  CONSTRAINT fk_sprint2_item_ingredient
                                                      FOREIGN KEY (ingredient_code) REFERENCES sprint2_ingredient(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 폐기 이력: 폐기 등록이 어느 재고에 적용되었는지 기록
-- 실제 수량 차감은 기존 FoodRestController의 JDBC 트랜잭션에서 수행
CREATE TABLE IF NOT EXISTS sprint2_disposal (
                                                id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- 폐기 ID
                                                inventory_id BIGINT NOT NULL,          -- 대상 재고 ID
                                                location VARCHAR(80) NOT NULL,        -- 폐기 발생 사업장
                                                quantity DECIMAL(12,2) NOT NULL,       -- 폐기 수량
                                                disposal_type VARCHAR(60) NOT NULL,    -- 폐기 유형
                                                disposal_method VARCHAR(60) NOT NULL,  -- 처리 방법
                                                reason VARCHAR(1000) NOT NULL,         -- 상세 사유
                                                manager_name VARCHAR(50) NOT NULL,     -- 등록자
                                                disposed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                CONSTRAINT fk_sprint2_disposal_inventory
                                                    FOREIGN KEY (inventory_id) REFERENCES sprint2_inventory(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================================================
-- 초기 테스트 데이터: INSERT IGNORE로 이미 있는 예제 행을 중복 생성하지 않음
-- 실제 등록 데이터에 영향을 주지 않도록 기존 키는 수정하지 않음
-- ========================================================================

-- 6. 샌드위치 프랜차이즈의 기본 식재료
INSERT IGNORE INTO sprint2_ingredient
(code, name, category, unit, storage_type, unit_price, shelf_life_days, safety_days)
VALUES
    ('0001','식빵','빵류','2개/팩','실온',2000,4,1),
    ('0006','양배추','채소류','1개/통','냉장',5000,10,3),
    ('0011','슬라이스치즈','유제품','10장/팩','냉장',2500,14,4),
    ('0021','소금','조미료','1kg/봉','실온',13000,180,14),
    ('0028','설탕','조미료','1kg/봉','실온',14000,180,14),
    ('0030','양상추','채소류','1개/통','냉장',3000,7,2);

-- 7. 본사 창고/직영점의 샘플 재고
INSERT IGNORE INTO sprint2_inventory
(id, location, ingredient_code, quantity, received_at, expiry_at, manager_name)
VALUES
    (1,'서울 한국대점','0001',20,'2026-11-01','2026-11-09','이수민'),
    (2,'본사 물류창고','0001',60,'2026-11-01','2026-11-10','김지훈'),
    (3,'강남점','0006',28,'2026-11-01','2026-11-12','박재현'),
    (4,'서울 한국대점','0030',15,'2026-11-01','2026-11-08','이수민'),
    (5,'본사 물류창고','0028',100,'2026-11-01','2027-04-01','김지훈');

-- 8. 본사 승인·대기·거절 기능을 시험할 발주 2건
INSERT IGNORE INTO sprint2_order
(order_number, location, status, reason, created_at)
VALUES
    ('202610310001','서울 한국대점','REQUESTED',NULL,'2026-10-31 12:30:00'),
    ('202611010001','강남점','REQUESTED',NULL,'2026-11-01 09:05:00');

-- 9. 위 발주의 품목 상세 데이터
INSERT IGNORE INTO sprint2_order_item
(id, order_number, ingredient_code, quantity, unit_price)
VALUES
    (1,'202610310001','0001',10,2000),
    (2,'202610310001','0028',2,14000),
    (3,'202611010001','0006',3,5000);
