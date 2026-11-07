# UI 실행 안내 — templates 단일 소스

업무 화면 HTML 18개는 `src/main/resources/templates/` **한 곳**에서만 관리합니다.
`src/main/resources/static/pages/`와 예전 `FoodIngredientManagement/static/` 복제본은 제거했습니다.
원래의 JavaScript, 공통 CSS, 사이드바 내용은 변경하지 않았습니다.

## Windows / macOS IntelliJ 직접 미리보기

- 프로젝트 루트(`pom.xml`이 있는 `FoodIngredientManagement`)를 IntelliJ로 엽니다.
- `src/main/resources/static/index.html`을 우클릭 → **Open In → Browser**로 열어 메뉴를 사용합니다.
- 개별 화면은 `src/main/resources/templates/orders/hqOrderList.html`처럼 해당 템플릿 파일을 우클릭 → **Open In → Browser**로 열 수 있습니다.
- 파일 탐색기에서 위 HTML을 직접 더블클릭(`file://`)해도 CSS와 iframe 사이드바의 파일 상대경로가 유지됩니다.
- 브라우저에서 바로 열 때 HTML의 일반 `href`, `src`를 사용합니다. Thymeleaf `th:*` 속성은 이때 처리되지 않습니다.
- IntelliJ 자체가 잘못된 63342 URL(중복 모듈명)을 생성하는 환경은 설정/파일 선택 문제이므로 코드만으로 100% 해결할 수 없습니다. `src/main/resources/templates/...`의 실제 파일을 직접 열거나 파일 탐색기에서 더블클릭하세요.

## Spring Boot Run (Java 17 권장)

- `FoodIngredientManagementApplication` 실행 또는 Windows: `mvnw.cmd spring-boot:run`.
- 메인: http://localhost:8881/index.html
- 발주 목록: http://localhost:8881/hq/orders/list
- 발주 등록: http://localhost:8881/store/orders/request
- 템플릿 직접 URL: http://localhost:8881/templates/orders/hqOrderList.html
- 이전 `/pages/<분류>/<파일>.html` 주소는 호환 리다이렉트를 통해 위 템플릿 경로로 이동합니다.

Spring Boot에서는 Controller가 업무 주소를 `/templates/...`로 리다이렉트하며, `templatePage`가 Thymeleaf 뷰 이름을 반환합니다.
서버에서는 Thymeleaf `th:href`/`th:src`가 `/css`, `/js`, `/fragments` 정적 리소스로 매핑됩니다.
파일 미리보기에서는 같은 속성의 일반 `href`/`src`가 `../../static/...`를 가리킵니다.
사이드바는 `src/main/resources/static/fragments/sidebar.html` **한 파일**만 iframe으로 사용합니다.

DB 연결 및 발주 저장은 UI 스켈레톤의 범위 밖입니다. 기존 버튼의 비활성화 상태를 유지합니다.
CDN Bootstrap 스타일은 인터넷 연결이 필요합니다.
