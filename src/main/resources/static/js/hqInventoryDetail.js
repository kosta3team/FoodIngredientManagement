(function () {
    var modal = document.getElementById('hqInventoryDetailModal');
    var title = document.getElementById('hqInventoryDetailModalLabel');
    var body = document.getElementById('hqInventoryDetailModalBody');
    var backdrop = document.getElementById('hqInventoryDetailBackdrop');

    function openModal() {
        modal.classList.add('d-block');
        modal.setAttribute('aria-hidden', 'false');
        backdrop.classList.remove('d-none');
        document.body.classList.add('modal-open');
    }

    function closeModal() {
        modal.classList.remove('d-block');
        modal.setAttribute('aria-hidden', 'true');
        backdrop.classList.add('d-none');
        document.body.classList.remove('modal-open');
    }

    function loadDetail(storeId, ingredientId, orderDetailId) {
        title.textContent = '재고 상세조회';
        body.innerHTML = '<div class="text-center text-secondary py-5">불러오는 중...</div>';
        openModal();

        fetch('/api/inventory/detail?storeId=' + storeId
            + '&ingredientId=' + ingredientId
            + '&orderDetailId=' + orderDetailId)
            .then(function (res) {
                if (!res.ok) throw new Error(res.status);
                return res.json();
            })
            .then(function (d) {
                title.textContent = d.storeName + ' 재고 상세조회';
                body.innerHTML = renderDetail(d);
            })
            .catch(function () {
                body.innerHTML = '<div class="text-center text-danger py-5">상세 정보를 불러오지 못했습니다.</div>';
            });
    }

    function formatDay(value) {
        return value ? value.substring(0, 10) : '-';
    }

    function item(label, value) {
        return '<div class="col-3 text-secondary">' + label + '</div>'
            + '<div class="col-3">' + value + '</div>';
    }


    function renderDetail(d) {
        var method = d.managementMethod ?? '관리';

        var usageRows = d.usageList.length === 0
            ? '<tr><td colspan="4" class="text-center text-secondary py-3">사용 이력이 없습니다.</td></tr>'
            : d.usageList.map(function (u, i) {
                return '<tr>'
                    + '<td>' + (i + 1) + '</td>'
                    + '<td>' + formatDay(u.consumptionAt) + '</td>'
                    + '<td>' + formatDate(u.managementStartAt) + '</td>'
                    + '<td>' + u.usageQuantity + ' ' + u.orderUnit + '</td>'
                    + '</tr>';
            }).join('');

        return ''
            // 기본 정보
            + '<section class="card p-4 mb-3">'
            + '<div class="d-flex justify-content-between align-items-start mb-3">'
            + '<h4 class="fw-bold mb-0">' + d.ingredientName + '</h4>'
            + '<div class="bg-light rounded px-4 py-3">잔여수량'
            + '<span class="fs-4 fw-bold ms-5">' + d.currentQuantity + '</span> ' + d.orderUnit
            + '</div>'
            + '</div>'
            + '<div class="row g-2">'
            + item('입고단위', d.orderUnit)
            + item('용량단위', d.capacity + ' ' + d.capacityUnit)
            + item('보관유형', d.storageType)
            + item('관리방식', d.managementMethod ?? '-')
            + item('유통기한', '제조일자 후 + ' + d.expirationPeriod + '일')
            + item('소비기한', method + ' 후 + ' + d.consumptionPeriod + '일')
            + item('담당자', d.userName ?? '-')
            + item('소비기한 (여름)', method + ' 후 + ' + d.summerConsumptionPeriod + '일')
            + '</div>'
            + '</section>'
            // 입고 정보
            + '<section class="card p-4 mb-3">'
            + '<h6 class="fw-bold mb-3">입고 정보</h6>'
            + '<div class="row g-2">'
            + item('제조일자', formatDay(d.manufactureAt))
            + item('유통기한', formatDay(d.expirationDate))
            + item('입고일자', formatDate(d.storeAt))
            + item('입고수량', d.orderQuantity + ' ' + d.orderUnit)
            + '</div>'
            + '</section>'
            // 사용 이력
            + '<section class="card p-4">'
            + '<h6 class="fw-bold mb-3">사용 이력</h6>'
            + '<table class="table mb-0">'
            + '<thead class="table-light"><tr>'
            + '<th>번호</th><th>소비기한</th><th>관리시작일시</th><th>사용개수</th>'
            + '</tr></thead>'
            + '<tbody>' + usageRows + '</tbody>'
            + '</table>'
            + '</section>';
    }


    // 모달
    document.addEventListener('click', function (event) {
        var btn = event.target.closest('.btn-detail');
        if (!btn) return;
        loadDetail(btn.dataset.storeId, btn.dataset.ingredientId, btn.dataset.orderDetailId);
    });

    //
    modal.querySelectorAll('[data-modal-close]').forEach(function (btn) {
        btn.addEventListener('click', closeModal);
    });
    modal.addEventListener('click', function (event) {
        if (event.target === modal) closeModal();
    });
    document.addEventListener('keydown', function (event) {
        if (event.key === 'Escape') closeModal();
    });
})();
