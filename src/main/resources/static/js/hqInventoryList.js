document.addEventListener('DOMContentLoaded', () => {
    loadIngredientOptions();
});

// 식재료명 드롭다운
async function loadIngredientOptions() {
    const select = document.getElementById('ingredient');
    try {
        const response = await fetch('/api/inventory/ingredient');
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