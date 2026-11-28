
document.addEventListener('DOMContentLoaded', function () {

    const detailModal = document.getElementById('storeOrderDetailModal');
    const saveOrderDatesBtn = document.getElementById('saveOrderDatesBtn');

    if (!detailModal) {
        console.error('[직영점] 발주 상세 모달을 찾을 수 없습니다.');
        return;
    }

    // ----------------------------------------
    // 1. 상세 모달이 열릴 때 발주번호 가져오기
    // ----------------------------------------
    detailModal.addEventListener('show.bs.modal', function (event) {

        const detailButton = event.relatedTarget;

        if (!detailButton) {
            console.error('[직영점] 상세 버튼 정보를 가져올 수 없습니다.');
            return;
        }

        const orderId = detailButton.getAttribute('data-order-id');

        if (!orderId) {
            console.error('[직영점] 발주번호가 없습니다.');
            return;
        }

        console.log('[직영점] 클릭한 발주번호:', orderId);

        resetOrderDetail();
        fetchOrderDetail(orderId);
    });

    // ----------------------------------------
    // 2. 발주 상세 내용 초기화
    // ----------------------------------------
    function resetOrderDetail() {

        const ids = [
            'detailOrderId',
            'detailOrderAt',
            'detailTotalPrice',
            'detailOrderStatus',
            'detailAppAt',
            'detailStoreAt'
        ];

        ids.forEach(id => {
            const element = document.getElementById(id);

            if (element) {
                element.textContent = '-';
            }
        });

        const statusElement = document.getElementById('detailOrderStatus');

        if (statusElement) {
            statusElement.className = 'status status-default';
        }

        const ingredientList =
            document.getElementById('storeOrderDetailItemsBody');

        if (ingredientList) {
            ingredientList.innerHTML = `
                <tr>
                    <td colspan="10" class="text-center py-4">
                        품목 정보를 조회하고 있습니다.
                    </td>
                </tr>
            `;
        }
    }

    // ----------------------------------------
    // 3. 발주 기본정보 조회
    // ----------------------------------------
    async function fetchOrderDetail(orderId) {

        try {

            const response = await fetch(
                '/api/v1/store/orders/progress'
            );

            if (!response.ok) {
                throw new Error(`HTTP 오류: ${response.status}`);
            }

            const data = await response.json();

            console.log('[직영점] 해당 매장 발주 목록:', data);

            console.log(
                '[직영점] API 응답 발주번호:',
                data.map(item => item.orderId)
            );

            const order = data.find(
                item => String(item.orderId) === String(orderId)
            );

            if (!order) {
                console.error(
                    '[직영점] 해당 발주 정보를 찾을 수 없습니다:',
                    orderId
                );

                showDetailError('해당 발주 정보를 찾을 수 없습니다.');
                return;
            }

            console.log('[직영점] 선택한 발주 상세 정보:', order);

            renderOrderDetail(order);

            await fetchOrderItems(orderId);

        } catch (error) {

            console.error('[직영점] 발주 상세 조회 실패:', error);
            showDetailError('발주 상세 정보를 불러오지 못했습니다.');

        }
    }

    // ----------------------------------------
    // 4. 발주 품목 상세 조회
    // ----------------------------------------
    async function fetchOrderItems(orderId) {

        const ingredientList =
            document.getElementById('storeOrderDetailItemsBody');

        try {

            console.log('[직영점] 발주 품목 조회 시작');
            console.log('[직영점] 발주번호:', orderId);

            if (!ingredientList) {
                console.error(
                    '[직영점] 품목 테이블 tbody를 찾을 수 없습니다.'
                );
                return;
            }

            ingredientList.innerHTML = `
                <tr>
                    <td colspan="10" class="text-center py-4">
                        품목 정보를 조회하고 있습니다.
                    </td>
                </tr>
            `;

            const response = await fetch(
                `/api/v1/store/orders/${orderId}/items?storeId=${storeId}`
            );

            if (!response.ok) {
                throw new Error(`HTTP 오류: ${response.status}`);
            }

            const items = await response.json();

            console.log('[직영점] 발주 품목 조회 결과:', items);
            console.log('[직영점] 조회된 품목 수:', items.length);

            renderOrderItems(items);

        } catch (error) {

            console.error('[직영점] 발주 품목 조회 실패:', error);

            if (ingredientList) {
                ingredientList.innerHTML = `
                    <tr>
                        <td colspan="10" class="text-center text-danger py-4">
                            품목 정보를 불러오지 못했습니다.
                        </td>
                    </tr>
                `;
            }
        }
    }

    // ----------------------------------------
    // 5. 발주 기본정보 출력
    // ----------------------------------------
    function renderOrderDetail(data) {

        console.log('[상세 조회] 발주번호:', data.orderId);
        console.log('[상세 조회] 서버 상태값:', data.orderStatus);

        document.getElementById('detailOrderId').textContent =
            data.orderId ?? '-';

        document.getElementById('detailOrderAt').textContent =
            formatDate(data.orderAt);

        document.getElementById('detailTotalPrice').textContent =
            formatPrice(data.totalPrice);

        const statusElement =
            document.getElementById('detailOrderStatus');

        const status = data.orderStatus ?? '-';

        statusElement.textContent = status;
        statusElement.className = `status ${getStatusClass(status)}`;

        document.getElementById('detailAppAt').textContent =
            formatDate(data.appAt);

        document.getElementById('detailStoreAt').textContent =
            formatDate(data.storeAt);
    }

    // ----------------------------------------
    // 6. 발주 품목 내역 출력
    // ----------------------------------------
    function renderOrderItems(items) {

        const ingredientList =
            document.getElementById('storeOrderDetailItemsBody');

        if (!ingredientList) {
            console.error('[직영점] 품목 테이블 tbody를 찾을 수 없습니다.');
            return;
        }

        ingredientList.innerHTML = '';

        if (!items || items.length === 0) {

            ingredientList.innerHTML = `
                <tr>
                    <td colspan="10" class="text-center py-4">
                        조회된 품목이 없습니다.
                    </td>
                </tr>
            `;

            return;
        }

        items.forEach((item, index) => {

            const tr = document.createElement('tr');

            tr.dataset.orderDetailId = item.orderDetailId;

            const isApproved = item.orderStatus === '승인';

            // 1. 순번
            addTextCell(tr, index + 1);

            // 2. 식재료명
            addTextCell(tr, item.ingredientName);

            // 3. 수량
            addTextCell(tr, item.orderQuantity);

            // 4. 입고단위
            addTextCell(tr, item.orderUnit);

            // 5. 공급가액
            addTextCell(tr, formatPrice(item.supplyPrice));

            // 6. 주문금액
            addTextCell(tr, formatPrice(item.orderPrice));

            // 7. 처리자
            addTextCell(tr, item.userName);

            // 8. 상태
            const statusCell = document.createElement('td');
            const statusBadge = document.createElement('span');

            const status = item.orderStatus ?? '-';

            statusBadge.className = `status ${getStatusClass(status)}`;
            statusBadge.textContent = status;

            statusCell.appendChild(statusBadge);
            tr.appendChild(statusCell);

            // 9. 제조일자
            addDateCell(
                tr,
                item.manufactureAt,
                'manufactureAt',
                item.orderDetailId,
                isApproved
            );

            // 10. 입고일자
            addDateCell(
                tr,
                item.storeAt,
                'storeAt',
                item.orderDetailId,
                isApproved
            );

            ingredientList.appendChild(tr);
        });
    }

    // ----------------------------------------
    // 7. 테이블 셀 생성
    // ----------------------------------------
    function addTextCell(tr, value) {

        const td = document.createElement('td');

        td.textContent = value ?? '-';

        tr.appendChild(td);
    }

    // ----------------------------------------
    // 8. 날짜 및 시간 입력 셀 생성
    // ----------------------------------------
    function addDateCell(tr, dateValue, fieldName, orderDetailId, enabled) {

        const td = document.createElement('td');
        const input = document.createElement('input');

        // 날짜 + 시간 입력
        input.type = 'datetime-local';
        input.className = 'form-control form-control-sm';

        input.dataset.field = fieldName;
        input.dataset.orderDetailId = orderDetailId;

        // 기존 DB 날짜 표시
        input.value = dateValue
            ? String(dateValue).substring(0, 16)
            : '';

        // 승인된 품목만 입력 가능
        input.disabled = !enabled;

        td.appendChild(input);
        tr.appendChild(td);
    }

    // ----------------------------------------
    // 9. 제조일자 및 입고일자 저장
    // ----------------------------------------
    if (saveOrderDatesBtn) {

        saveOrderDatesBtn.addEventListener('click', async function () {

            const inputs = detailModal.querySelectorAll(
                'input[data-order-detail-id]'
            );

            const orderDetails = new Map();

            // 품목별 제조일자 및 입고일자 수집
            inputs.forEach(input => {

                if (input.disabled) return;

                const orderDetailId = input.dataset.orderDetailId;

                if (!orderDetails.has(orderDetailId)) {
                    orderDetails.set(orderDetailId, {
                        orderDetailId: Number(orderDetailId)
                    });
                }

                const detail = orderDetails.get(orderDetailId);

                detail[input.dataset.field] = input.value
                    ? input.value + ':00'
                    : null;
            });

            if (orderDetails.size === 0) {
                alert('수정할 수 있는 승인 품목이 없습니다.');
                return;
            }

            // 저장 버튼 중복 클릭 방지
            saveOrderDatesBtn.disabled = true;

            try {

                console.log(
                    '[직영점] 날짜 수정 요청 데이터:',
                    Array.from(orderDetails.values())
                );

                const results = await Promise.all(
                    Array.from(orderDetails.values()).map(async detail => {

                        const response = await fetch(
                            '/api/v1/store/orders/dates',
                            {
                                method: 'PUT',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify(detail)
                            }
                        );

                        const result = await response.json();

                        if (!response.ok || !result.success) {
                            throw new Error(
                                result.message || '날짜 수정에 실패했습니다.'
                            );
                        }

                        return result;
                    })
                );

                console.log('[직영점] 날짜 수정 결과:', results);

                alert('제조일자 및 입고일자가 저장되었습니다.');

            } catch (error) {

                console.error('[직영점] 날짜 저장 실패:', error);

                alert(error.message || '날짜 저장 중 오류가 발생했습니다.');

            } finally {

                saveOrderDatesBtn.disabled = false;

            }
        });
    }

    // ----------------------------------------
    // 10. 상태별 CSS 클래스
    // ----------------------------------------
    function getStatusClass(status) {

        switch (status) {

            case '대기':
                return 'status-pending';

            case '승인':
                return 'status-approved';

            case '거절':
                return 'status-rejected';

            case '완료':
                return 'status-completed';

            case '부분':
                return 'status-partial';

            default:
                return 'status-default';
        }
    }

    // ----------------------------------------
    // 11. 날짜 포맷
    // ----------------------------------------
    function formatDate(date) {

        if (!date) {
            return '-';
        }

        const parsedDate = new Date(date);

        if (isNaN(parsedDate.getTime())) {
            return '-';
        }

        return parsedDate.toLocaleString('ko-KR', {
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
    // 12. 금액 포맷
    // ----------------------------------------
    function formatPrice(price) {

        if (price == null) {
            return '-';
        }

        return Number(price).toLocaleString('ko-KR') + '원';
    }

    // ----------------------------------------
    // 13. 상세 조회 오류 출력
    // ----------------------------------------
    function showDetailError(message) {

        const ingredientList =
            document.getElementById('storeOrderDetailItemsBody');

        if (ingredientList) {

            ingredientList.innerHTML = `
                <tr>
                    <td colspan="10" class="text-center text-danger py-4">
                        ${message}
                    </td>
                </tr>
            `;
        }
    }

});
