// 목록의 상세 버튼을 누르면 HTML 조각과 DB 값을 AJAX로 조회하여 오른쪽에 표시한다.
let currentDetailRow = null;
let currentDetailRequest = 0;

async function loadDetailDrawer(button) {
    const fragmentUrl = button.dataset.urlBase + encodeURIComponent(button.dataset.id);
    const apiUrl = button.dataset.apiBase + encodeURIComponent(button.dataset.id);
    const drawer = document.getElementById("commonDetailDrawer");
    const body = document.getElementById("commonDrawerBody");
    if (!drawer || !body) return;

    if (currentDetailRow) currentDetailRow.classList.remove("table-active");
    currentDetailRow = button.closest("tr");
    if (currentDetailRow) currentDetailRow.classList.add("table-active");

    document.getElementById("commonDrawerTitle").textContent = button.dataset.title || "상세 보기";
    body.textContent = "상세 정보를 불러오는 중입니다...";
    bootstrap.Offcanvas.getOrCreateInstance(drawer).show();

    // 연속으로 클릭했을 때 이전 응답이 새 화면을 덮어쓰지 못하도록 순서를 확인한다.
    const requestId = ++currentDetailRequest;
    try {
        const [htmlResponse, data] = await Promise.all([
            fetch(fragmentUrl), apiRequest(apiUrl)
        ]);
        if (!htmlResponse.ok) throw new Error("상세 템플릿 HTTP " + htmlResponse.status);
        const html = await htmlResponse.text();
        if (requestId !== currentDetailRequest) return;
        body.innerHTML = html;
        fillDetailFromDatabase(button.dataset.detailType, data, body);
    } catch (error) {
        console.error(error);
        if (requestId === currentDetailRequest) body.textContent = error.message;
    }
}

// 상세 HTML에 준비된 data-detail-field에 DB의 실제 값을 채운다.
function fillDetailFromDatabase(type, data, body) {
    let values = {};
    if (type === "order") {
        values = {
            number: data.id, location: data.location, datetime: data.date,
            status: orderStatusName(data.status), total: formatMoney(orderAmount(data))
        };
        const reasonBox = body.querySelector("#orderReasonBox");
        if (reasonBox) {
            reasonBox.hidden = !data.reason;
            reasonBox.querySelector("#orderReasonText").textContent = data.reason || "";
        }
        const items = body.querySelector("#detailItemRows");
        if (items) {
            items.replaceChildren();
            data.items.forEach(function (item, index) {
                const row = document.createElement("tr");
                [index + 1, item.code, item.name, item.quantity,
                    formatMoney(item.price), formatMoney(item.price * item.quantity)]
                    .forEach(function (value) { addTextCell(row, value); });
                items.appendChild(row);
            });
        }
    } else if (type === "inventory") {
        values = {
            number: data.id, location: data.location, manager: data.manager,
            name: data.name, category: data.code, storage: data.storageType,
            quantity: data.quantity + " " + data.unit,
            received: data.receivedDate, expiry: data.expiryDate,
            price: formatMoney(data.price)
        };
        const history = body.querySelector("#inventoryHistoryRows");
        if (history) {
            history.replaceChildren();
            if (!data.history || !data.history.length) {
                const row = document.createElement("tr");
                addTextCell(row, "폐기 이력이 없습니다.").colSpan = 3;
                history.appendChild(row);
            } else {
                data.history.forEach(function (record) {
                    const row = document.createElement("tr");
                    [record.datetime, record.quantity, record.disposalType].forEach(function (value) {
                        addTextCell(row, value);
                    });
                    history.appendChild(row);
                });
            }
        }
    } else if (type === "ingredient") {
        values = {
            number: data.code, name: data.name, category: data.category,
            unit: data.unit, storage: data.storageType, price: formatMoney(data.price)
        };
    } else if (type === "disposal") {
        values = {
            number: "D-" + data.id, datetime: data.datetime, location: data.location,
            name: data.name, quantity: data.quantity + " " + data.unit,
            type: data.type, method: data.method, manager: data.manager, reason: data.reason
        };
    }
    body.querySelectorAll("[data-detail-field]").forEach(function (element) {
        const key = element.dataset.detailField;
        if (values[key] !== undefined) element.textContent = values[key];
    });
}

document.addEventListener("DOMContentLoaded", function () {
    const drawer = document.getElementById("commonDetailDrawer");
    if (drawer) {
        drawer.addEventListener("hidden.bs.offcanvas", function () {
            currentDetailRequest++;
            if (currentDetailRow) currentDetailRow.classList.remove("table-active");
            currentDetailRow = null;
        });
    }
});
