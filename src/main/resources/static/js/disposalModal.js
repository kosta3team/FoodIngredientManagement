/**
 * ==================== 전역 변수 ====================
 */

/**
 * 현재 조회 중인 폐기 상세 정보
 *
 * disposalId     : 폐기 일련번호
 * disposalStatus : 폐기 상태
 * role           : 현재 로그인한 사용자 권한
 * checkAt        : 폐기 확인 일시
 */
let currentDisposal = null;


/**
 * 상세 조회 요청 순서 관리
 *
 * 여러 상세 버튼을 빠르게 클릭했을 때
 * 이전 응답이 최신 응답을 덮어쓰는 문제 방지
 */
let disposalRequestSequence = 0;


/**
 * 폐기 증빙 이미지 미리보기 URL
 *
 * 이미지 제거 및 모달 종료 시 메모리 해제에 사용
 */
let disposalImagePreviewUrl = null;


/**
 * ==================== 상세 조회 이벤트 ====================
 *
 * 이벤트 위임을 사용하므로 비동기로 생성된
 * 상세 버튼에도 동일하게 적용된다.
 */
document.addEventListener("click", function (event) {

    const button =
        event.target.closest('.disposal-detail-btn');

    if (!button) {
        return;
    }

    // 상세 버튼에 저장된 폐기 일련번호
    const disposalId = button.dataset.disposalId;

    if (!disposalId) {
        return;
    }

    // 현재 상세 조회 요청 순서 저장
    const requestSequence = ++disposalRequestSequence;

    fetch(`/api/disposal/${disposalId}`, {
        method: 'GET'
    })
        .then(function (response) {

            // HTTP 응답 상태 검증
            if (!response.ok) {

                throw new Error(
                    `폐기 상세 조회 실패: ${response.status}`
                );
            }

            // JSON 데이터 변환
            return response.json();
        })
        .then(function (data) {

            // 최신 요청이 아닌 경우 처리하지 않음
            if (requestSequence !== disposalRequestSequence) {
                return;
            }

            console.log("폐기 상세 정보:", data);
            console.log("로그인 사용자 권한:", data.role);

            // 이전 모달 상태 초기화
            resetDisposalModal();

            // 현재 조회 중인 폐기 정보 저장
            currentDisposal = data;

            // 폐기 상세 정보 바인딩
            bindDisposalDetail(data);

            // 폐기 상태 및 로그인 사용자 권한에 따른 UI 제어
            setDisposalModalButtons(
                data.disposalStatus,
                data.role,
                data.checkAt
            );

            // Bootstrap 모달 객체 가져오기
            const modalElement =
                document.querySelector('#disposalDetailModal');

            const modal =
                bootstrap.Modal.getOrCreateInstance(modalElement);

            // 데이터 바인딩 완료 후 모달 출력
            modal.show();

        })
        .catch(function (error) {

            // 이전 요청에서 발생한 오류는 무시
            if (requestSequence !== disposalRequestSequence) {
                return;
            }

            console.error("폐기 상세 처리 오류:", error);

            alert("폐기 상세 정보를 조회하지 못했습니다.");
        });
});


/**
 * ==================== 폐기 상세 정보 바인딩 ====================
 */
function bindDisposalDetail(data) {

    // 폐기 일련번호 저장
    setValue('#detailDisposalId', data.disposalId);

    // 기본 정보
    setText('#detailOrderDetailsId', data.orderDetailId);
    setText('#detailDisposalScope', data.disposalUnit);
    setText('#detailStatus', data.disposalStatus);

    // 폐기 상태별 배지 색상 설정
    setDisposalStatusBadge(data.disposalStatus);

    // 폐기 정보
    setText('#detailStore', data.storeName);
    setText('#detailIngredient', data.ingredientName);
    setText('#detailDisposalUnit', data.disposalUnit);
    setText('#detailProcessingMethod', data.processingMethod);

    // 폐기 수량 및 단위
    if (data.disposalQuantity != null) {

        setText(
            '#detailAmount',
            data.disposalQuantity + (data.orderUnit ?? '')
        );

    } else {

        setText('#detailAmount', '—');
    }

    // 폐기 요청 정보
    setText(
        '#detailReqAt',
        formatDisposalDateTime(data.disposalReqAt)
    );

    setText(
        '#detailRequestUser',
        formatUser(data.reqUserRole, data.reqUserName)
    );

    // 폐기 승인 및 거절 정보
    setText(
        '#detailApprovedAt',
        formatDisposalDateTime(data.disposalAppAt)
    );

    setText(
        '#detailProcessor',
        formatUser(data.appUserRole, data.appUserName)
    );

    // 폐기 유형 및 사유
    // 일반 HTML 요소에 바인딩하므로 수정 불가능
    setText('#detailType', data.disposalType);
    setText('#detailReason', data.disposalReason);
    setText('#detailDetailsReason', data.disposalDetailReason);

    // 거절 사유 조회 정보 바인딩
    bindRejectionInformation(data);

    // 금액 정보
    setText(
        '#detailUnitPrice',
        formatPrice(data.supplyPrice)
    );

    setText(
        '#detailQuantity',
        data.disposalQuantity
    );

    // 폐기 비용
    if (data.disposalPrice != null) {

        setText(
            '#detailTotalPrice',
            formatPrice(data.disposalPrice) + '원'
        );

    } else {

        setText('#detailTotalPrice', '—');
    }

    // 폐기 확인 정보 바인딩
    bindDisposalConfirmation(data);
}


