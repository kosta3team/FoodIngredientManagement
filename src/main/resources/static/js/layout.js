document.addEventListener("DOMContentLoaded", () => {

    // 1. 부트스트랩 JS를 동적으로 생성해서 body에 추가
    const bootstrapScript = document.createElement('script');
    bootstrapScript.src = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js";
    document.body.appendChild(bootstrapScript);

    const loadFragment = async (url, containerId) => {
        try {
            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${url}`);
            }

            const html = await response.text();

            document.getElementById(containerId).outerHTML = html;

        } catch (error) {
            console.error("프래그먼트 로드 실패:", error);
        }
    };

    loadFragment('/fragments/sidebar.html', 'sidebar-container');
});