document.addEventListener("DOMContentLoaded", () => {
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