/**
 * ==================== 거절 사유 조회 정보 바인딩 ====================
 *
 * 거절 상태에서는 조회 전용 영역만 표시한다.
 * 입력 영역 활성화는 setDisposalModalButtons()에서 처리한다.
 */
function bindRejectionInformation(data) {

    const rejectionViewArea =
        document.querySelector('#rejectionViewArea');

    // 거절 상태일 경우 조회 전용 영역 표시
    if (data.disposalStatus === '거절') {

        rejectionViewArea.hidden = false;

        // 일반 div에 바인딩하므로 입력 및 수정 불가능
        setText(
            '#detailRejectReason',
            data.rejectReason
        );

    } else {

        // 거절 상태가 아니면 조회 영역 숨김
        rejectionViewArea.hidden = true;

        setText('#detailRejectReason', '—');
    }
}


/**
 * ==================== 폐기 확인 정보 바인딩 ====================
 */
function bindDisposalConfirmation(data) {

    // 폐기 처리 일시
    setText(
        '#detailDisposedAt',
        formatDisposalDateTime(data.checkAt)
    );

    // 폐기 처리 직원
    setText(
        '#detailDisposalCheckUser',
        formatUser(data.checkUserRole, data.checkUserName)
    );

    const image =
        document.querySelector('#detailDisposalImage');

    const imageMessage =
        document.querySelector('#detailDisposalImageMessage');

    // 서버에서 전달받은 이미지 조회 URL
    if (data.disposalImg) {

        image.src = data.disposalImg;

        image.hidden = false;
        imageMessage.hidden = true;

    } else {

        // 이전 조회 이미지 제거
        image.removeAttribute('src');

        image.hidden = true;
        imageMessage.hidden = false;

        imageMessage.textContent =
            '등록된 폐기 이미지가 없습니다.';
    }

    const confirmationContent =
        document.querySelector('#disposalConfirmationContent');

    const confirmationMessage =
        document.querySelector('#disposalConfirmationMessage');

    // 승인 및 완료 상태에서는 폐기 확인 정보 조회 가능
    if (data.disposalStatus === '승인' ||
        data.disposalStatus === '완료') {

        confirmationContent.hidden = false;
        confirmationMessage.hidden = true;

    } else {

        // 요청 및 거절 상태에서는 폐기 확인 정보 숨김
        confirmationContent.hidden = true;
        confirmationMessage.hidden = false;

        if (data.disposalStatus === '거절') {

            confirmationMessage.textContent =
                '거절된 폐기 요청입니다.';

        } else {

            confirmationMessage.textContent =
                '폐기 승인 후 확인 정보를 볼 수 있습니다.';
        }
    }
}


/**
 * ==================== 상태 및 권한별 버튼 제어 ====================
 *
 * 요청 + STORE          : 삭제
 * 요청 + HQ/HQ_ADMIN    : 승인, 거절, 거절 사유 입력
 * 승인 + STORE          : 폐기 확인 정보 입력
 * 승인 + 확인 등록 완료 : 조회만 가능
 * 거절                  : 조회만 가능
 * 완료                  : 조회만 가능
 */
