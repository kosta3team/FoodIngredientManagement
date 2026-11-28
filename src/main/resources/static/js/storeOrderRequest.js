
document.addEventListener('DOMContentLoaded', function () {

    // ==============================
    // DOM 요소
    // ==============================
    const orderRequestModal = document.getElementById('orderRequestModal');
    const orderRequestForm = document.getElementById('orderRequestForm');
    const orderRequestItemBody = document.getElementById('orderRequestItemBody');

    const addOrderItemBtn = document.getElementById('addOrderItemBtn');
    const submitOrderBtn = document.getElementById('submitOrderBtn');

    const selectedIngredientCount = document.getElementById('selectedIngredientCount');
    const orderTotalPrice = document.getElementById('orderTotalPrice');

    // ==============================
    // 데이터
    // ==============================
    let ingredientList = [];
    let ingredientLoaded = false;

    // 임시 매장 ID
    const STORE_ID = 1;

    // ==============================
    // 초기 DOM 확인
    // ==============================
    if (!orderRequestItemBody || !addOrderItemBtn) {
        console.error('[직영점 발주요청] 필요한 DOM 요소를 찾을 수 없습니다.');
        return;
    }

    // ==============================
    // 식재료 목록 조회 API
    // ==============================
    async function fetchIngredientList() {
        try {
            const response = await fetch('/api/v1/store/ingredients');

            console.log('[직영점 발주요청] 응답 상태:', response.status);

            if (!response.ok) {
                throw new Error(`HTTP 오류: ${response.status}`);
            }

            const data = await response.json();

            console.log('[직영점 발주요청] 실제 API 데이터:', data);

            ingredientList = Array.isArray(data)
                ? data
                : data.data ?? data.result ?? [];

            console.log('[직영점 발주요청] 조회된 식재료:', ingredientList.length);

            ingredientLoaded = true;

            return ingredientList;

        } catch (error) {
            console.error('[직영점 발주요청] 식재료 조회 실패:', error);

            ingredientList = [];
            ingredientLoaded = false;

            return [];
        }
    }

    // ==============================
    // 현재 재고값 추출
    // ==============================
    function getCurrentQuantity(ingredient) {
        const quantity = ingredient.currentQuantity ?? ingredient.supplyQuantity;

        return Number(quantity ?? 0);
    }

    // ==============================
    // 식재료 드롭다운 생성
    // ==============================
    function createIngredientSelect() {

        const select = document.createElement('select');

        select.className = 'form-select form-select-sm ingredient-select';
        select.name = 'ingredientId';
        select.required = true;

        const defaultOption = document.createElement('option');

        defaultOption.value = '';
        defaultOption.textContent = '-- 품목 선택 --';

        select.appendChild(defaultOption);

        ingredientList.forEach(function (ingredient) {

            const option = document.createElement('option');

            option.value = ingredient.ingredientId;
            option.textContent = ingredient.ingredientName;

            option.dataset.supplyPrice = ingredient.supplyPrice;
            option.dataset.currentQuantity = getCurrentQuantity(ingredient);

            select.appendChild(option);
        });

        return select;
    }

    // ==============================
    // 발주 품목 행 생성
    // ==============================
    function createOrderItemRow() {

        const tr = document.createElement('tr');

        // 1. 순번
        const numberTd = document.createElement('td');

        numberTd.className = 'text-center item-number';

        // 2. 식재료 선택
        const ingredientTd = document.createElement('td');

        const ingredientSelect = createIngredientSelect();

        ingredientTd.appendChild(ingredientSelect);

        // 3. 현재 수량
        const currentQuantityTd = document.createElement('td');

        const currentQuantityInput = document.createElement('input');

        currentQuantityInput.type = 'text';
        currentQuantityInput.className = 'form-control form-control-sm current-quantity';
        currentQuantityInput.value = '-';
        currentQuantityInput.readOnly = true;

        currentQuantityTd.appendChild(currentQuantityInput);

        // 4. 주문 수량
        const quantityTd = document.createElement('td');

        const quantityInput = document.createElement('input');

        quantityInput.type = 'number';
        quantityInput.name = 'quantity';
        quantityInput.className = 'form-control form-control-sm quantity-input';
        quantityInput.min = '1';
        quantityInput.step = '1';
        quantityInput.value = '1';

        quantityTd.appendChild(quantityInput);

        // 5. 공급 단가
        const supplyPriceTd = document.createElement('td');

        supplyPriceTd.className = 'text-nowrap';

        const supplyPriceInput = document.createElement('input');

        supplyPriceInput.type = 'text';
        supplyPriceInput.className = 'form-control form-control-sm supply-price';
        supplyPriceInput.value = '0';
        supplyPriceInput.readOnly = true;

        supplyPriceTd.appendChild(supplyPriceInput);

        // 6. 주문 금액
        const itemTotalTd = document.createElement('td');

        itemTotalTd.className = 'text-nowrap fw-semibold item-total';
        itemTotalTd.textContent = '0원';

        // 7. 삭제 버튼
        const deleteTd = document.createElement('td');

        const deleteBtn = document.createElement('button');

        deleteBtn.type = 'button';
        deleteBtn.className = 'btn btn-outline-danger btn-sm delete-item-btn';
        deleteBtn.textContent = '삭제';

        deleteTd.appendChild(deleteBtn);

        // 행 구성
        tr.appendChild(numberTd);
        tr.appendChild(ingredientTd);
        tr.appendChild(currentQuantityTd);
        tr.appendChild(quantityTd);
        tr.appendChild(supplyPriceTd);
        tr.appendChild(itemTotalTd);
        tr.appendChild(deleteTd);

        // 식재료 선택 이벤트
        ingredientSelect.addEventListener('change', function () {

            const selectedOption = this.options[this.selectedIndex];

            const supplyPrice = Number(selectedOption.dataset.supplyPrice || 0);
            const currentQuantity = selectedOption.dataset.currentQuantity;

            currentQuantityInput.value =
                currentQuantity == null || currentQuantity === ''
                    ? '-'
                    : Number(currentQuantity).toLocaleString('ko-KR');

            supplyPriceInput.value = supplyPrice.toLocaleString('ko-KR');

            calculateRowTotal(tr);
            calculateOrderSummary();
        });

        // 주문 수량 변경 이벤트
        quantityInput.addEventListener('input', function () {

            calculateRowTotal(tr);
            calculateOrderSummary();
        });

        // 행 삭제 이벤트
        deleteBtn.addEventListener('click', function () {

            tr.remove();

            updateRowNumbers();
            calculateOrderSummary();
        });

        return tr;
    }

    // ==============================
    // 재고 및 주문 수량 검증
    // ==============================
    function isQuantityValid(tr) {

        const ingredientSelect = tr.querySelector('.ingredient-select');
        const quantityInput = tr.querySelector('.quantity-input');

        if (!ingredientSelect.value) {
            return false;
        }

        const selectedOption =
            ingredientSelect.options[ingredientSelect.selectedIndex];

        const currentQuantity =
            Number(selectedOption.dataset.currentQuantity);

        const orderQuantity = Number(quantityInput.value);

        return Number.isFinite(currentQuantity)
            && Number.isInteger(orderQuantity)
            && orderQuantity > 0
            && orderQuantity <= currentQuantity;
    }

    // ==============================
    // 행별 주문금액 계산
    // ==============================
    function calculateRowTotal(tr) {

        const ingredientSelect = tr.querySelector('.ingredient-select');
        const quantityInput = tr.querySelector('.quantity-input');
        const itemTotalTd = tr.querySelector('.item-total');

        const selectedOption =
            ingredientSelect.options[ingredientSelect.selectedIndex];

        if (!ingredientSelect.value) {

            itemTotalTd.textContent = '0원';
            itemTotalTd.classList.remove('text-danger');

            return;
        }

        const supplyPrice = Number(selectedOption.dataset.supplyPrice || 0);
        const quantity = Number(quantityInput.value || 0);

        if (!isQuantityValid(tr)) {

            itemTotalTd.textContent = '수량 확인';
            itemTotalTd.classList.add('text-danger');

            return;
        }

        itemTotalTd.classList.remove('text-danger');

        const total = supplyPrice * quantity;

        itemTotalTd.textContent = total.toLocaleString('ko-KR') + '원';
    }

    // ==============================
    // 전체 발주금액 및 품목 개수 계산
    // ==============================
    function calculateOrderSummary() {

        const rows = orderRequestItemBody.querySelectorAll('tr');

        let totalPrice = 0;
        let selectedCount = 0;

        rows.forEach(function (tr) {

            const ingredientSelect = tr.querySelector('.ingredient-select');
            const quantityInput = tr.querySelector('.quantity-input');

            if (!ingredientSelect || !ingredientSelect.value) {
                return;
            }

            selectedCount++;

            if (!isQuantityValid(tr)) {
                return;
            }

            const selectedOption =
                ingredientSelect.options[ingredientSelect.selectedIndex];

            const supplyPrice = Number(selectedOption.dataset.supplyPrice || 0);
            const quantity = Number(quantityInput.value || 0);

            totalPrice += supplyPrice * quantity;
        });

        if (selectedIngredientCount) {
            selectedIngredientCount.textContent = selectedCount;
        }

        if (orderTotalPrice) {
            orderTotalPrice.textContent = totalPrice.toLocaleString('ko-KR');
        }
    }

    // ==============================
    // 순번 재정렬
    // ==============================
    function updateRowNumbers() {

        const rows = orderRequestItemBody.querySelectorAll('tr');

        rows.forEach(function (tr, index) {

            const numberTd = tr.querySelector('.item-number');

            numberTd.textContent = index + 1;
        });
    }

    // ==============================
    // 품목 추가 버튼
    // ==============================
    addOrderItemBtn.addEventListener('click', function () {

        if (!ingredientLoaded || ingredientList.length === 0) {

            alert('식재료 목록을 먼저 불러와야 합니다.');

            return;
        }

        const newRow = createOrderItemRow();

        orderRequestItemBody.appendChild(newRow);

        updateRowNumbers();
        calculateOrderSummary();
    });

    // ==============================
    // 발주 요청 화면 초기화
    // ==============================
    async function initializeOrderRequest() {

        console.log('[직영점 발주요청] 초기화 시작');

        orderRequestItemBody.innerHTML = '';

        if (!ingredientLoaded) {
            await fetchIngredientList();
        }

        if (ingredientLoaded && ingredientList.length > 0) {

            const firstRow = createOrderItemRow();

            orderRequestItemBody.appendChild(firstRow);

            console.log('[직영점 발주요청] 첫 번째 품목 행 생성 완료');

        } else {

            console.warn('[직영점 발주요청] 식재료 목록이 없습니다.');
        }

        updateRowNumbers();
        calculateOrderSummary();
    }

    // ==============================
    // 발주 데이터 생성
    // ==============================
    function createOrderRequestData() {

        const rows = orderRequestItemBody.querySelectorAll('tr');

        const items = [];
        const selectedIngredientIds = new Set();

        for (const tr of rows) {

            const ingredientSelect = tr.querySelector('.ingredient-select');
            const quantityInput = tr.querySelector('.quantity-input');

            const ingredientId = Number(ingredientSelect.value);

            const orderQuantity = Number(quantityInput.value);

            const selectedOption =
                ingredientSelect.options[ingredientSelect.selectedIndex];

            const supplyPrice = Number(selectedOption.dataset.supplyPrice || 0);

            if (!ingredientId || !isQuantityValid(tr)) {
                throw new Error('식재료와 주문 수량을 확인해주세요.');
            }

            // 중복 식재료 선택 방지
            if (selectedIngredientIds.has(ingredientId)) {
                throw new Error('동일한 식재료를 중복 선택할 수 없습니다.');
            }

            selectedIngredientIds.add(ingredientId);

            items.push({
                ingredientId: ingredientId,
                orderQuantity: orderQuantity,
                supplyPrice: supplyPrice,
                orderPrice: supplyPrice * orderQuantity
            });
        }

        if (items.length === 0) {
            throw new Error('발주 품목을 추가해주세요.');
        }

        return {
            storeId: STORE_ID,
            items: items
        };
    }

    // ==============================
    // 발주 등록 API
    // ==============================
    async function registerOrder() {

        if (!submitOrderBtn) {
            console.error('[직영점 발주요청] 등록 버튼을 찾을 수 없습니다.');
            return;
        }

        try {

            // 중복 클릭 방지
            submitOrderBtn.disabled = true;

            const requestData = createOrderRequestData();

            console.log('[직영점 발주요청] 등록 요청 데이터:', requestData);

            const response = await fetch('/api/v1/store/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            });

            const result = await response.json();

            console.log('[직영점 발주요청] 등록 응답:', result);

            if (!response.ok) {
                throw new Error(
                    result.message || `발주 등록 실패 (HTTP ${response.status})`
                );
            }

            alert(
                `발주 등록이 완료되었습니다.\n발주 번호: ${result.orderId}`
            );

            // 모달 닫기
            if (orderRequestModal) {

                const modalInstance =
                    bootstrap.Modal.getInstance(orderRequestModal);

                if (modalInstance) {
                    modalInstance.hide();
                }
            }

            // 발주 목록 갱신
            window.location.reload();

        } catch (error) {

            console.error('[직영점 발주요청] 등록 실패:', error);

            alert(error.message || '발주 등록 중 오류가 발생했습니다.');

        } finally {

            submitOrderBtn.disabled = false;
        }
    }

    // ==============================
    // 발주 요청 버튼 클릭
    // ==============================
    if (submitOrderBtn) {

        submitOrderBtn.addEventListener('click', function () {
            registerOrder();
        });
    }

    // ==============================
    // 폼 기본 제출 방지
    // ==============================
    if (orderRequestForm) {

        orderRequestForm.addEventListener('submit', function (event) {

            event.preventDefault();

            registerOrder();
        });
    }

    // ==============================
    // 모달 또는 일반 페이지 대응
    // ==============================
    if (orderRequestModal) {

        orderRequestModal.addEventListener('show.bs.modal', async function () {

            console.log('[직영점 발주요청] 모달 열기');

            await initializeOrderRequest();
        });

    } else {

        initializeOrderRequest();
    }

    console.log('[직영점 발주요청] JS 초기화 완료');

});
