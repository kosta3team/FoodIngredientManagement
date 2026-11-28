
document.addEventListener('DOMContentLoaded', function () {

    const searchForm = document.getElementById('storeOrderSearchForm');
    const orderListBody = document.getElementById('storeOrderListBody');
    const pagination = document.getElementById('storeOrderPagination');
    const resetButton = document.getElementById('resetStoreOrderSearch');

    if (!searchForm || !orderListBody || !pagination) {
        console.error('[직영점] 발주 목록 HTML 요소를 찾을 수 없습니다.');
        return;
    }

    // ----------------------------------------
    // 1. 검색 이벤트
    // ----------------------------------------
    searchForm.addEventListener('submit', function (event) {

        event.preventDefault();
        fetchOrderList(1);

    });

    // ----------------------------------------
    // 2. 검색 초기화
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

            console.log('[직영점] 발주 목록 조회 시작');
            console.log('[직영점] 요청 페이지:', page);

            const response = await fetch(
                `/api/v1/store/orders?${params.toString()}`
            );

            if (!response.ok) {
                throw new Error(`HTTP 오류: ${response.status}`);
            }

            const data = await response.json();

            console.log('[직영점] 발주 목록 조회 결과:', data);

            renderOrderList(data);
            renderPagination(data);

        } catch (error) {

            console.error('[직영점] 발주 목록 조회 실패:', error);

            orderListBody.innerHTML = `
                <tr>
                    <td colspan="8" class="text-center text-danger py-4">
                        발주 목록을 불러오지 못했습니다.
                    </td>
                </tr>
            `;

            pagination.replaceChildren();

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

            td.colSpan = 8;
            td.className = 'text-center py-4';
            td.textContent = '조회된 발주 내역이 없습니다.';

            tr.appendChild(td);
            orderListBody.appendChild(tr);

            return;
        }

        orders.forEach((order, index) => {

            const tr = document.createElement('tr');

            // --------------------------------
            // 1. 순번
            // --------------------------------
            const rowNumber =
                (data.pageNum - 1) * data.pageSize + index + 1;

            addCell(tr, rowNumber);

            // --------------------------------
            // 2. 발주번호
            // --------------------------------
            addCell(tr, order.orderId);

            // --------------------------------
            // 3. 발주일시
            // --------------------------------
            addCell(tr, formatDate(order.orderAt));

            // --------------------------------
            // 4. 발주 요약
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
            // 5. 총금액
            // --------------------------------
            addCell(tr, formatPrice(order.totalPrice));

            // --------------------------------
            // 6. 발주 상태
            // --------------------------------
            const statusTd = document.createElement('td');
            const statusSpan = document.createElement('span');

            const status = order.orderStatus ?? '-';

            // 기본 CSS 클래스
            statusSpan.className = 'status';

            // 상태별 CSS 클래스 추가
            statusSpan.classList.add(getStatusClass(status));

            statusSpan.textContent = status;

            statusTd.appendChild(statusSpan);
            tr.appendChild(statusTd);

            // --------------------------------
            // 7. 입고일시
            // --------------------------------
            addCell(tr, formatDate(order.storeAt));

            // --------------------------------
            // 8. 상세 버튼
            // --------------------------------
            const detailTd = document.createElement('td');
            const detailButton = document.createElement('button');

            detailButton.type = 'button';
            detailButton.className =
                'btn btn-outline-primary btn-sm text-nowrap';

            detailButton.textContent = '상세';

            detailButton.setAttribute('data-bs-toggle', 'modal');
            detailButton.setAttribute(
                'data-bs-target',
                '#storeOrderDetailModal'
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
    // 5. 상태별 CSS 클래스
    // ----------------------------------------
    function getStatusClass(status) {

        switch (status) {

            case '승인':
                return 'status-approved';

            case '대기':
                return 'status-pending';

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
    // 6. 페이지네이션 출력
    // ----------------------------------------
    function renderPagination(data) {

        pagination.replaceChildren();

        if (!data.pages || data.pages <= 1) {
            return;
        }

        // 이전
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

        // 다음
        if (data.hasNextPage) {
            createPageButton('다음', data.nextPage);
        }

    }

    // ----------------------------------------
    // 7. 페이지 버튼 생성
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
    // 8. 공통 셀 생성
    // ----------------------------------------
    function addCell(tr, value) {

        const td = document.createElement('td');

        td.textContent = value ?? '-';

        tr.appendChild(td);

    }

    // ----------------------------------------
    // 9. 날짜 포맷
    // ----------------------------------------
    function formatDate(value) {

        if (!value) {
            return '-';
        }

        const date = new Date(value);

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
    // 10. 금액 포맷
    // ----------------------------------------
    function formatPrice(value) {

        if (value == null) {
            return '-';
        }

        return Number(value).toLocaleString('ko-KR') + '원';

    }

    // ----------------------------------------
    // 11. 최초 목록 조회
    // ----------------------------------------
    fetchOrderList(1);

});