function setDisposalModalButtons(status, role, checkAt) {

    // 모든 버튼 및 입력 영역 초기화
    hideDisposalModalButtons();
    resetRejectionInput();
    resetConfirmationInput();

    const footer =
        document.querySelector('#disposalModalFooter');

    const rejectionInputArea =
        document.querySelector('#rejectionInputArea');

    const rejectReasonInput =
        document.querySelector('#rejectReasonInput');

    switch (status) {

        case '요청':

            // 직영점 직원 : 삭제 가능
            if (role === 'STORE') {

                footer.hidden = false;

                setButtonEnabled(
                    '#detailDeleteBtn',
                    true
                );
            }

            // 본사 직원 및 관리자 : 승인, 거절 가능
            if (role === 'HQ' || role === 'HQ_ADMIN') {

                footer.hidden = false;

                // 승인 버튼 활성화
                setButtonEnabled(
                    '#detailApproveBtn',
                    true
                );

                // 거절 버튼 활성화
                setButtonEnabled(
                    '#detailRejectBtn',
                    true
                );

                // 거절 사유 입력 영역 기본 활성화
                rejectionInputArea.hidden = false;

                rejectReasonInput.disabled = false;
                rejectReasonInput.readOnly = false;

                // 승인 시에는 필수 입력이 아니므로
                // 거절 버튼 이벤트에서만 입력값 검증
                rejectReasonInput.required = false;
            }

            break;

        case '승인':

            // 직영점 직원이며 폐기 확인 정보가 없는 경우
            if (role === 'STORE' && checkAt == null) {

                footer.hidden = false;

                setButtonEnabled(
                    '#detailConfirmationBtn',
                    true
                );
            }

            break;

        case '거절':

            // 거절 사유 조회만 가능
            // 모든 업무 버튼 및 입력 요소 비활성화
            break;

        case '완료':

            // 모든 정보 조회만 가능
            break;

        default:

            // 알 수 없는 상태에서는 조회만 가능
            break;
    }
}


/**
 * ==================== 폐기 승인 버튼 이벤트 ====================
 *
 * 요청 상태의 HQ 및 HQ_ADMIN만 가능
 *
 * 거절 사유가 입력되어 있어도
 * 승인 요청에서는 rejectReason을 null로 전송한다.
 */
document.addEventListener("click", function (event) {

    const button =
        event.target.closest('#detailApproveBtn');

    if (!button || button.hidden || button.disabled) {
        return;
    }

    // 요청 상태 및 본사 직원 권한 재검증
    if (!canApproveOrReject()) {
        return;
    }

    // 승인 여부 확인
    if (!confirm("해당 폐기 요청을 승인하시겠습니까?")) {
        return;
    }

    const disposalId = currentDisposal.disposalId;

    // 서버로 전달할 승인 요청 데이터
    const requestData = {

        disposalStatus: '승인',

        // 거절 사유 입력값과 관계없이 null 처리
        rejectReason: null
    };

    console.log("승인 대상 폐기 ID:", disposalId);
    console.log("승인 요청 데이터:", requestData);

    fetch("/api/hq/approve/" + disposalId, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData)
    }).then(() => {
        console.log("Send Data : ", requestData);
        return refreshDisposalModal(disposalId);
    }).catch(error => {
        alert("Fail Approve");
    });
});


/**
 * ==================== 폐기 거절 버튼 이벤트 ====================
 *
 * 요청 상태의 HQ 및 HQ_ADMIN만 가능
 *
 * 거절 사유는 상세 조회 시 이미 활성화되어 있으므로
 * 별도의 거절 입력 모드로 전환하지 않는다.
 */
document.addEventListener("click", function (event) {

    const button =
        event.target.closest('#detailRejectBtn');

    if (!button || button.hidden || button.disabled) {
        return;
    }

    // 요청 상태 및 본사 직원 권한 재검증
    if (!canApproveOrReject()) {
        return;
    }

    const rejectReasonInput =
        document.querySelector('#rejectReasonInput');

    // 거절 사유 입력창이 비활성화된 경우 처리하지 않음
    if (rejectReasonInput.disabled ||
        rejectReasonInput.readOnly) {

        return;
    }

    // 거절 사유 가져오기 및 앞뒤 공백 제거
    const rejectReason =
        rejectReasonInput.value.trim();

    // 거절 사유 필수 입력 검증
    if (!rejectReason) {

        alert("거절 사유를 입력해주세요.");

        rejectReasonInput.focus();

        return;
    }

    // 거절 사유 최대 길이 검증
    if (rejectReason.length > 100) {

        alert("거절 사유는 100자 이하로 입력해주세요.");

        rejectReasonInput.focus();

        return;
    }

    // 거절 여부 확인
    if (!confirm("해당 폐기 요청을 거절하시겠습니까?")) {
        return;
    }

    const disposalId = currentDisposal.disposalId;

    // 서버로 전달할 거절 요청 데이터
    const requestData = {

        disposalStatus: '거절',

        rejectReason: rejectReason
    };

    console.log("거절 대상 폐기 ID:", disposalId);
    console.log("거절 요청 데이터:", requestData);

    fetch("/api/hq/approve/" + disposalId, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData)
    }).then(() => {
        console.log("Send Data : ", requestData);
        return refreshDisposalModal(disposalId);
    }).catch(error => {
        alert("Fail Approve");
    });
});


/**
 * ==================== 폐기 삭제 버튼 이벤트 ====================
 *
 * 요청 상태의 직영점 직원만 가능
 */
