# 팀 공용 브라우저 미리보기 / Spring Boot 실행

이 프로젝트는 하나의 UI 소스(`src/main/resources/static`)를 사용합니다.

## 팀원이 사용하는 방법

- IntelliJ에서 `pom.xml`이 있는 폴더를 열어 Maven 프로젝트로 인식시킵니다.
- `src/main/resources/static/pages/.../*.html`을 우클릭 → Open in Browser로 열 수 있습니다.
- Spring Boot는 `FoodIngredientManagementApplication`을 실행하고 http://localhost:8881/index.html 로 접속합니다.
- 로컬 파일 직접 열기(file://)도 `src/main/resources/static/index.html`을 통해 확인할 수 있습니다.

## 자동 URL에 모듈명이 추가되는 경우

일부 환경은 `http://localhost:63342/<프로젝트이름>/FoodIngredientManagement/static/...`으로 URL을 생성합니다.
이 URL을 받기 위해 Git에 포함된 `FoodIngredientManagement/static/**/*.html`은 본문이 아닌 짧은 호환 이동 파일입니다.
원본 UI의 복사본이 아니라 원본 HTML로만 이동시키며, 기존 IntelliJ `_ijt` 접근 토큰도 유지합니다.
기존 원본은 `src/main/resources/static`의 HTML만 편집하세요.

- IntelliJ가 모듈명을 중복하지 않고 `<프로젝트이름>/static/...`을 생성하는 경우에는 원본 HTML이 그대로 열립니다.
- IntelliJ의 URL 생성 로직 자체를 변경하는 것은 아닙니다. 일부 버전이나 특이한 모듈 구성에서는 추가 확인이 필요할 수 있습니다.
- 이 호환 파일은 일반 텍스트이므로 Windows·macOS에서 모두 Git으로 배포됩니다. symlink, 별도 설치, 사용자별 절대 경로가 필요 없습니다.
- Spring Boot 서버는 호환 파일을 사용하지 않습니다. Maven Java 17/애플리케이션 설정은 기존과 같습니다.
- IntelliJ의 `.idea`는 Git 공유 대상에서 제외됩니다.

## 검증 범위

- 실제 자동 생성 사례의 URL 구조를 로컬 HTTP 서버로 모사하여 HTTP 200 응답 확인
- 프로젝트 폴더명이 `FoodIngredientManagement 4` 또는 `FoodIngredientManagement`인 경우 모두 확인
- 업무 화면, 스타일시트, 사이드바 iframe 파일의 HTTP 200 응답과 상대경로 확인
- Node.js로 20개 호환 이동 파일의 URL 변환 및 `_ijt` 파라미터 보존 확인
- Windows 및 macOS의 IntelliJ와 Chrome을 직접 실행한 종단간 테스트는 할 수 없었습니다.
- 브라우저 접근은 테스트 환경 정책상 차단되어 실제 화면 렌더링·클릭 검증은 수행하지 못했습니다.
- IntelliJ의 내장 서버는 각 개발자의 IDE 설정 및 프로젝트 인식에 종속되므로, 모든 버전에서 자동 URL을 보장한다고 주장하지 않습니다.
