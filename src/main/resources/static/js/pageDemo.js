// 화면의 검색은 현재 조회된 DB 결과에만 적용한다.
// 실제 서비스에서는 검색 조건을 API 쿼리 파라미터로 옮겨 서버 페이징을 구현한다.
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll("form[data-search-table]").forEach(function (form) {
        form.addEventListener("submit", function (event) {
            event.preventDefault();
            const table = document.getElementById(form.dataset.searchTable);
            if (!table) return;
            const keyword = (form.querySelector('[name="keyword"]')?.value || "").trim().toLowerCase();
            const location = form.querySelector('[name="location"]')?.value || "";
            const status = form.querySelector('[name="status"]')?.value || "";
            const start = form.querySelector('[name="startDate"]')?.value || "";
            const end = form.querySelector('[name="endDate"]')?.value || "";
            table.querySelectorAll("tbody tr").forEach(function (row) {
                const visible = row.textContent.toLowerCase().includes(keyword) &&
                    (!location || row.dataset.location === location) &&
                    (!status || row.dataset.status === status) &&
                    (!start || row.dataset.date >= start) &&
                    (!end || row.dataset.date <= end);
                row.hidden = !visible;
                // 숨긴 발주가 본사의 일괄 처리에 포함되지 않도록 체크를 해제한다.
                if (!visible) {
                    const check = row.querySelector(".order-checkbox");
                    if (check) check.checked = false;
                }
            });
            if (table.id === "hqOrderTable" && typeof updateOrderSelection === "function") {
                updateOrderSelection();
            }
        });
    });
    if (document.getElementById("ingredientTable")) renderIngredients();
    if (document.getElementById("inventoryTable")) renderInventory();
    if (document.getElementById("disposalTable")) renderDisposals();
    if (document.getElementById("ingredientRequestForm")) setupIngredientForm();
    if (document.getElementById("disposalRequestForm")) setupDisposalForm();
});

// 여러 화면이 같은 상세 드로어 함수를 쓰도록 버튼의 URL 정보만 변경한다.
function makeDetailButton(row, type, id, urlBase, apiBase, title) {
    const cell = addTextCell(row, "");
    const button = document.createElement("button");
    button.type = "button";
    button.className = "btn btn-sm btn-outline-primary";
    button.textContent = "상세";
    button.dataset.detailType = type;
    button.dataset.id = id;
    button.dataset.urlBase = urlBase;
    button.dataset.apiBase = apiBase;
    button.dataset.title = title;
    button.onclick = function () {
        loadDetailModal(button);
    };
    cell.appendChild(button);
}

async function renderIngredients() {
    const body = document.querySelector("#ingredientTable tbody");

    body.replaceChildren();

    try {
        const items = await apiRequest("/api/ingredients");

        items.forEach(function (item) {
            const row = document.createElement("tr");

            // 체크박스
            const select = addTextCell(row, "");

            const input = document.createElement("input");
            input.type = "checkbox";
            input.className = "form-check-input";
            input.value = item.code;

            select.appendChild(input);

            // 식재료 목록 컬럼
            [
                item.name,
                item.category,
                item.unit,
                item.storageType,
                formatMoney(item.price)
            ].forEach(function (value) {
                addTextCell(row, value);
            });

            // 상세 버튼
            makeDetailButton(
                row,
                "ingredient",
                item.code,
                "/ingredients/detail/",
                "/api/ingredients/",
                "식재료 상세 정보"
            );

            body.appendChild(row);
        });

    } catch (error) {
        showLoadError(body, error);
    }
}

async function renderInventory() {
    const body = document.querySelector("#inventoryTable tbody");
    body.replaceChildren();
    try {
        const list = await apiRequest("/api/inventory");
        list.forEach(function (item) {
            const row = document.createElement("tr");
            row.dataset.location = item.location;
            row.dataset.date = item.receivedDate;
            [item.id, item.location, item.name, item.storageType,
                item.quantity + " " + item.unit, item.receivedDate,
                item.expiryDate, item.manager].forEach(function (value) {
                addTextCell(row, value);
            });
            makeDetailButton(row, "inventory", item.id, "/inventory/detail/", "/api/inventory/", "재고 상세");
            body.appendChild(row);
        });
    } catch (error) {
        showLoadError(body, error);
    }
}

async function renderDisposals() {
    const body = document.querySelector("#disposalTable tbody");
    body.replaceChildren();
    const store = window.location.pathname.startsWith("/store/");
    try {
        const list = await apiRequest("/api/disposals?scope=" + (store ? "store" : "hq"));
        list.forEach(function (item, index) {
            const row = document.createElement("tr");
            row.dataset.location = item.location;
            row.dataset.date = item.datetime.slice(0, 10);
            [index + 1, "D-" + item.id, item.datetime, item.location, item.name,
                item.quantity + " " + item.unit, item.type, item.method, item.manager]
                .forEach(function (value) {
                    addTextCell(row, value);
                });
            makeDetailButton(row, "disposal", item.id,
                store ? "/store/disposal/detail/" : "/hq/disposal/detail/",
                "/api/disposals/", "폐기 상세");
            body.appendChild(row);
        });
        // 단위(kg/개/팩)가 다르므로 단순 수량합이 아닌 건수만 통계에 사용한다.
        const summary = document.getElementById("disposalCount");
        if (summary) summary.textContent = list.length + "건";
    } catch (error) {
        showLoadError(body, error);
    }
}

