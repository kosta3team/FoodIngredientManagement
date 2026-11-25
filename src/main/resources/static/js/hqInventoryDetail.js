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



    function renderDetail(d) {
        var usageRows = d.usageList.length === 0
            ? '<tr><td colspan="5" class="text-center text-secondary py-3">사용 이력이 없습니다.</td></tr>'
            : d.usageList.map(function (u) {
                return '<tr>'
                    + '<td>' + formatDate(u.storeAt) + '</td>'
                    + '<td>' + formatDate(u.managementStartAt) + '</td>'
                    + '<td>' + formatDate(u.consumptionAt) + '</td>'
                    + '<td>' + formatDate(u.expirationAt) + '</td>'
                    + '<td>' + u.usageQuantity + ' ' + u.orderUnit + '</td>'
                    + '</tr>';
            }).join('');

        return ''
            + '<section class="mb-4"><h6 class="fw-bold mb-3">기본 정보</h6>'
            + '<table class="table table-bordered mb-0"><tbody>'
            + '<tr><th>직영점명</th><td>' + d.storeName + '</td><th>식재료명</th><td>' + d.ingredientName + '</td></tr>'
            + '<tr><th>보관유형</th><td>' + d.storageType + '</td><th>관리방법</th><td>' + (d.managementMethod ?? '-') + '</td></tr>'
            + '<tr><th>현재 재고</th><td>' + d.currentQuantity + ' ' + d.orderUnit + '</td><th>용량</th><td>' + d.capacity + ' ' + d.capacityUnit + '</td></tr>'
            + '<tr><th>유통기한</th><td>' + d.expirationPeriod + '일</td><th>소비기한 (하절기)</th><td>' + d.consumptionPeriod + '일 (' + d.summerConsumptionPeriod + '일)</td></tr>'
            + '</tbody></table></section>'
            + '<section><h6 class="fw-bold mb-3">사용 이력</h6>'
            + '<table class="table table-hover mb-0"><thead><tr>'
            + '<th>입고일시</th><th>관리시작</th><th>소비일시</th><th>유통기한</th><th>사용수량</th>'
            + '</tr></thead><tbody>' + usageRows + '</tbody></table></section>';
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