document.addEventListener("click", function (event) {

    const button =
        event.target.closest('#detailDeleteBtn');

    if (!button || button.hidden || button.disabled) {
        return;
    }

    // 요청 상태 및 직영점 직원 권한 재검증
    if (!canDeleteDisposal()) {
        return;
    }

    // 삭제 여부 확인
    if (!confirm("해당 폐기 요청을 삭제하시겠습니까?")) {
        return;
    }

    const disposalId = currentDisposal.disposalId;

    console.log("삭제 대상 폐기 ID:", disposalId);

    // TODO : 폐기 삭제 REST API 연동
    // 서버 성공 응답 이후 모달 닫기 및 목록 갱신
    fetch(`/api/store/disposal/` + disposalId, {
        method: 'DELETE',
    })
        .then(response => {

            if (!response.ok) {

                throw new Error(
                    `폐기 상세 조회 실패: ${response.status}`
                );
            }

            bootstrap.Modal.getInstance(
                document.querySelector('#disposalDetailModal')
            ).hide();

        });
});


/**
 * ==================== 폐기 확인 정보 입력 이벤트 ====================
 *
 * 승인 상태의 직영점 직원만 이미지 입력 가능
 * 이미 폐기 확인 정보가 등록된 경우에는 입력 불가능
 */
document.addEventListener("click", function (event) {

    const button =
        event.target.closest('#detailConfirmationBtn');

    if (!button || button.hidden || button.disabled) {
        return;
    }

    // 승인 상태 및 직영점 직원 권한 재검증
    if (!canRegisterConfirmation()) {
        return;
    }

    const inputArea =
        document.querySelector('#disposalConfirmationInputArea');

    const imageFile =
        document.querySelector('#disposalImageFile');

    // 이전에 선택한 이미지 초기화
    resetDisposalImageSelection();

    // 기존 폐기 확인 정보 영역 펼치기
    document.querySelector('#disposalConfirmation').open = true;

    // 폐기 이미지 입력 영역 표시
    inputArea.hidden = false;

    // 이미지 파일 입력 활성화
    imageFile.disabled = false;
    imageFile.required = true;

    // 기존 폐기 확인 정보 입력 버튼 숨김
    setButtonEnabled(
        '#detailConfirmationBtn',
        false
    );

    // 폐기 확인 등록 버튼 활성화
    setButtonEnabled(
        '#detailConfirmationRegisterBtn',
        true
    );

    // 폐기 확인 입력 취소 버튼 활성화
    setButtonEnabled(
        '#detailConfirmationCancelBtn',
        true
    );

    // 이미지 입력 영역으로 스크롤 이동
    inputArea.scrollIntoView({
        behavior: 'smooth',
        block: 'nearest'
    });

    imageFile.focus();
});


/**
 * ==================== 폐기 이미지 선택 이벤트 ====================
 *
 * 이미지 파일 선택 시
 *
 * 1. 상태 및 권한 검증
 * 2. 파일 형식 및 크기 검증
 * 3. 이미지 미리보기 URL 생성
 * 4. 미리보기 화면 표시
 */
document.addEventListener("change", function (event) {

    const imageFile = event.target;

    // 폐기 증빙 이미지 입력 요소가 아니면 종료
    if (imageFile.id !== 'disposalImageFile') {
        return;
    }

    // 폐기 확인 등록 가능 여부 검증
    if (!canRegisterConfirmation() || imageFile.disabled) {

        resetDisposalImageSelection();

        return;
    }

    // 기존 이미지 미리보기 및 오류 메시지 초기화
    clearDisposalImagePreview();
    clearDisposalImageError();

    // 이미지 선택이 취소된 경우
    if (imageFile.files.length === 0) {
        return;
    }

    // 선택한 이미지 파일
    const file = imageFile.files[0];

    // 이미지 파일 검증
    const errorMessage = validateDisposalImage(file);

    if (errorMessage) {

        // 잘못된 파일 입력값 제거
        imageFile.value = '';

        showDisposalImageError(errorMessage);

        return;
    }

    const previewArea =
        document.querySelector('#disposalImagePreviewArea');

    const previewImage =
        document.querySelector('#disposalImagePreview');

    const fileName =
        document.querySelector('#disposalImageFileName');

    const removeImageButton =
        document.querySelector('#detailRemoveImageBtn');

    // 이미지 미리보기용 임시 URL 생성
    disposalImagePreviewUrl =
        URL.createObjectURL(file);

    // 미리보기 이미지 설정
    previewImage.src = disposalImagePreviewUrl;

    // 선택한 파일 이름 표시
    fileName.textContent = file.name;

    // 이미지 미리보기 영역 표시
    previewArea.hidden = false;

    // 이미지 제거 버튼 활성화
    removeImageButton.disabled = false;
});


