
// 직영점은 자기 매장 발주를 조회한다.
// 승인/대기/거절 상태 변경은 본사에서만 처리한다.


// ========================================
// 페이지 초기화
// ========================================

document.addEventListener("DOMContentLoaded", function () {

    // 발주 목록 조회
    if (document.getElementById("storeOrderTable")) {
        renderStoreOrders();
    }

    // 발주 요청 모달 초기화
    initOrderRequestModal();

});


// ========================================
// 1. 발주 목록 조회
// ========================================

async function renderStoreOrders() {

    const body = document.querySelector("#storeOrderTable tbody");

    if (!body) return;

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


            // 상태
            const statusCell = addTextCell(row, "");

            const badge = document.createElement("span");

            badge.className = "badge " + orderStatusBadge(order.status);
            badge.textContent = orderStatusName(order.status);

            statusCell.appendChild(badge);


            // 상세보기
            const detailCell = addTextCell(row, "");

            const detail = document.createElement("button");

            detail.className = "btn btn-sm btn-outline-primary";
            detail.textContent = "상세";

            detail.dataset.urlBase = "/store/orders/detail/";
            detail.dataset.apiBase = "/api/orders/";
            detail.dataset.detailType = "order";
            detail.dataset.id = order.id;
            detail.dataset.title = "직영점 발주 상세";

            detail.onclick = function () {
                loadDetailModal(detail);
            };

            detailCell.appendChild(detail);


            // 수정
            const editCell = addTextCell(row, "");

            const edit = document.createElement("button");

            edit.className = "btn btn-sm btn-outline-secondary";
            edit.textContent = "수정";
            edit.disabled = order.status !== "REQUESTED";

            edit.onclick = function () {
                window.location.href =
                    "/store/orders/request/" + encodeURIComponent(order.id);
            };

            editCell.appendChild(edit);


            // 삭제
            const deleteCell = addTextCell(row, "");

            const remove = document.createElement("button");

            remove.className = "btn btn-sm btn-outline-danger";
            remove.textContent = "삭제";
            remove.disabled = order.status !== "REQUESTED";

            remove.onclick = function () {
                deleteStoreOrder(order.id);
            };

            deleteCell.appendChild(remove);

            body.appendChild(row);

        });


        document.getElementById("storeOrderTotal").textContent =
            formatMoney(total);


    } catch (error) {

        console.error("발주 목록 조회 오류:", error);

        alert("발주 목록 조회 실패: " + error.message);

    }

}


// ========================================
// 2. 발주 요청 삭제
// ========================================

// 버튼 비활성화 외에도 서버가 REQUESTED 상태인지 다시 검사한다.

async function deleteStoreOrder(id) {

    if (!confirm("발주 요청을 삭제하시겠습니까?")) return;

    try {

        await apiRequest(
            "/api/store/orders/" + encodeURIComponent(id),
            "DELETE"
        );

        await renderStoreOrders();

    } catch (error) {

        alert("삭제 실패: " + error.message);

    }

}


// ========================================
// 3. 발주 요청 모달
// ========================================

async function initOrderRequestModal() {

    const openButton = document.getElementById("openOrderRequestButton");
    const modalElement = document.getElementById("orderRequestModal");
    const modalBody = document.getElementById("orderRequestModalBody");

    if (!openButton || !modalElement || !modalBody) return;

    const orderModal = bootstrap.Modal.getOrCreateInstance(modalElement);

    let isLoading = false;

    openButton.addEventListener("click", async function () {

        if (isLoading) return;

        isLoading = true;
        openButton.disabled = true;

        modalBody.textContent = "발주 화면을 불러오는 중입니다.";

        orderModal.show();

        try {

            // 발주 요청 HTML 조각 조회
            const response = await fetch("/store/orders/request");

            if (!response.ok) {
                throw new Error("발주 화면 조회 실패: HTTP " + response.status);
            }

            const html = await response.text();

            modalBody.innerHTML = html;

            // 발주 요청 화면 초기화
            if (typeof window.initOrderRequest === "function") {
                window.initOrderRequest();
            }

        } catch (error) {

            console.error("발주 요청 모달 오류:", error);

            modalBody.textContent = "발주 화면을 불러오지 못했습니다.";

        } finally {

            isLoading = false;
            openButton.disabled = false;

        }

    });

}