function showLoadError(body, error) {
    console.error(error);
    const row = document.createElement("tr");
    const cell = addTextCell(row, "MariaDB 조회 실패: " + error.message);
    cell.colSpan = 12;
    cell.className = "text-danger p-3";
    body.appendChild(row);
}

// 식재료 등록은 기존 입력 UI를 유지하면서 SQL 등록 API로 전송한다.
function setupIngredientForm() {
    const form = document.getElementById("ingredientRequestForm");
    form.addEventListener("submit", async function (event) {
        event.preventDefault();
        if (!form.reportValidity()) return;
        const data = {
            name: document.getElementById("ingredientName").value.trim(),
            category: document.getElementById("ingredientGroup").value,
            unit: document.getElementById("ingredientUnit").value.trim(),
            storageType: document.getElementById("storageType").value,
            price: Number(document.getElementById("ingredientPrice").value),
            shelfLifeDays: Number(document.getElementById("expiryDays").value),
            safetyDays: Number(document.getElementById("safetyDays").value)
        };
        try {
            await apiRequest("/api/ingredients", "POST", data);
            alert("식재료가 MariaDB에 등록되었습니다. 사진 업로드는 아직 지원하지 않습니다.");
            window.location.href = "/ingredients/list";
        } catch (error) {
            alert("식재료 등록 실패: " + error.message);
        }
    });
}

// 폐기 등록할 재고를 DB에서 불러온다. 재고 ID를 직접 잘못 입력하지 않도록 한다.
async function setupDisposalForm() {
    const form = document.getElementById("disposalRequestForm");
    const inventorySelect = document.getElementById("disposalInventory");
    const ingredientSelect = document.getElementById("disposalIngredient");
    const locationSelect = document.getElementById("disposalLocation");
    const store = window.location.pathname.startsWith("/store/");
    let inventory = [];
    try {
        inventory = await apiRequest("/api/inventory");
    } catch (error) {
        return alert("재고 조회 실패: " + error.message);
    }

    function refreshInventoryOptions() {
        const location = store ? "서울 한국대점" : locationSelect.value;
        const available = inventory.filter(function (item) {
            return item.location === location && Number(item.quantity) > 0;
        });
        inventorySelect.replaceChildren(new Option("재고 선택", ""));
        ingredientSelect.replaceChildren(new Option("식재료 선택", ""));
        available.forEach(function (item) {
            inventorySelect.add(new Option(item.id + " / " + item.name + " / 잔여 " + item.quantity + " " + item.unit, item.id));
            if (!Array.from(ingredientSelect.options).some(function (opt) {
                return opt.value === item.code;
            })) {
                ingredientSelect.add(new Option(item.name, item.code));
            }
        });
    }

    if (!store && locationSelect) locationSelect.addEventListener("change", refreshInventoryOptions);
    ingredientSelect.addEventListener("change", function () {
        const selectedCode = ingredientSelect.value;
        const location = store ? "서울 한국대점" : locationSelect.value;
        const chosen = inventory.find(function (item) {
            return item.location === location && item.code === selectedCode;
        });
        if (chosen) inventorySelect.value = chosen.id;
    });
    inventorySelect.addEventListener("change", function () {
        const chosen = inventory.find(function (item) {
            return String(item.id) === inventorySelect.value;
        });
        if (chosen) ingredientSelect.value = chosen.code;
    });
    refreshInventoryOptions();
    form.addEventListener("submit", async function (event) {
        event.preventDefault();
        if (!form.reportValidity()) return;
        const location = store ? "서울 한국대점" : locationSelect.value;
        const payload = {
            scope: store ? "store" : "hq",
            inventoryId: Number(inventorySelect.value),
            location: location,
            quantity: Number(document.getElementById("disposalQty").value),
            type: document.getElementById("disposalType").value,
            method: document.getElementById("disposalMethod").value,
            reason: document.getElementById("disposalReason").value.trim(),
            manager: document.getElementById("disposalManager").value.trim(),
            disposedAt: document.getElementById("disposedAt").value
        };
        const chosen = inventory.find(function (item) {
            return item.id === payload.inventoryId;
        });
        if (!chosen || Number(chosen.quantity) < payload.quantity || payload.quantity <= 0) {
            return alert("폐기수량이 남은 재고보다 많습니다.");
        }
        try {
            await apiRequest("/api/disposals", "POST", payload);
            alert("폐기 등록과 재고 차감이 MariaDB에 저장되었습니다. 첨부사진 저장은 아직 지원하지 않습니다.");
            window.location.href = store ? "/store/disposal/list" : "/hq/disposal/list";
        } catch (error) {
            alert("폐기 등록 실패: " + error.message);
        }
    });
}