/**
 * ==================== 이미지 제거 버튼 이벤트 ====================
 *
 * 선택한 이미지 파일과 미리보기 정보 제거
 */
document.addEventListener("click", function (event) {

    const button =
        event.target.closest('#detailRemoveImageBtn');

    if (!button || button.disabled) {
        return;
    }

    // 승인 상태 및 직영점 직원 권한 재검증
    if (!canRegisterConfirmation()) {
        return;
    }

    const imageFile =
        document.querySelector('#disposalImageFile');

    if (imageFile.disabled) {
        return;
    }

    // 선택한 이미지 파일 및 미리보기 초기화
    resetDisposalImageSelection();
});


/**
 * ==================== 폐기 확인 입력 취소 이벤트 ====================
 *
 * 이미지 입력을 취소하고 기존 상세 조회 상태로 복구
 */
document.addEventListener("click", function (event) {

    const button =
        event.target.closest('#detailConfirmationCancelBtn');

    if (!button || button.hidden || button.disabled) {
        return;
    }

    // 승인 상태 및 직영점 직원 권한 재검증
    if (!canRegisterConfirmation()) {
        return;
    }

    // 상태 및 권한에 따른 기본 버튼 복구
    // 내부에서 이미지 입력값과 미리보기까지 초기화
    setDisposalModalButtons(
        currentDisposal.disposalStatus,
        currentDisposal.role,
        currentDisposal.checkAt
    );
});


/**
 * ==================== 폐기 확인 등록 이벤트 ====================
 *
 * 선택한 이미지 파일을 FormData에 담아 서버로 전송한다.
 *
 * 실제 파일 저장 및 저장 경로 관리는 서버에서 처리한다.
 */
document.addEventListener("click", function (event) {

    const button =
        event.target.closest('#detailConfirmationRegisterBtn');

    if (!button || button.hidden || button.disabled) {
        return;
    }

    // 승인 상태 및 직영점 직원 권한 재검증
    if (!canRegisterConfirmation()) {
        return;
    }

    const imageFile =
        document.querySelector('#disposalImageFile');

    // 이미지 입력 요소가 비활성화된 경우 처리하지 않음
    if (imageFile.disabled) {
        return;
    }

    // 이미지 첨부 여부 검증
    if (imageFile.files.length === 0) {

        showDisposalImageError(
            "폐기 증빙 이미지를 선택해주세요."
        );

        imageFile.focus();

        return;
    }

    const file = imageFile.files[0];

    // 이미지 파일 재검증
    const errorMessage = validateDisposalImage(file);

    if (errorMessage) {

        resetDisposalImageSelection();

        showDisposalImageError(errorMessage);

        return;
    }

    // 폐기 확인 등록 여부 확인
    if (!confirm("폐기 확인 정보를 등록하시겠습니까?")) {
        return;
    }

    const disposalId = currentDisposal.disposalId;

    // 이미지 전송을 위한 FormData 생성
    const formData = new FormData();

    // 서버에서 받을 MultipartFile 파라미터명
    formData.append('disposalImg', file);

    fetch("/api/store/disposalApproveInfo/" + disposalId, {
        method: "PATCH",
        body: formData
    })
        .then(response => {

            // 폐기 확인 등록 결과 검증
            if (!response.ok) {

                throw new Error(
                    `폐기 확인 등록 실패: ${response.status}`
                );
            }

            // 폐기 상세 정보 갱신
            return refreshDisposalModal(disposalId);

        })
        .catch(error => {

            console.error("폐기 확인 등록 또는 상세 갱신 오류:", error);

            alert("폐기 확인 등록 또는 상세 정보 갱신에 실패했습니다.");
        });
});


/**
 * ==================== 승인 및 거절 가능 여부 ====================
 *
 * 요청 상태이며 HQ 또는 HQ_ADMIN일 경우에만 가능
 */
function canApproveOrReject() {

    if (currentDisposal == null) {
        return false;
    }

    // 요청 상태에서만 승인 및 거절 가능
    if (currentDisposal.disposalStatus !== '요청') {
        return false;
    }

    // 서버에서 전달받은 로그인 사용자 권한
    const role = currentDisposal.role;

    // 본사 직원 및 본사 관리자만 가능
    if (role !== 'HQ' && role !== 'HQ_ADMIN') {
        return false;
    }

    return true;
}


/**
 * ==================== 삭제 가능 여부 ====================
 *
 * 요청 상태이며 직영점 직원일 경우에만 가능
 */
function canDeleteDisposal() {

    if (currentDisposal == null) {
        return false;
    }

    if (currentDisposal.disposalStatus !== '요청') {
        return false;
    }

    if (currentDisposal.role !== 'STORE') {
        return false;
    }

    return true;
}


