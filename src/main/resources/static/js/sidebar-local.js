// IntelliJ 브라우저 미리보기에서 공통 사이드바 불러오기
// script 파일 위치를 기준으로 sidebar.html의 주소를 찾는다.
const sidebarUrl = new URL("../fragments/sidebar.html", document.currentScript.src);

document.addEventListener("DOMContentLoaded", async function () {
    const container = document.getElementById("sidebar-container");
    if (!container) return;

    try {
        const response = await fetch(sidebarUrl);
        if (!response.ok) throw new Error("HTTP " + response.status);

        // 원본 sidebar.html을 현재 페이지에 삽입한다.
        container.outerHTML = await response.text();
    } catch (error) {
        console.error("사이드바 불러오기 실패:", error);
    }
});
