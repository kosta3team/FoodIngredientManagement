// 공통 사이드바는 기존 static/fragments/sidebar.html을 재사용한다.
// Bootstrap 스크립트는 HTML에서 먼저 연결하여 실행 순서를 보장한다.
document.addEventListener("DOMContentLoaded", function () {
    const container = document.getElementById("sidebar-container");
    if (!container) return;

    fetch("/fragments/sidebar.html")
        .then(function (response) {
            if (!response.ok) throw new Error("사이드바 HTTP " + response.status);
            return response.text();
        })
        .then(function (html) {
            container.outerHTML = html;
            // 현재 경로 메뉴만 강조한다.
            document.querySelectorAll(".hq-sidebar a[href]").forEach(function (link) {
                if (link.getAttribute("href") === window.location.pathname) {
                    link.classList.add("active");
                }
            });
        })
        .catch(function (error) { console.error(error); });
});