/**
 * ==================== 폐기 확인 등록 가능 여부 ====================
 *
 * 승인 상태이며 직영점 직원이고
 * 아직 폐기 확인 정보가 등록되지 않은 경우에만 가능
 */
function canRegisterConfirmation() {

    if (currentDisposal == null) {
        return false;
    }

    if (currentDisposal.disposalStatus !== '승인') {
        return false;
    }

    if (currentDisposal.role !== 'STORE') {
        return false;
    }

    // 이미 폐기 확인 정보가 등록된 경우
    if (currentDisposal.checkAt != null) {
        return false;
    }

    return true;
}


/**
 * ==================== 이미지 파일 검증 ====================
 *
 * 정상적인 파일이면 null 반환
 * 잘못된 파일이면 오류 메시지 반환
 */
function validateDisposalImage(file) {

    // 이미지 파일 존재 여부
    if (!file) {
        return '폐기 증빙 이미지를 선택해주세요.';
    }

    // 이미지 파일 형식 검증
    if (file.type !== 'image/jpeg' &&
        file.type !== 'image/png') {

        return 'JPG 또는 PNG 이미지만 등록할 수 있습니다.';
    }

    // 빈 파일 검증
    if (file.size === 0) {

        return '비어 있는 파일은 등록할 수 없습니다.';
    }

    // TODO : 서버의 multipart 설정에 맞춰 파일 크기 제한 추가

    return null;
}


/**
 * ==================== 이미지 오류 메시지 표시 ====================
 */
function showDisposalImageError(message) {

    const errorElement =
        document.querySelector('#disposalImageError');

    const imageFile =
        document.querySelector('#disposalImageFile');

    // 오류 메시지 표시
    errorElement.textContent = message;
    errorElement.hidden = false;

    // Bootstrap 입력 오류 스타일 적용
    imageFile.classList.add('is-invalid');
}


/**
 * ==================== 이미지 오류 메시지 초기화 ====================
 */
function clearDisposalImageError() {

    const errorElement =
        document.querySelector('#disposalImageError');

    const imageFile =
        document.querySelector('#disposalImageFile');

    // 오류 메시지 제거
    errorElement.textContent = '';
    errorElement.hidden = true;

    // Bootstrap 입력 오류 스타일 제거
    imageFile.classList.remove('is-invalid');
}


/**
 * ==================== 이미지 미리보기 초기화 ====================
 *
 * 기존 Object URL을 해제하여 메모리 누수 방지
 */
function clearDisposalImagePreview() {

    const previewArea =
        document.querySelector('#disposalImagePreviewArea');

    const previewImage =
        document.querySelector('#disposalImagePreview');

    const fileName =
        document.querySelector('#disposalImageFileName');

    const removeImageButton =
        document.querySelector('#detailRemoveImageBtn');

    // 기존 이미지 미리보기 URL 메모리 해제
    if (disposalImagePreviewUrl != null) {

        URL.revokeObjectURL(disposalImagePreviewUrl);

        disposalImagePreviewUrl = null;
    }

    // 미리보기 이미지 제거
    previewImage.removeAttribute('src');

    // 파일 이름 제거
    fileName.textContent = '';

    // 이미지 미리보기 영역 숨김
    previewArea.hidden = true;

    // 이미지 제거 버튼 비활성화
    removeImageButton.disabled = true;
}


/**
 * ==================== 이미지 선택 정보 초기화 ====================
 */
function resetDisposalImageSelection() {

    const imageFile =
        document.querySelector('#disposalImageFile');

    // 선택한 파일 제거
    imageFile.value = '';

    // 이미지 미리보기 제거
    clearDisposalImagePreview();

    // 이미지 오류 메시지 제거
    clearDisposalImageError();
}


/**
 * ==================== 버튼 활성화 및 비활성화 ====================
 *
 * enabled가 true이면 버튼 표시 및 활성화
 * enabled가 false이면 버튼 숨김 및 비활성화
 */
function setButtonEnabled(selector, enabled) {

    const button =
        document.querySelector(selector);

    button.hidden = !enabled;
    button.disabled = !enabled;
}


/**
 * ==================== 모든 하단 버튼 초기화 ====================
 */
function hideDisposalModalButtons() {

    const footer =
        document.querySelector('#disposalModalFooter');

    // 모달 하단 영역 숨김
    footer.hidden = true;

    // 모든 업무 버튼 숨김 및 비활성화
    setButtonEnabled('#detailDeleteBtn', false);
    setButtonEnabled('#detailApproveBtn', false);
    setButtonEnabled('#detailRejectBtn', false);
    setButtonEnabled('#detailConfirmationBtn', false);
    setButtonEnabled('#detailConfirmationRegisterBtn', false);
    setButtonEnabled('#detailConfirmationCancelBtn', false);
}


