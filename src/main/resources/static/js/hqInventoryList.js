document.addEventListener('DOMContentLoaded', () => {
    loadIngredientOptions();
    loadInventoryList();
});



// 식재료명 드롭다운
async function loadIngredientOptions() {
    const select = document.getElementById('ingredient');
    try {
        const response = await fetch('/api/inventory/ingredients');
        if (!response.ok) throw new Error('식재료 목록 조회 실패: ' + response.status);

        const list = await response.json();
        list.forEach(item => {
            const option = document.createElement('option');
            option.value = item.ingredientId;
            option.textContent = item.ingredientName;
            select.appendChild(option);
        });
    } catch (e) {
        console.error(e);
    }
}

// 재고 목록 조회
async function loadInventoryList() {
    const tbody = document.getElementById('inventoryTableBody');
    try {
        const response = await fetch('/api/inventory/list');
        if (!response.ok) throw new Error('재고 목록 조회 실패: ' + response.status);

        const list = await response.json();

        if (list.length === 0) {
            tbody.innerHTML = '<tr><td colspan="10" class="text-center text-secondary py-4">조회된 재고가 없습니다.</td></tr>';
            return;
        }

        tbody.innerHTML = list.map((item, index) => `
            <tr>
              <td>${index + 1}</td>
              <td>${item.storeName}</td>
              <td>${item.ingredientName}</td>
              <td>${item.storageType}</td>
              <td>${item.currentQuantity} ${item.orderUnit}</td>
              <td>${formatDate(item.storeAt)}</td>
              <td>${formatDate(item.consumptionAt)}</td>
              <td>${item.userName ?? '-'}</td>
              <td><a class="btn btn-outline-primary btn-sm" href="#">상세 보기</a></td>
              <td><a class="btn btn-outline-secondary btn-sm" href="#">폐기 등록</a></td>
            </tr>
        `).join('');
    } catch (e) {
        console.error(e);
        tbody.innerHTML = '<tr><td colspan="10" class="text-center text-danger py-4">재고 목록을 불러오지 못했습니다.</td></tr>';
    }
}

// "2026-11-15 10:50"
function formatDate(value) {
    if (!value) return '-';
    return value.replace('T', ' ').substring(0, 16);
}