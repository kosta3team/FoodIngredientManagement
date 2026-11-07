
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
    if (document.getElementById('sidebar-container')) {
        loadFragment('/fragments/sidebar.html', 'sidebar-container');
    }


    // ==================================================
    // 2. 본사 발주 목록 전체선택 / 전체해제 기능 추가
    // ==================================================

    // 발주 목록의 전체선택 체크박스
    const selectAllOrders = document.getElementById("selectAllOrders");

    // 현재 페이지에 전체선택 체크박스가 있는 경우에만 실행
    if (selectAllOrders) {
        // 발주 목록에 있는 개별 체크박스
        const orderCheckboxes = document.querySelectorAll(
            "#hqOrderTable tbody .order-checkbox"
        );
        // --------------------------------------------------
        // 2-1. 전체선택 체크박스를 클릭한 경우
        // --------------------------------------------------
        selectAllOrders.addEventListener("change", () => {
            // 전체선택 상태를 모든 개별 체크박스에 적용
            orderCheckboxes.forEach(checkbox => {
                checkbox.checked = selectAllOrders.checked;
            });
            // 중간 선택 상태 초기화
            selectAllOrders.indeterminate = false;
        });


        // --------------------------------------------------
        // 2-2. 개별 체크박스를 클릭한 경우
        // --------------------------------------------------
        orderCheckboxes.forEach(checkbox => {
            checkbox.addEventListener("change", () => {
                // 발주 목록 전체 개수
                const totalCount = orderCheckboxes.length;
                // 선택된 발주 개수
                const checkedCount = document.querySelectorAll(
                    "#hqOrderTable tbody .order-checkbox:checked"
                ).length;

                // 모든 발주가 선택되었으면 전체선택 체크
                selectAllOrders.checked =
                    totalCount > 0 && checkedCount === totalCount;
                // 일부만 선택되었으면 중간 상태 표시
                selectAllOrders.indeterminate =
                    checkedCount > 0 && checkedCount < totalCount;
            });
        });
    }
});
