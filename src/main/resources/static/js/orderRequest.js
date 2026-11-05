// 직영점 발주 요청: MariaDB 품목 목록을 읽고 등록/수정 API로 저장한다.
let editingOrderId = "";

// 신규 요청과 수정 요청을 같은 화면에서 다룬다.
document.addEventListener("DOMContentLoaded", async function () {
    const form = document.getElementById("orderRequestForm");
    if (!form) return;
    try {
        await loadIngredientOptions();
        const path = window.location.pathname.split("/").filter(Boolean);
        if (path.length === 4 && path[2] === "request") {
            editingOrderId = path[3];
            await loadExistingOrder(editingOrderId);
        } else {
            addOrderRow();
        }
    } catch (error) {
        alert("발주 화면 초기화 실패: " + error.message);
    }
    document.getElementById("itemTbody").addEventListener("input", calculateOrderTotal);
    document.getElementById("itemTbody").addEventListener("change", calculateOrderTotal);
    form.addEventListener("submit", submitOrderRequest);
});

// DB 품목 정보를 template의 select 옵션에 넣어 중복 하드코딩을 막는다.
async function loadIngredientOptions() {
    const ingredients = await apiRequest("/api/ingredients");
    const select = document.getElementById("orderRowTemplate").content.querySelector(".item-select");
    select.innerHTML = '<option value="">-- 품목 선택 --</option>';
    ingredients.forEach(function (item) {
        const option = document.createElement("option");
        option.value = item.code;
        option.dataset.price = item.price;
        option.textContent = item.name + " (" + formatMoney(item.price) + ")";
        select.appendChild(option);
    });
}

function addOrderRow(code, quantity) {
    const body = document.getElementById("itemTbody");
    const template = document.getElementById("orderRowTemplate");
    const row = template.content.firstElementChild.cloneNode(true);
    body.appendChild(row);
    if (code) row.querySelector(".item-select").value = code;
    row.querySelector(".item-qty").value = quantity || 1;
    calculateOrderTotal();
}

function removeOrderRow(button) {
    const body = document.getElementById("itemTbody");
    if (body.rows.length <= 1) return alert("품목은 최소 한 줄 필요합니다.");
    button.closest("tr").remove();
    calculateOrderTotal();
}

// 화면 합계는 편의용이며 최종 단가는 서버의 DB 값으로 다시 계산한다.
function calculateOrderTotal() {
    let count = 0;
    let total = 0;
    document.querySelectorAll("#itemTbody tr").forEach(function (row, index) {
        const select = row.querySelector(".item-select");
        const price = Number(select.selectedOptions[0]?.dataset.price || 0);
        const quantity = Number(row.querySelector(".item-qty").value);
        const amount = Number.isInteger(quantity) && quantity > 0 ? price * quantity : 0;
        row.querySelector(".row-seq").textContent = index + 1;
        row.querySelector(".item-unit").textContent = formatMoney(price);
        row.querySelector(".item-total").textContent = formatMoney(amount);
        if (select.value && quantity > 0) count++;
        total += amount;
    });
    document.getElementById("itemCount").textContent = count;
    document.getElementById("grandTotal").textContent = total.toLocaleString("ko-KR");
}

async function loadExistingOrder(id) {
    const order = await apiRequest("/api/orders/" + encodeURIComponent(id));
    if (order.status !== "REQUESTED" || order.location !== "서울 한국대점") {
        throw new Error("본사 처리 후에는 수정할 수 없습니다.");
    }
    document.getElementById("requestTitle").textContent = "발주 요청 수정 (직영점)";
    document.getElementById("submitOrderButton").textContent = "수정 완료";
    order.items.forEach(function (item) { addOrderRow(item.code, item.quantity); });
}

async function submitOrderRequest(event) {
    event.preventDefault();
    const items = [];
    const used = new Set();
    document.querySelectorAll("#itemTbody tr").forEach(function (row) {
        items.push({
            code: row.querySelector(".item-select").value,
            quantity: Number(row.querySelector(".item-qty").value)
        });
    });
    for (const item of items) {
        if (!item.code || !Number.isSafeInteger(item.quantity) || item.quantity < 1) {
            return alert("품목과 수량을 확인해 주세요.");
        }
        if (used.has(item.code)) return alert("같은 품목을 중복 선택할 수 없습니다.");
        used.add(item.code);
    }
    const button = document.getElementById("submitOrderButton");
    button.disabled = true;
    try {
        await apiRequest(
            editingOrderId ? "/api/store/orders/" + encodeURIComponent(editingOrderId) : "/api/store/orders",
            editingOrderId ? "PUT" : "POST", { items: items }
        );
        alert(editingOrderId ? "발주 수정이 완료되었습니다." : "발주 요청이 DB에 저장되었습니다.");
        window.location.href = "/store/orders/list";
    } catch (error) {
        alert("저장 실패: " + error.message);
        button.disabled = false;
    }
}
