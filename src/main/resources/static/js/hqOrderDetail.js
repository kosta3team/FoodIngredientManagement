document.addEventListener('DOMContentLoaded', function () {

    const orderDetailModal = document.getElementById('hqOrderDetailModal');
    const saveOrderBtn = document.getElementById('saveOrderBtn');
    const itemBody = document.getElementById('hqOrderItemBody');
    const selectAllItems = document.getElementById('selectAllItems');

    // 상세 모달 확인
    if (!orderDetailModal) {
        console.error('[본사] 상세 모달을 찾을 수 없습니다.');
        return;
    }

    let currentOrderId = null;

    // ----------------------------------------
    // 1. 발주 상세 모달 열기
    // ----------------------------------------
    orderDetailModal.addEventListener('show.bs.modal', async function (event) {

        const button = event.relatedTarget;

        if (!button) {
            console.error('[본사] 상세 버튼을 찾을 수 없습니다.');
            return;
        }

        const orderId = button.getAttribute('data-order-id');

        if (!orderId) {
            console.error('[본사] 발주번호가 없습니다.');
            return;
        }

        currentOrderId = orderId;

        console.log('[본사] 발주 상세 조회 요청');
        console.log('선택한 발주번호:', orderId);

        resetOrderModal();

        try {

            const response = await fetch(
                `/api/v1/hq/orders/${orderId}`
            );

            if (!response.ok) {
                throw new Error(`HTTP 오류: ${response.status}`);
            }

            const data = await response.json();

            console.log('[본사] 발주 상세 조회 결과:', data);

            // 기본 정보가 있는지 먼저 확인
            if (!data.info) {
                throw new Error('발주 기본 정보가 존재하지 않습니다.');
            }

            // 발주 기본 정보 및 전체 상태 확인
            console.log('[본사] 발주 기본 정보:', data.info);
            console.log('[본사] 발주 전체 상태:', data.info.orderStatus);

            // 발주 기본 정보 출력
            renderOrderInfo(data);

            // 발주 상세 품목 출력
            renderOrderItems(data.items || []);

        } catch (error) {

            console.error('[본사] 발주 상세 조회 실패:', error);

            itemBody.innerHTML = `
                <tr>
                    <td colspan="9" class="text-center text-danger py-4">
                        발주 상세 정보를 불러오지 못했습니다.
                    </td>
                </tr>
            `;
        }
    });


    // ----------------------------------------
    // 2. 처리 결과 저장
    // ----------------------------------------
    if (saveOrderBtn) {

        saveOrderBtn.addEventListener('click', async function () {

            if (!currentOrderId) {
                alert('발주번호를 확인할 수 없습니다.');
                return;
            }

            const rows = itemBody.querySelectorAll('tr');
            const updateItems = [];

            // 변경된 품목 수집
            for (const row of rows) {

                const statusSelect = row.querySelector('.item-decision');
                const reasonInput = row.querySelector('.item-reason');

                if (!statusSelect || !reasonInput) {
                    continue;
                }

                // 이미 처리된 품목은 변경 불가
                if (statusSelect.disabled) {
                    continue;
                }

                const originalStatus = row.dataset.originalStatus;
                const currentStatus = statusSelect.value;

                // 상태가 변경되지 않았다면 제외
                if (originalStatus === currentStatus) {
                    continue;
                }

                // 대기 상태 저장 방지
                if (currentStatus === '대기') {
                    continue;
                }

                // 거절 사유 검증
                if (
                    currentStatus === '거절' &&
                    !reasonInput.value.trim()
                ) {
                    alert('거절 처리된 품목은 거절 사유를 입력해야 합니다.');
                    reasonInput.focus();
                    return;
                }

                updateItems.push({
                    orderDetailId: Number(row.dataset.orderDetailId),
                    orderStatus: currentStatus,
                    rejectReason: currentStatus === '거절'
                        ? reasonInput.value.trim()
                        : null
                });
            }

            // 변경된 품목이 없는 경우
            if (updateItems.length === 0) {
                alert('변경된 처리 결과가 없습니다.');
                return;
            }

            console.log('[본사] 변경된 품목:', updateItems);

            // 중복 저장 방지
            saveOrderBtn.disabled = true;

            try {

                const response = await fetch(
                    `/api/v1/hq/orders/${currentOrderId}/status`,
                    {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(updateItems)
                    }
                );

                if (!response.ok) {
                    const errorMessage = await response.text();
                    throw new Error(errorMessage || '발주 처리 실패');
                }

                const result = await response.text();

                console.log('[본사] 처리 결과 저장 완료');
                console.log('서버 응답:', result);

                console.log(
                    '[본사] 서버로 전송한 데이터:',
                    JSON.stringify({items: updateItems}, null, 2)
                );

                alert(result);

                // 상세 모달 닫기
                const modalInstance =
                    bootstrap.Modal.getInstance(orderDetailModal);

                if (modalInstance) {
                    modalInstance.hide();
                }

                // 본사 발주 목록 재조회
                // 1. 발주 목록 재조회
                if (typeof window.fetchOrderList === 'function') {
                    window.fetchOrderList(1);
                } else {
                    console.error('[본사] 발주 목록 조회 함수를 찾을 수 없습니다.');
                }

                // 2. 상태별 건수 재조회
                if (typeof window.fetchOrderStatusCount === 'function') {
                    window.fetchOrderStatusCount();
                } else {
                    console.error('[본사] 상태별 건수 조회 함수를 찾을 수 없습니다.');
                }


            } catch (error) {

                console.error('[본사] 발주 처리 오류:', error);
                alert(error.message);

            } finally {

                saveOrderBtn.disabled = false;

            }
        });

    } else {

        console.error('[본사] 처리 결과 저장 버튼을 찾을 수 없습니다.');

    }


    // ----------------------------------------
    // 3. 전체 품목 선택
    // ----------------------------------------
    if (selectAllItems) {

        selectAllItems.addEventListener('change', function () {

            const checked = selectAllItems.checked;

            itemBody.querySelectorAll('.item-check').forEach(checkbox => {
                checkbox.checked = checked;
            });

        });

    }

    // 개별 품목 선택 시 전체 선택 상태 변경
    if (itemBody && selectAllItems) {

        itemBody.addEventListener('change', function (event) {

            if (!event.target.classList.contains('item-check')) {
                return;
            }

            const checkboxes = itemBody.querySelectorAll('.item-check');

            const checkedCount = Array.from(checkboxes)
                .filter(checkbox => checkbox.checked).length;

            selectAllItems.checked =
                checkboxes.length > 0 &&
                checkedCount === checkboxes.length;

            selectAllItems.indeterminate =
                checkedCount > 0 &&
                checkedCount < checkboxes.length;

        });

    }


    // ----------------------------------------
    // 4. 날짜 포맷
    // ----------------------------------------
    function formatDate(dateValue) {

        if (!dateValue) {
            return '-';
        }

        const date = new Date(dateValue);

        if (isNaN(date.getTime())) {
            return '-';
        }

        return date.toLocaleString('ko-KR', {
            timeZone: 'Asia/Seoul',
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
        });
    }


    // ----------------------------------------
    // 5. 발주 기본 정보 출력
    // ----------------------------------------
    function renderOrderInfo(data) {

        const info = data.info;

        document.getElementById('modalOrderId').textContent =
            info.orderId ?? '-';

        document.getElementById('modalOrderAt').textContent =
            formatDate(info.orderAt);

        document.getElementById('modalStoreName').textContent =
            info.storeName ?? '-';

        document.getElementById('modalOrderStatus').textContent =
            info.orderStatus ?? '-';

        document.getElementById('modalAppAt').textContent =
            formatDate(info.appAt);

        document.getElementById('modalStoreAt').textContent =
            formatDate(info.storeAt);

        document.getElementById('modalTotalPrice').textContent =
            info.totalPrice != null
                ? Number(info.totalPrice).toLocaleString('ko-KR') + '원'
                : '-';

        document.getElementById('modalManagerName').textContent =
            info.userName ?? '-';
    }


    // ----------------------------------------
    // 6. 발주 상세 품목 출력
    // ----------------------------------------
    function renderOrderItems(items) {

        itemBody.replaceChildren();

        if (!items.length) {

            const tr = document.createElement('tr');
            const td = document.createElement('td');

            td.colSpan = 9;
            td.className = 'text-center text-secondary py-4';
            td.textContent = '등록된 발주 품목이 없습니다.';

            tr.appendChild(td);
            itemBody.appendChild(tr);

            return;
        }

        items.forEach((item, index) => {

            const tr = document.createElement('tr');

            console.log('[본사] 개별 품목 데이터:', item);
            console.log('[본사] 품목 번호:', item.orderDetailId);
            console.log('[본사] 품목 상태:', item.orderStatus);

            // 발주 상세 품목 번호와 기존 상태 저장
            tr.dataset.orderDetailId = item.orderDetailId;
            tr.dataset.originalStatus = item.orderStatus;

            // 체크박스
            const checkTd = document.createElement('td');
            const checkbox = document.createElement('input');

            checkbox.type = 'checkbox';
            checkbox.className = 'form-check-input item-check';

            checkTd.appendChild(checkbox);
            tr.appendChild(checkTd);

            // 순번
            addTextCell(tr, index + 1);

            // 식재료명
            addTextCell(tr, item.ingredientName);

            // 요청수량
            addTextCell(tr, item.orderQuantity);

            // 입고단위
            addTextCell(tr, item.orderUnit);

            // 단가
            addPriceCell(tr, item.supplyPrice);

            // 요청금액
            addPriceCell(tr, item.orderPrice);

            // 변경 상태
            const statusTd = document.createElement('td');
            const select = document.createElement('select');

            select.className = 'form-select form-select-sm item-decision';

            const statusOptions = [
                {value: '대기', text: '대기'},
                {value: '승인', text: '승인'},
                {value: '거절', text: '거절'}
            ];

            statusOptions.forEach(option => {

                const optionElement = document.createElement('option');

                optionElement.value = option.value;
                optionElement.textContent = option.text;

                if (item.orderStatus === option.value) {
                    optionElement.selected = true;
                }

                select.appendChild(optionElement);

            });

            // 대기 상태인 품목만 변경 가능
            select.disabled = item.orderStatus !== '대기';

            statusTd.appendChild(select);
            tr.appendChild(statusTd);

            // 거절 사유
            const reasonTd = document.createElement('td');
            const reasonInput = document.createElement('input');

            reasonInput.type = 'text';
            reasonInput.maxLength = 200;
            reasonInput.className = 'form-control form-control-sm item-reason';
            reasonInput.placeholder = '사유';
            reasonInput.value = item.rejectReason ?? '';

            reasonInput.disabled = select.value !== '거절';

            // 상태 변경에 따른 사유 입력 활성화
            select.addEventListener('change', function () {

                if (select.value === '거절') {

                    reasonInput.disabled = false;
                    reasonInput.placeholder = '거절 사유 필수 입력';

                } else {

                    reasonInput.disabled = true;
                    reasonInput.value = '';
                    reasonInput.placeholder = '사유';

                }

            });

            reasonTd.appendChild(reasonInput);
            tr.appendChild(reasonTd);

            itemBody.appendChild(tr);

        });

        // 전체 선택 초기화
        if (selectAllItems) {
            selectAllItems.checked = false;
            selectAllItems.indeterminate = false;
        }
    }


    // ----------------------------------------
    // 7. 일반 텍스트 셀 생성
    // ----------------------------------------
    function addTextCell(tr, value) {

        const td = document.createElement('td');

        td.textContent = value ?? '-';

        tr.appendChild(td);
    }


    // ----------------------------------------
    // 8. 금액 셀 생성
    // ----------------------------------------
    function addPriceCell(tr, value) {

        const td = document.createElement('td');

        td.textContent = value != null
            ? Number(value).toLocaleString('ko-KR') + '원'
            : '-';

        tr.appendChild(td);
    }


    // ----------------------------------------
    // 9. 모달 초기화
    // ----------------------------------------
    function resetOrderModal() {

        const ids = [
            'modalOrderId',
            'modalOrderAt',
            'modalStoreName',
            'modalOrderStatus',
            'modalAppAt',
            'modalStoreAt',
            'modalTotalPrice',
            'modalManagerName'
        ];

        ids.forEach(id => {

            const element = document.getElementById(id);

            if (element) {
                element.textContent = '-';
            }

        });

        itemBody.innerHTML = `
            <tr>
                <td colspan="9" class="text-center text-secondary py-4">
                    발주 상세 정보를 불러오는 중입니다.
                </td>
            </tr>
        `;

        if (selectAllItems) {
            selectAllItems.checked = false;
            selectAllItems.indeterminate = false;
        }
    }

});
