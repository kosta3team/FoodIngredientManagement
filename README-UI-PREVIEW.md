# 팀 공용 UI 미리보기 (단일 HTML 소스)

이 프로젝트의 **화면 HTML은 `src/main/resources/static/` 아래에 한 벌만 존재**합니다.
백엔드 서버와 브라우저에서 같은 파일을 보여 줍니다. Windows/macOS/Linux 공용입니다.

## A. 브라우저로 바로 보기 (Java, Maven, DB, 별도 서버 불필요)

파일 탐색기에서 `src/main/resources/static/index.html`을 더블클릭하여 Chrome/Edge/Safari로 엽니다.
하위 메뉴 및 다른 페이지도 사이드바에서 연결됩니다. 파일 URL(`file://`)에서 작동합니다.

또는 IntelliJ에서 동일 파일을 `Open in Browser`로 열 수 있습니다. IntelliJ 빌트인
웹 서버의 63342 URL이 잘못 생성되는 일부 IDE 설정에서는 해당 자동 URL이
404가 될 수 있습니다. 이것은 Java/CSS 코드로 수정할 수 있는 동작이 아니며,
**그 경우 파일 더블클릭 방식을 사용하세요.**

## B. Spring Boot로 보기 (Java 17, Maven 필요)

`FoodIngredientManagementApplication`을 실행합니다. 또는 터미널에서 `./mvnw spring-boot:run`
(macOS/Linux에서 실행 권한이 없다면 `sh mvnw spring-boot:run`; Windows에서는 `mvnw.cmd spring-boot:run`).

- 메인: http://localhost:8881/index.html
- 본사 폐기 목록: http://localhost:8881/hq/disposal/list
- 실제 정적 페이지: http://localhost:8881/pages/disposal/hqDisposalList.html

정적 HTML 한 벌을 제공하는 동안 컨트롤러는 `redirect:/pages/...`를 사용합니다.
이 단계에서는 DB/JPA 자동 연결을 제외합니다(application.yaml). DB 연동 시 설정을 복원하고
Controller/Thymeleaf를 점진적으로 구현해야 합니다.

## 저장소 및 규칙

- `src/main/resources/static/index.html`: 모든 모드 공통 진입점
- `src/main/resources/static/pages/**`: 18개 업무 화면 (DB 전 예시 데이터)
- `src/main/resources/static/fragments/sidebar.html`: 하나의 공통 사이드바, iframe 안에서 표시
- `src/main/resources/static/css/common.css`: 공통 스타일 및 iframe 폭
- `src/main/resources/static/js/layout.js`: 기존 Bootstrap 및 발주 전체선택 보조 JS

페이지에서 `src="../../fragments/sidebar.html"`, `href="../../css/common.css"` 같은
**상대 경로**만 사용합니다. 사이드바 메뉴는 `target="_top"`을 사용해 프레임 밖에서 페이지를 이동합니다.

**주의:** 이 방식은 파일 직접 보기와 Spring Boot를 지원하기 위한 UI 스켈레톤 구조입니다.
Thymeleaf 변수·인증·DB CRUD는 `file://` 미리보기에서 실행되지 않습니다.
CDN Bootstrap을 사용하므로 스타일을 완전히 보려면 인터넷 연결이 필요합니다.
