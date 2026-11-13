// 직영점은 자기 매장 발주를 조회한다. 승인/대기/거절은 본사에서만 변경한다.
document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("storeOrderTable")) renderStoreOrders();
});

async function renderStoreOrders() {
    const body = document.querySelector("#storeOrderTable tbody");
    body.replaceChildren();
    try {
        const orders = await apiRequest("/api/orders?scope=store");
        let total = 0;
        orders.forEach(function (order, index) {
            const row = document.createElement("tr");
            row.dataset.date = order.date.slice(0, 10);
            row.dataset.status = order.status;
            row.dataset.id = order.id;
            total += orderAmount(order);
            addTextCell(row, index + 1);
            addTextCell(row, order.date);
            addTextCell(row, orderSummary(order));
            addTextCell(row, formatMoney(orderAmount(order)), "text-end fw-bold");
            const statusCell = addTextCell(row, "");
            const badge = document.createElement("span");
            badge.className = "badge " + orderStatusBadge(order.status);
            badge.textContent = orderStatusName(order.status);
            statusCell.appendChild(badge);
            const detailCell = addTextCell(row, "");
            const detail = document.createElement("button");
            detail.className = "btn btn-sm btn-outline-primary";
            detail.textContent = "상세";
            detail.dataset.urlBase = "/store/orders/detail/";
            detail.dataset.apiBase = "/api/orders/";
            detail.dataset.detailType = "order";
            detail.dataset.id = order.id;
            detail.dataset.title = "직영점 발주 상세";
            detail.onclick = function () { loadDetailModal(detail); };
            detailCell.appendChild(detail);
            const editCell = addTextCell(row, "");
            const edit = document.createElement("button");
            edit.className = "btn btn-sm btn-outline-secondary";
            edit.textContent = "수정";
            edit.disabled = order.status !== "REQUESTED";
            edit.onclick = function () {
                window.location.href = "/store/orders/request/" + encodeURIComponent(order.id);
            };
            editCell.appendChild(edit);
            const deleteCell = addTextCell(row, "");
            const remove = document.createElement("button");
            remove.className = "btn btn-sm btn-outline-danger";
            remove.textContent = "삭제";
            remove.disabled = order.status !== "REQUESTED";
            remove.onclick = function () { deleteStoreOrder(order.id); };
            deleteCell.appendChild(remove);
            body.appendChild(row);
        });
        document.getElementById("storeOrderTotal").textContent = formatMoney(total);
    } catch (error) {
        alert("발주 목록 조회 실패: " + error.message);
    }
}

// 버튼 비활성화 외에도 서버가 REQUESTED 상태인지 다시 검사한다.
async function deleteStoreOrder(id) {
    if (!confirm("발주 요청을 삭제하시겠습니까?")) return;
    try {
        await apiRequest("/api/store/orders/" + encodeURIComponent(id), "DELETE");
        await renderStoreOrders();
    } catch (error) {
        alert("삭제 실패: " + error.message);
    }
}
