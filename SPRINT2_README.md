# FoodIngredientManagement 2차 스프린트: 원본 유지 + MariaDB UI 연결

## 기준 원본과 변경 원칙

- 기준 ZIP: **사용자가 업로드한 `FoodIngredientManagement-pjh-feat-ui.zip`**
- 폴더 골격, 패키지명, 기존 `FoodIngredientManagementApplication.java`, 매퍼, 팀 문서 유지
- `src/main/resources/static/css/common.css`의 **원본 바이트를 전혀 변경하지 않음**
- 팀원이 만든 `static/index.html` 대시보드 UI 유지 (개발용 화면 바로가기만 접기 형태로 추가)
- `sidebar.html`의 기존 상·하단 골격 유지. 폐기/재고/식재료/발주 링크만 실제 URL에 맞게 수정
- Controller는 `FoodController` (화면 이동), `FoodRestController` (AJAX/JDBC) 두 개만 사용
- **Service, Repository, Entity, DTO, Thymeleaf 변수/반복문 없음**
- Bootstrap 로컬 라이브러리는 추가되지만, CSS 커스텀 파일은 따로 만들지 않음
- JavaScript 파일명은 camelCase, 주석은 구현 이유와 향후 DB 계층 분리 지점을 설명

## 중요: 환경 설정과 DB 안전성

업로드한 원본 `pom.xml`의 `java.version`은 **17**이며 이 값을 그대로 유지했습니다.
업로드한 원본 `application.yaml`에 설정된 MariaDB(`localhost:3306/mydb`)를 사용합니다.

**기존 JPA `ddl-auto: create`는 재기동 시 DB 테이블에 영향을 줄 수 있어 `none`으로 변경했습니다.**
**테이블 자동 생성은 하지 않습니다.** `src/main/resources/db/sprint2_schema.sql`를 DB에서 직접 한 번 실행하세요.
SQL에는 `CREATE TABLE IF NOT EXISTS sprint2_...`와 `INSERT IGNORE` 샘플 데이터만 포함합니다.
팀의 기존 이름을 가진 테이블은 변경하지 않습니다.

MariaDB 명령행 예시:

```text
mariadb -u mydbid -p
USE mydb;
SOURCE C:/절대경로/FoodIngredientManagement/src/main/resources/db/sprint2_schema.sql;
SHOW TABLES LIKE 'sprint2_%';
```

- 로컬 사용자에게 `mydb`의 CREATE/INSERT/UPDATE/DELETE 권한이 있어야 합니다.
- `mydb` 데이터가 중요하다면 먼저 백업하세요.
- 예시 SQL은 테스트 데이터를 포함하므로, **팀의 공유 운영 DB가 아니라 개인 개발 DB에서만 실행**하세요.
- `spring.security.user`는 `spring-boot-starter-security`가 없는 원본 프로젝트에서는 로그인 기능을 만들지 않습니다.
- **현재 API는 로그인·권한 검사가 없어 외부에 배포하면 안 됩니다.** `sidebar.html`의 로그아웃 버튼은 UI 전용으로 비활성화되어 있습니다.

## 실행

IntelliJ에서 Maven Reload 후 `FoodIngredientManagementApplication` 실행.
윈도우 터미널 또는 Git Bash:

```bash
./mvnw spring-boot:run
```

Windows CMD에서는 `mvnw.cmd spring-boot:run` 실행.

- 메인: `http://localhost:8881/dashboard` (기존 `static/index.html` 대시보드)
- 직영점 발주 요청: `http://localhost:8881/store/orders/request`
- 직영점 발주 목록: `http://localhost:8881/store/orders/list`
- 본사 발주 목록: `http://localhost:8881/orders/list`
- 재고 목록: `http://localhost:8881/inventory/list`
- 식재료: `http://localhost:8881/ingredients/list`
- 본사 폐기: `http://localhost:8881/hq/disposal/list`
- 직영점 폐기: `http://localhost:8881/store/disposal/list`

## MariaDB 데이터 연동 테스트

1. `/store/orders/request`: 식재료·수량 선택 후 저장.
2. `/store/orders/list`: 같은 발주가 저장되고 `REQUESTED` 상태에서만 수정·삭제 가능한지 확인.
3. `/orders/list`: 저장된 발주 선택 후 승인/대기/거절. 거절/대기는 공통 사유 한 개 + 일부 발주 예외 사유 입력 가능.
4. `/store/orders/list`: 새로고침 후 변경 상태가 저장됐는지, 수정·삭제가 비활성화됐는지 확인.
5. `/hq/disposal/request` 또는 `/store/disposal/request`: 재고를 선택하여 폐기 등록.
6. `/inventory/list`: 재고가 폐기 수량만큼 감소했는지, 상세의 폐기 이력이 생겼는지 확인.
7. `/ingredients/request`: 신규 식재료 등록 → 목록에서 조회.

현재 본사/직영점은 화면 역할만 구분합니다. 실제 로그인과 매장별 접근 통제는 추후 구현해야 합니다.
식재료 사진/폐기 증빙사진의 실제 저장은 아직 없습니다. 파일 선택 UI만 유지합니다.
메인 대시보드 KPI와 차트는 팀원 원본 HTML의 시안 값이며, 새 DB와 연동된 통계는 아닙니다.

## 프런트/API 연결

- `api.js`: 공통 fetch(JSON)와 화면 표시 함수
- `ingredientDetailModal.js`: 기존 상세 HTML 조각을 fetch → API로 실제 데이터 조회 → Offcanvas 내용 채우기
- `orderRequest.js`: 발주 품목 선택·합계와 POST/PUT
- `orderReview.js`: 본사 목록·일괄 상태 변경 PATCH
- `storeOrder.js`: 직영점 목록·수정 및 삭제
- `pageDemo.js`: DB 목록 표시/검색과 식재료·폐기 등록 (이름은 기존 파일의 역할에서 이어지지만 더는 브라우저 메모리를 사용하지 않음)
- `layout.js`: 기존 AJAX 사이드바 로드

`GET /api/orders?scope=store|hq`, `GET /api/orders/{id}`, `POST /api/store/orders`, `PUT/DELETE /api/store/orders/{id}`, `PATCH /api/hq/orders/status`
`GET/POST /api/ingredients`, `GET /api/inventory`, `GET /api/inventory/{id}`, `GET/POST /api/disposals`, `GET /api/disposals/{id}`.

## 이후 Service/Repository 추가 시

현재 `FoodRestController.java`의 JdbcTemplate SQL 부분을 도메인별 `Service` → `Repository`로 옮깁니다.
**기존 프런트의 API URL/JSON 필드 구조는 유지**하면 화면을 다시 만들 필요가 없습니다.
이 프로젝트의 `sprint2_` 테이블을 실제 업무 스키마로 바꿀 때는 기존 팀의 DB 구조와 먼저 협의하세요.

## 검증 수준

- JavaScript 파일 전체에 `node --check` 실행: 통과
- HTML의 로컬 CSS/JS 경로 및 원본 CSS 동일성 확인: 통과
- 독립 Chromium 인페이지 테스트에서 본사 발주 테이블과 직영점 발주 계산 동작 확인
- **실제 MariaDB 접속, Spring Boot Maven 빌드/기동, 다중 사용자 동시성 검증은 이 환경에서 수행하지 못했습니다.**
- 로컬 Chromium의 HTTP 주소 접속은 차단되므로 localhost 실서버 테스트는 사용자 환경에서 해야 합니다.
