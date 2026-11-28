document.addEventListener('DOMContentLoaded', function () {

    const searchForm = document.getElementById('hqOrderSearchForm');
    const orderListBody = document.getElementById('hqOrderListBody');
    const pagination = document.getElementById('hqOrderPagination');
    const resetButton = document.getElementById('resetOrderSearch');

    if (!searchForm || !orderListBody || !pagination) {
        console.error('[본사] 발주 목록 HTML 요소를 찾을 수 없습니다.');
        return;
    }

    // ----------------------------------------
    // 1. 검색 폼 제출 이벤트
    // ----------------------------------------
    searchForm.addEventListener('submit', function (event) {

        event.preventDefault();
        fetchOrderList(1);

    });

    // ----------------------------------------
    // 2. 초기화 버튼
    // ----------------------------------------
    if (resetButton) {

        resetButton.addEventListener('click', function () {

            searchForm.reset();
            fetchOrderList(1);

        });

    }

    // ----------------------------------------
    // 3. 발주 목록 AJAX 조회
    // ----------------------------------------
    async function fetchOrderList(page) {

        const formData = new FormData(searchForm);
        const params = new URLSearchParams(formData);

        params.set('page', page);
        params.set('size', 10);

        try {

            const response = await fetch(
                `/api/v1/hq/orders?${params.toString()}`
            );

            if (!response.ok) {
                throw new Error(`HTTP 오류: ${response.status}`);
            }

            const data = await response.json();

            console.log('[본사] 발주 목록 조회 결과:', data);

            renderOrderList(data);
            renderPagination(data);

        } catch (error) {

            console.error('[본사] 발주 목록 조회 실패:', error);

            orderListBody.innerHTML = `
                <tr>
                    <td colspan="10" class="text-center text-danger py-4">
                        발주 목록을 불러오지 못했습니다.
                    </td>
                </tr>
            `;

        }

    }

    // ----------------------------------------
    // 4. 발주 목록 출력
    // ----------------------------------------
    function renderOrderList(data) {

        orderListBody.replaceChildren();

        const orders = data.list || [];

        // 조회 결과가 없는 경우
        if (orders.length === 0) {

            const tr = document.createElement('tr');
            const td = document.createElement('td');

            td.colSpan = 10;
            td.className = 'text-center py-4';
            td.textContent = '조회된 발주 내역이 없습니다.';

            tr.appendChild(td);
            orderListBody.appendChild(tr);

            return;
        }

        orders.forEach((order, index) => {

            const tr = document.createElement('tr');

            // --------------------------------
            // 1. 체크박스
            // --------------------------------
            const checkTd = document.createElement('td');
            checkTd.className = 'text-center';

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'form-check-input order-check';
            checkbox.value = order.orderId;

            checkTd.appendChild(checkbox);
            tr.appendChild(checkTd);

            // --------------------------------
            // 2. 순번
            // --------------------------------
            const rowNumber =
                (data.pageNum - 1) * data.pageSize + index + 1;

            addCell(tr, rowNumber);

            // --------------------------------
            // 3. 발주번호
            // --------------------------------
            addCell(tr, order.orderId);

            // --------------------------------
            // 4. 발주일시
            // --------------------------------
            addCell(tr, formatDate(order.orderAt));

            // --------------------------------
            // 5. 직영점명
            // --------------------------------
            addCell(tr, order.storeName);

            // --------------------------------
            // 6. 발주 요약
            // --------------------------------
            const summaryTd = document.createElement('td');

            const ingredientName = document.createElement('span');
            ingredientName.textContent =
                order.firstIngredientName ?? '-';

            summaryTd.appendChild(ingredientName);

            if (
                order.ingredientCount != null &&
                order.ingredientCount > 1
            ) {

                const countSpan = document.createElement('span');

                countSpan.textContent =
                    ` 외 ${order.ingredientCount - 1}종`;

                summaryTd.appendChild(countSpan);

            }

            tr.appendChild(summaryTd);

            // --------------------------------
            // 7. 총금액
            // --------------------------------
            addCell(tr, formatPrice(order.totalPrice));

            // --------------------------------
            // 8. 발주 전체 상태
            // --------------------------------
            const statusTd = document.createElement('td');
            const statusSpan = document.createElement('span');

            const status = order.orderStatus ?? '-';

            statusSpan.className = 'status';

            switch (status) {

                case '대기':
                    statusSpan.classList.add('status-pending');
                    break;

                case '승인':
                    statusSpan.classList.add('status-approved');
                    break;

                case '거절':
                    statusSpan.classList.add('status-rejected');
                    break;

                case '부분':
                    statusSpan.classList.add('status-partial');
                    break;

                default:
                    statusSpan.classList.add('status-default');

            }

            statusSpan.textContent = status;

            statusTd.appendChild(statusSpan);
            tr.appendChild(statusTd);

            // --------------------------------
            // 9. 입고일시
            // --------------------------------
            addCell(tr, formatDate(order.storeAt));

            // --------------------------------
            // 10. 상세 버튼
            // --------------------------------
            const detailTd = document.createElement('td');

            const detailButton = document.createElement('button');

            detailButton.type = 'button';
            detailButton.className =
                'btn btn-sm btn-outline-primary';

            detailButton.textContent = '상세';

            detailButton.setAttribute('data-bs-toggle', 'modal');
            detailButton.setAttribute(
                'data-bs-target',
                '#hqOrderDetailModal'
            );

            detailButton.setAttribute(
                'data-order-id',
                order.orderId
            );

            detailTd.appendChild(detailButton);
            tr.appendChild(detailTd);

            orderListBody.appendChild(tr);

        });

    }

    // ----------------------------------------
    // 5. 페이지네이션 출력
    // ----------------------------------------
    function renderPagination(data) {

        pagination.replaceChildren();

        // 페이지가 1개 이하라면 표시하지 않음
        if (!data.pages || data.pages <= 1) {
            return;
        }

        // 이전 버튼
        if (data.hasPreviousPage) {
            createPageButton('이전', data.prePage);
        }

        // 페이지 번호
        (data.navigatepageNums || []).forEach(page => {

            const button = createPageButton(page, page);

            if (page === data.pageNum) {
                button.classList.add('active');
                button.disabled = true;
            }

        });

        // 다음 버튼
        if (data.hasNextPage) {
            createPageButton('다음', data.nextPage);
        }

    }

    // ----------------------------------------
    // 6. 페이지 버튼 생성
    // ----------------------------------------
    function createPageButton(label, page) {

        const button = document.createElement('button');

        button.type = 'button';
        button.className = 'btn btn-sm btn-outline-primary mx-1';
        button.textContent = label;

        button.addEventListener('click', function () {
            fetchOrderList(page);
        });

        pagination.appendChild(button);

        return button;
    }

    // ----------------------------------------
    // 7. 공통 셀 생성
    // ----------------------------------------
    function addCell(tr, value) {

        const td = document.createElement('td');

        td.textContent = value ?? '-';

        tr.appendChild(td);

    }

    // ----------------------------------------
    // 8. 날짜 포맷
    // ----------------------------------------
    function formatDate(value) {

        if (!value) return '-';

        const date = new Date(value);

        if (isNaN(date.getTime())) return '-';

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
    // 9. 금액 포맷
    // ----------------------------------------
    function formatPrice(value) {

        if (value == null) return '-';

        return Number(value).toLocaleString('ko-KR') + '원';

    }

    // ----------------------------------------
// 10. 상태별 건수 AJAX 조회
// ----------------------------------------
    async function fetchOrderStatusCount() {

        try {

            const response = await fetch('/api/v1/hq/orders/status');

            if (!response.ok) {
                throw new Error(`HTTP 오류: ${response.status}`);
            }

            const data = await response.json();

            console.log('[본사] 상태별 건수 조회 결과:', data);

            renderOrderStatusCount(data);

        } catch (error) {

            console.error('[본사] 상태별 건수 조회 실패:', error);

        }

    }

// ----------------------------------------
// 11. 상태별 건수 렌더링
// ----------------------------------------
    function renderOrderStatusCount(data) {

        const waitingCount = document.getElementById('waitingCount');
        const approvedCount = document.getElementById('approvedCount');
        const rejectedCount = document.getElementById('rejectedCount');

        if (!waitingCount || !approvedCount || !rejectedCount) {
            console.error('[본사] 상태별 건수 출력 요소를 찾을 수 없습니다.');
            return;
        }

        waitingCount.textContent = `${data.waitingCount ?? 0}건`;
        approvedCount.textContent = `${data.approvedCount ?? 0}건`;
        rejectedCount.textContent = `${data.rejectedCount ?? 0}건`;

    }

// ----------------------------------------
// 12. 최초 목록 및 상태별 건수 조회
// ----------------------------------------
    fetchOrderList(1);
    fetchOrderStatusCount();

// ----------------------------------------
// 13. 외부 JS에서 재조회 가능하도록 노출
// ----------------------------------------
    window.fetchOrderList = fetchOrderList;
    window.fetchOrderStatusCount = fetchOrderStatusCount;
});