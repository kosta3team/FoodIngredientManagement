// 발주 목록 전체선택 및 선택 해제
// DB 처리 없이 화면 체크박스 상태만 변경한다.
document.addEventListener("DOMContentLoaded", function () {
    const selectAll = document.getElementById("select-all-orders");
    const orderCheckboxes = document.querySelectorAll(".order-checkbox");

    if (!selectAll || orderCheckboxes.length === 0) {
        return;
    }

    // 전체선택 체크박스 클릭 시 목록을 모두 선택하거나 해제한다.
    selectAll.addEventListener("change", function () {
        orderCheckboxes.forEach(function (checkbox) {
            checkbox.checked = selectAll.checked;
        });
    });

    // 개별 항목 선택 상태에 따라 전체선택 표시를 변경한다.
    orderCheckboxes.forEach(function (checkbox) {
        checkbox.addEventListener("change", function () {
            const selectedCount = document.querySelectorAll(".order-checkbox:checked").length;
            selectAll.checked = selectedCount === orderCheckboxes.length;
            selectAll.indeterminate = selectedCount > 0 && selectedCount < orderCheckboxes.length;
        });
    });
});