/**
 * ==================== 거절 사유 입력 영역 초기화 ====================
 *
 * 이전에 입력한 거절 사유가
 * 다음 상세 조회에 영향을 주지 않도록 초기화
 */
function resetRejectionInput() {

    const rejectionInputArea =
        document.querySelector('#rejectionInputArea');

    const rejectReasonInput =
        document.querySelector('#rejectReasonInput');

    // 거절 사유 입력 영역 숨김
    rejectionInputArea.hidden = true;

    // 입력값 초기화 및 비활성화
    rejectReasonInput.value = '';
    rejectReasonInput.disabled = true;
    rejectReasonInput.readOnly = true;
    rejectReasonInput.required = false;
}


/**
 * ==================== 폐기 확인 입력 영역 초기화 ====================
 */
function resetConfirmationInput() {

    const inputArea =
        document.querySelector('#disposalConfirmationInputArea');

    const imageFile =
        document.querySelector('#disposalImageFile');

    // 이미지 입력 영역 숨김
    inputArea.hidden = true;

    // 이미지 입력값 및 미리보기 초기화
    resetDisposalImageSelection();

    // 이미지 입력 비활성화
    imageFile.disabled = true;
    imageFile.required = false;

    // 폐기 확인 등록 버튼 비활성화
    setButtonEnabled(
        '#detailConfirmationRegisterBtn',
        false
    );

    // 폐기 확인 입력 취소 버튼 비활성화
    setButtonEnabled(
        '#detailConfirmationCancelBtn',
        false
    );
}


/**
 * ==================== 모달 전체 상태 초기화 ====================
 *
 * 이전에 조회한 폐기 정보의 입력 상태가
 * 다음 상세 조회에 영향을 주지 않도록 초기화
 */
function resetDisposalModal() {

    // 현재 조회 중인 폐기 정보 초기화
    currentDisposal = null;

    // 폼 입력값 초기화
    document.querySelector('#disposalModalForm').reset();

    // 모든 하단 버튼 비활성화
    hideDisposalModalButtons();

    // 거절 사유 조회 영역 초기화
    document.querySelector('#rejectionViewArea').hidden = true;

    setText('#detailRejectReason', '—');

    // 거절 사유 입력 영역 초기화
    resetRejectionInput();

    // 폐기 확인 정보 입력 영역 초기화
    resetConfirmationInput();

    // 폐기 확인 정보 접기
    document.querySelector('#disposalConfirmation').open = false;

    // 폐기 확인 정보 조회 영역 숨김
    document.querySelector(
        '#disposalConfirmationContent'
    ).hidden = true;

    const confirmationMessage =
        document.querySelector('#disposalConfirmationMessage');

    // 폐기 확인 정보 안내 초기화
    confirmationMessage.hidden = false;

    confirmationMessage.textContent =
        '폐기 승인 후 확인 정보를 볼 수 있습니다.';

    // 이전에 조회한 폐기 이미지 제거
    const image =
        document.querySelector('#detailDisposalImage');

    image.removeAttribute('src');
    image.hidden = true;

    // 이미지 미등록 안내 초기화
    const imageMessage =
        document.querySelector('#detailDisposalImageMessage');

    imageMessage.hidden = false;

    imageMessage.textContent =
        '등록된 폐기 이미지가 없습니다.';
}


/**
 * ==================== 폼 기본 제출 방지 ====================
 *
 * 모든 업무 처리는 fetch를 통해 수행하므로
 * 브라우저의 기본 form 제출을 방지한다.
 */
document.addEventListener("submit", function (event) {

    if (event.target.id !== 'disposalModalForm') {
        return;
    }

    event.preventDefault();
});


/**
 * ==================== 모달 닫기 이벤트 ====================
 *
 * 모달을 닫으면 현재 조회 정보 및 입력 상태 초기화
 */
document.addEventListener("hidden.bs.modal", function (event) {

    if (event.target.id !== 'disposalDetailModal') {
        return;
    }

    // 진행 중인 이전 상세 조회 응답 무효화
    disposalRequestSequence++;

    // 모달 입력 정보 및 이미지 미리보기 초기화
    resetDisposalModal();
});


/**
 * ==================== 기존 폐기 이미지 로딩 오류 처리 ====================
 *
 * 서버에서 폐기 이미지를 불러오지 못한 경우
 */
