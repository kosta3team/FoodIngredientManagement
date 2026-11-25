document.addEventListener('DOMContentLoaded', () => {
    loadInventoryList();
    document.getElementById('searchBtn').addEventListener('click', searchInventory);

    // 기간선택 전에는 날짜 입력 막기
    const dateType = document.getElementById('dateType');
    const toggleDateInputs = () => {
        const disabled = !dateType.value;
        document.getElementById('startDate').disabled = disabled;
        document.getElementById('endDate').disabled = disabled;
    };
    dateType.addEventListener('change', toggleDateInputs);
    toggleDateInputs();
});



// 재고 목록 조회
async function loadInventoryList(params = new URLSearchParams()) {
    const tbody = document.getElementById('inventoryTableBody');
    try {
        const response = await fetch('/api/inventory/list?' + params.toString());
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
              
              <td>
                <button type="button" class="btn btn-outline-primary btn-sm btn-detail"
                        data-store-id="${item.storeId}"
                        data-ingredient-id="${item.ingredientId}"
                        data-order-detail-id="${item.orderDetailId}">상세 보기</button>
              </td>
              
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

function searchInventory() {
    const params = new URLSearchParams();

    const ingredientName = document.getElementById('ingredientName').value.trim();
    if (ingredientName) params.append('ingredientName', ingredientName);

    const storeName = document.getElementById('storeName').value.trim();
    if (storeName) params.append('storeName', storeName);

    const dateType = document.getElementById('dateType').value;
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (dateType) {
        if (startDate && endDate && startDate > endDate) {
            alert('시작일이 종료일보다 늦을 수 없습니다.');
            return;
        }

        const prefix = dateType === 'receivedAt' ? 'store' : 'manage';
        if (startDate) params.append(prefix + 'StartDate', startDate);
        if (endDate) params.append(prefix + 'EndDate', endDate);
    }

    loadInventoryList(params);
}