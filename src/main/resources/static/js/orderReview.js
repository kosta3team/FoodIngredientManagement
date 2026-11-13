// 본사 발주 심사: 공통 사유 + 다른 사유가 필요한 소수 건에만 예외 사유 적용.
const orderExceptions = {};

document.addEventListener("DOMContentLoaded", function () {
    if (!document.getElementById("hqOrderTable")) return;
    renderHqOrders();
    document.getElementById("bulkStatus").addEventListener("change", updateOrderSelection);
    document.getElementById("hqOrderTable").addEventListener("change", updateOrderSelection);
});

async function renderHqOrders() {
    const body = document.querySelector("#hqOrderTable tbody");
    body.replaceChildren();
    try {
        const orders = await apiRequest("/api/orders?scope=hq");
        orders.forEach(function (order, index) {
            const row = document.createElement("tr");
            row.dataset.id = order.id;
            row.dataset.date = order.date.slice(0, 10);
            row.dataset.location = order.location;
            row.dataset.status = order.status;
            const boxCell = addTextCell(row, "");
            const check = document.createElement("input");
            check.type = "checkbox";
            check.className = "form-check-input order-checkbox";
            check.value = order.id;
            check.disabled = !["REQUESTED", "PENDING"].includes(order.status);
            boxCell.appendChild(check);
            addTextCell(row, index + 1);
            addTextCell(row, order.id);
            addTextCell(row, order.date);
            addTextCell(row, order.location);
            addTextCell(row, orderSummary(order));
            addTextCell(row, formatMoney(orderAmount(order)), "text-end fw-bold");
            const status = addTextCell(row, "");
            const badge = document.createElement("span");
            badge.className = "badge " + orderStatusBadge(order.status);
            badge.textContent = orderStatusName(order.status);
            status.appendChild(badge);
            addTextCell(row, "-", "target-status");
            const detailCell = addTextCell(row, "");
            const detail = document.createElement("button");
            detail.className = "btn btn-sm btn-outline-primary";
            detail.textContent = "상세";
            detail.dataset.urlBase = "/orders/detail/";
            detail.dataset.apiBase = "/api/orders/";
            detail.dataset.detailType = "order";
            detail.dataset.id = order.id;
            detail.dataset.title = "본사 발주 상세";
            detail.onclick = function () { loadDetailModal(detail); };
            detailCell.appendChild(detail);
            body.appendChild(row);
        });
        updateOrderSelection();
    } catch (error) {
        alert("본사 발주 조회 실패: " + error.message);
    }
}

// 검색으로 화면에서 제외된 발주는 전체 선택 대상에 포함하지 않는다.
function selectAllOrders(check) {
    document.querySelectorAll("#hqOrderTable tbody tr").forEach(function (row) {
        const item = row.querySelector(".order-checkbox");
        if (!item.disabled && !row.hidden) item.checked = check.checked;
    });
    updateOrderSelection();
}

function getSelectedOrders() {
    return Array.from(document.querySelectorAll("#hqOrderTable .order-checkbox:checked"))
        .filter(function (item) { return !item.closest("tr").hidden; });
}

// 선택한 건수와 예외 사유 대상 선택지를 갱신한다.
function updateOrderSelection() {
    const selected = getSelectedOrders();
    const target = document.getElementById("bulkStatus").value;
    const needsReason = target === "PENDING" || target === "REJECTED";
    document.getElementById("bulkReasonArea").hidden = !needsReason;
    document.getElementById("applyBulkStatus").disabled = !selected.length || !target;
    document.getElementById("selectedOrderCount").textContent = selected.length;
    const picker = document.getElementById("exceptionOrderId");
    const previous = picker.value;
    picker.replaceChildren(new Option("발주번호 선택", ""));
    selected.forEach(function (input) { picker.add(new Option(input.value, input.value)); });
    picker.value = previous;
    document.querySelectorAll("#hqOrderTable tbody tr").forEach(function (row) {
        const checkbox = row.querySelector(".order-checkbox");
        row.querySelector(".target-status").textContent =
            checkbox.checked && !row.hidden && target ? orderStatusName(target) : "-";
    });
    const all = document.getElementById("selectAllOrders");
    const eligible = Array.from(document.querySelectorAll("#hqOrderTable .order-checkbox:not(:disabled)"))
        .filter(function (box) { return !box.closest("tr").hidden; });
    all.checked = eligible.length > 0 && eligible.every(function (box) { return box.checked; });
    all.indeterminate = selected.length > 0 && !all.checked;
    showExceptionReasons();
}

function addExceptionReason() {
    const id = document.getElementById("exceptionOrderId").value;
    const reason = document.getElementById("exceptionReason").value.trim();
    if (!id || !reason) return alert("발주번호와 예외 사유를 입력하세요.");
    orderExceptions[id] = reason;
    document.getElementById("exceptionReason").value = "";
    showExceptionReasons();
}

function showExceptionReasons() {
    const list = document.getElementById("exceptionReasonList");
    list.replaceChildren();
    const selectedIds = new Set(getSelectedOrders().map(function (box) { return box.value; }));
    Object.keys(orderExceptions).forEach(function (id) {
        if (!selectedIds.has(id)) { delete orderExceptions[id]; return; }
        const li = document.createElement("li");
        li.className = "list-group-item d-flex justify-content-between align-items-center";
        const text = document.createElement("span");
        text.textContent = id + ": " + orderExceptions[id];
        const remove = document.createElement("button");
        remove.type = "button";
        remove.className = "btn btn-outline-danger btn-sm";
        remove.textContent = "삭제";
        remove.onclick = function () { delete orderExceptions[id]; showExceptionReasons(); };
        li.append(text, remove);
        list.appendChild(li);
    });
}

// 여러 건을 하나의 PATCH 요청으로 전송한다. 서버에서 전부 성공하거나 전부 취소된다.
async function applyBulkStatus() {
    const target = document.getElementById("bulkStatus").value;
    const common = document.getElementById("bulkReason").value.trim();
    const items = [];
    for (const checkbox of getSelectedOrders()) {
        const row = checkbox.closest("tr");
        if (row.dataset.status === "PENDING" && target === "PENDING") {
            return alert("이미 대기 상태인 건은 다시 대기로 변경할 수 없습니다.");
        }
        const reason = orderExceptions[checkbox.value] || common;
        if (target !== "APPROVED" && !reason) return alert("대기/거절 사유를 입력하세요.");
        items.push({ orderId: checkbox.value, status: target, reason: target === "APPROVED" ? "" : reason });
    }
    if (!items.length || !target) return;
    if (!confirm(items.length + "건을 " + orderStatusName(target) + " 처리하시겠습니까?")) return;
    try {
        await apiRequest("/api/hq/orders/status", "PATCH", { items: items });
        Object.keys(orderExceptions).forEach(function (key) { delete orderExceptions[key]; });
        document.getElementById("bulkReason").value = "";
        document.getElementById("bulkStatus").value = "";
        await renderHqOrders();
        alert("본사 상태 변경이 DB에 저장되었습니다.");
    } catch (error) {
        alert("일괄 처리 실패: " + error.message);
    }
}
