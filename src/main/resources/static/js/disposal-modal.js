/**
 * 폐기 상세 모달 (화면 예시 데이터용)
 * 현재 목록은 정적 HTML이므로 행 데이터를 모달에 표시하고,
 * 승인/거절 결과는 브라우저 화면에서만 반영합니다 (서버 저장 X).
 * 실제 운영 시 processDisposal()을 승인/거절 API로 교체하세요.
 */
(() => {
  'use strict';
  const modalElement = document.getElementById('disposalDetailModal');
  if (!modalElement) return;
  const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
  const table = document.querySelector('.disposal-table-scroll table');
  if (!table) return;

  const $ = id => document.getElementById(id);
  const put = (id, value) => { $(id).textContent = value == null || value === '' ? '—' : value; };
  const statusCodes = { '요청': 'REQUESTED', '대기': 'REQUESTED', '승인': 'APPROVED', '거절': 'REJECTED' };
  const statusClasses = { REQUESTED: 'status-pending', APPROVED: 'status-approved', REJECTED: 'status-rejected' };
  let activeRow = null;
  let currentStatus = null;

  function showActionButtons() {
    const requested = currentStatus === 'REQUESTED';
    $('detailApproveBtn').hidden = !requested;
    $('detailRejectBtn').hidden = !requested;
    $('rejectionInputArea').hidden = true;
    $('detailRejectReason').value = '';
  }

  table.addEventListener('click', event => {
    const button = event.target.closest('.disposal-detail-btn');
    if (!button || !table.contains(button)) return;
    activeRow = button.closest('tr');
    const cells = activeRow.cells;
    if (cells.length < 11) return;
    const statusText = cells[8].textContent.trim();
    currentStatus = statusCodes[statusText] || 'REQUESTED';

    put('detailDisposalId', cells[0].textContent.trim());
    put('detailReqAt', cells[1].textContent.trim());
    put('detailStore', cells[2].textContent.trim());
    put('detailIngredient', cells[3].textContent.trim());
    put('detailAmount', cells[4].textContent.trim());
    put('detailType', cells[6].textContent.trim());
    put('detailStatus', statusText);
    put('detailResult', statusText);
    const processed = cells[9].textContent.trim();
    put('detailDisposedAt', processed);
    $('detailProcessedAt').value = /^\d{4}-\d{2}-\d{2}/.test(processed) ? processed.replace(' ', 'T').slice(0,16) : '';

    // 현재 목록 HTML에는 아래 상세 정보가 없으므로 임의 값을 넣지 않습니다.
    put('detailRequestUser', activeRow.dataset.requestUser);
    put('detailInboundId', activeRow.dataset.inboundId);
    put('detailStartDate', activeRow.dataset.startDate);
    put('detailExpiryDate', activeRow.dataset.expiryDate);
    put('detailProcessor', activeRow.dataset.processor);
    $('detailReason').value = activeRow.dataset.reason || '';
    showActionButtons();
    modal.show();
  });

  $('detailApproveBtn').addEventListener('click', () => processDisposal('APPROVED'));
  $('detailRejectBtn').addEventListener('click', () => {
    if ($('rejectionInputArea').hidden) {
      $('rejectionInputArea').hidden = false;
      $('detailRejectReason').focus();
      return;
    }
    const reason = $('detailRejectReason').value.trim();
    if (!reason) {
      alert('거절 사유를 입력하세요.');
      $('detailRejectReason').focus();
      return;
    }
    processDisposal('REJECTED', reason);
  });

  function processDisposal(nextStatus, rejectReason = '') {
    if (!activeRow || currentStatus !== 'REQUESTED') return;
    const label = nextStatus === 'APPROVED' ? '승인' : '거절';
    if (!confirm(`해당 폐기 요청을 ${label}하시겠습니까?`)) return;

    // TODO: 백엔드 API 연동 후 성공 응답을 받았을 때만 아래 UI를 갱신하세요.
    // 예: POST /api/disposals/{id}/approve 또는 /reject
    const cells = activeRow.cells;
    const badge = cells[8].querySelector('.status');
    badge.textContent = label;
    badge.className = `status ${statusClasses[nextStatus]}`;
    const now = new Date();
    const pad = n => String(n).padStart(2, '0');
    const timestamp = `${now.getFullYear()}-${pad(now.getMonth()+1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}`;
    cells[9].textContent = timestamp;
    cells[9].className = 'nowrap';
    if (rejectReason) activeRow.dataset.reason = rejectReason;
    currentStatus = nextStatus;
    put('detailStatus', label);
    put('detailResult', label);
    put('detailDisposedAt', timestamp);
    $('detailProcessedAt').value = timestamp.replace(' ', 'T');
    if (rejectReason) $('detailReason').value = rejectReason;
    showActionButtons();
    modal.hide();
    alert(`화면 예시에서 ${label} 처리했습니다. 서버에는 저장되지 않았습니다.`);
  }
})();