document.addEventListener("error", function (event) {

    const image = event.target;

    if (image.id !== 'detailDisposalImage') {
        return;
    }

    image.hidden = true;

    const imageMessage =
        document.querySelector('#detailDisposalImageMessage');

    imageMessage.hidden = false;

    imageMessage.textContent =
        '폐기 이미지를 불러오지 못했습니다.';

}, true);


/**
 * ==================== 첨부 이미지 미리보기 오류 처리 ====================
 *
 * 선택한 파일이 이미지로 표시되지 않는 경우
 */
document.addEventListener("error", function (event) {

    if (event.target.id !== 'disposalImagePreview') {
        return;
    }

    // 현재 이미지 입력 상태인지 확인
    if (!canRegisterConfirmation() ||
        document.querySelector('#disposalImageFile').disabled) {

        return;
    }

    // 이미지 파일 및 미리보기 초기화
    resetDisposalImageSelection();

    showDisposalImageError(
        '정상적인 이미지 파일을 선택해주세요.'
    );

}, true);


/**
 * ==================== 폐기 상태별 배지 색상 ====================
 */
function setDisposalStatusBadge(status) {

    const badge =
        document.querySelector('#detailStatus');

    // 기존 상태 색상 제거
    badge.classList.remove(
        'text-bg-warning',
        'text-bg-success',
        'text-bg-danger',
        'text-bg-primary',
        'text-bg-secondary'
    );

    switch (status) {

        case '요청':

            badge.classList.add('text-bg-warning');

            break;

        case '승인':

            badge.classList.add('text-bg-success');

            break;

        case '거절':

            badge.classList.add('text-bg-danger');

            break;

        case '완료':

            badge.classList.add('text-bg-primary');

            break;

        default:

            badge.classList.add('text-bg-secondary');

            break;
    }
}


/**
 * ==================== 일반 HTML 요소 데이터 설정 ====================
 *
 * span, div, dd, strong 등의 요소에 사용
 *
 * textContent를 사용하여 HTML 코드 삽입 방지
 */
function setText(selector, value) {

    const element =
        document.querySelector(selector);

    element.textContent = value ?? '—';
}


/**
 * ==================== 입력 요소 데이터 설정 ====================
 *
 * input, textarea 등의 요소에 사용
 */
function setValue(selector, value) {

    const element =
        document.querySelector(selector);

    element.value = value ?? '';
}


/**
 * ==================== 날짜 포맷 변환 ====================
 *
 * yyyy-MM-dd HH:mm:ss 형식
 */
function formatDisposalDateTime(value) {

    if (!value) {
        return '—';
    }

    const date = new Date(value);

    // 날짜 변환 실패 시 기본값 반환
    if (Number.isNaN(date.getTime())) {
        return '—';
    }

    const year =
        date.getFullYear();

    const month =
        String(date.getMonth() + 1).padStart(2, '0');

    const day =
        String(date.getDate()).padStart(2, '0');

    const hour =
        String(date.getHours()).padStart(2, '0');

    const minute =
        String(date.getMinutes()).padStart(2, '0');

    const second =
        String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
}


/**
 * ==================== 사용자 권한 및 이름 포맷 ====================
 */
function formatUser(role, name) {

    if (!name) {
        return '—';
    }

    const roleNames = {

        HQ_ADMIN: '본사 관리자',

        HQ: '본사 직원',

        STORE: '직영점 직원'
    };

    const roleName =
        roleNames[role] ?? role;

    if (!roleName) {
        return name;
    }

    return `[${roleName}] ${name}`;
}


/**
 * ==================== 금액 포맷 ====================
 *
 * 천 단위 콤마 적용
 */
function formatPrice(value) {

    if (value == null) {
        return '—';
    }

    return Number(value).toLocaleString('ko-KR');
}


/**
 * ==================== 폐기 상세 모달 갱신 ====================
 *
 * 폐기 승인, 거절, 확인 등록 이후
 * 서버에서 최신 폐기 상세 정보를 조회하여 모달을 갱신한다.
 */
function refreshDisposalModal(disposalId) {

    const requestSequence = ++disposalRequestSequence;

    return fetch(`/api/disposal/${disposalId}`, {
        method: 'GET',
    })
        .then(response => {

            if (!response.ok) {

                throw new Error(
                    `폐기 상세 조회 실패: ${response.status}`
                );
            }

            return response.json();
        })
        .then(data => {

            // 모달이 닫혔거나 다른 상세 조회가 실행된 경우
            if (requestSequence !== disposalRequestSequence) {
                return;
            }

            // 현재 폐기 정보 갱신
            currentDisposal = data;

            // 모달 내부 데이터 갱신
            bindDisposalDetail(data);

            // 상태 및 권한에 따른 버튼 갱신
            setDisposalModalButtons(
                data.disposalStatus,
                data.role,
                data.checkAt
            );
        });
}