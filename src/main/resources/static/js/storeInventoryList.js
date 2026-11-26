document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('searchBtn').addEventListener('click', searchInventory);
    dateType.addEventListener('change', toggleDateInputs);
    toggleDateInputs();
});

async function loadInventoryList(params = new URLSearchParams()){
    const tbody = document.getElementById('inventoryTableBody');
    try {
        const response = await fetch('/api/store/inventory/list?' + params.toString());
        if (!response.ok) throw new Error('재고 목록 조회 실패: ' + response.status);

        const list = await response.json();

        if (list.length === 0) {
            tbody.innerHTML = '<tr><td colspan="10" class="text-center text-secondary py-4">조회된 재고가 없습니다.</td></tr>';
            return;
        }

        tbody.innerHTML = list.map((item, index) => `
         <tr>
              <td>${index + 1}</td>
              <td>${item.subCategoryName}</td>
              <td>${item.ingredientName}</td>
              <td>${item.storageType}</td>
              <td>${item.currentQuantity} ${item.orderUnit}</td>
              <td>${item.userName ?? '-'}</td>
              
              
               <td>
                <button type="button" class="btn btn-outline-primary btn-sm btn-detail"
                        data-store-id="${item.storeId}"
                        data-ingredient-id="${item.ingredientId}"
                        >상세 보기</button>
              </td>
              
               <td><a class="btn btn-outline-secondary btn-sm" href="#">폐기 등록</a></td>
               </tr>
        `).join('');
        } catch (e) {
        console.error(e);
        tbody.innerHTML = '<tr><td colspan="10" class="text-center text-danger py-4">재고 목록을 불러오지 못했습니다.</td></tr>';
    }}


function searchInventory() {
    const params = new URLSearchParams();

    const subCategoryName = document.getElementById('subCategoryName').value.trim();
    if (subCategoryName) params.append('subCategoryName', subCategoryName);

    const ingredientName = document.getElementById('ingredientName').value.trim();
    if (ingredientName) params.append('ingredientName', ingredientName);

    loadInventoryList(params);
}
