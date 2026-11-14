    (function () {
    var modal = document.getElementById('storeInventoryDetailModal');
    var title = document.getElementById('storeInventoryDetailModalLabel');
    var body = document.getElementById('storeInventoryDetailModalBody');
    var backdrop = document.getElementById('storeInventoryDetailBackdrop');

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

    function loadDetail(url) {
    body.innerHTML = '<div class="text-center text-secondary py-5">불러오는 중...</div>';
    openModal();
    fetch(url)
    .then(function (res) {
    if (!res.ok) throw new Error(res.status);
    return res.text();
})
    .then(function (html) {
    var doc = new DOMParser().parseFromString(html, 'text/html');
    var pageTitle = doc.querySelector('.hq-page-title');
    var sections = doc.querySelectorAll('.hq-content > section');
    if (pageTitle) title.textContent = pageTitle.textContent.trim();
    body.innerHTML = '';
    sections.forEach(function (section) {
    body.appendChild(document.importNode(section, true));
});
})
    .catch(function () {
    body.innerHTML = '<div class="text-center text-danger py-5">상세 정보를 불러오지 못했습니다.</div>';
});
}

    // 모달
    document.addEventListener('click', function (event) {
    var link = event.target.closest('a');
    if (!link || link.textContent.trim() !== '상세 보기') return;
    event.preventDefault();
    loadDetail(link.href);
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
