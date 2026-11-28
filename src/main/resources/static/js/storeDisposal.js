// 검색 조건
const ingredientName = document.querySelector("#ingredientName");
const disposalStatus = document.querySelector("#status");
const processingMethod = document.querySelector("#disposalTiming");
const startDate = document.querySelector("#startDate");
const endDate = document.querySelector("#endDate");
const sortType = document.querySelector("#requestedAtSort");
const pageSize = document.querySelector("#pageSize");

// 폐기 목록 영역
const tbody = document.querySelector("#disposalListRows");
const searchedCount = document.querySelector("#disposalTotalCount");
const disposalPagination = document.querySelector("#disposalListFooter");

// 페이지네이션 영역
const prevPageBtn = document.querySelector("#prevPageBtn");
const nextPageBtn = document.querySelector("#nextPageBtn");
const pageButtons = document.querySelectorAll(".page-number-btn");


// ==================================================
// 데이터 포맷 및 CSS 처리
// ==================================================

/**
 * 날짜 문자열/타임스탬프를
 * yyyy-MM-dd HH:mm:ss 형식으로 변환
 *
 * 값이 없으면 '—', 파싱 실패 시 원본 문자열 반환
 */
function formatDateTime(value) {

    if (!value) return "—";

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
        return String(value);
    }

    const yyyy = date.getFullYear();
    const MM = String(date.getMonth() + 1).padStart(2, "0");
    const dd = String(date.getDate()).padStart(2, "0");
    const HH = String(date.getHours()).padStart(2, "0");
    const mm = String(date.getMinutes()).padStart(2, "0");
    const ss = String(date.getSeconds()).padStart(2, "0");

    return `${yyyy}-${MM}-${dd} ${HH}:${mm}:${ss}`;
}


/**
 * 숫자 포맷 (천 단위 콤마)
 *
 * null/undefined/NaN 방어
 */
function formatNumber(value) {

    const num = Number(value);

    if (
        value === null ||
        value === undefined ||
        Number.isNaN(num)
    ) {
        return "0";
    }

    return num.toLocaleString("ko-KR");
}


/**
 * 폐기 상태에 따른 CSS 클래스명 반환
 */
function getStatusClass(status) {

    switch (status) {

        case "승인":
            return "status-approved";

        case "거절":
            return "status-rejected";

        case "완료":
            return "status-completed";

        case "요청":
        default:
            return "status-pending";
    }
}


// ==================================================
// 폐기 목록 렌더링
// ==================================================

/**
 * 조회된 폐기 목록을
 * #disposalListRows 내부에 렌더링한다.
 *
 * @param {Object} data PageInfo 응답 데이터
 */
function renderDisposalRows(data) {

    // 서버 응답에서 폐기 목록 추출
    const list = data.list ?? [];

    // 전체 검색 건수 갱신
    // 조회 결과가 0건이어도 정상적으로 갱신
    searchedCount.textContent = formatNumber(data.total);

    // 기존 목록 제거
    tbody.replaceChildren();

    // 조회 결과가 비어 있을 때
    if (!Array.isArray(list) || list.length === 0) {

        tbody.innerHTML = `
            <tr>
                <td colspan="10"
                    class="text-center text-secondary py-4">
                    조회된 폐기 내역이 없습니다.
                </td>
            </tr>
        `;

        return;
    }

    // 생성된 행을 모아서 DOM에 한 번에 추가
    const fragment = document.createDocumentFragment();

    list.forEach((item) => {

        const tr = document.createElement("tr");

        // 폐기 상세 조회를 위한 ID 저장
        if (item.disposalId !== undefined &&
            item.disposalId !== null) {

            tr.dataset.disposalId = item.disposalId;
        }

        // 출력 데이터 포맷
        const reqAtText = formatDateTime(item.disposalReqAt);
        const checkAtText = formatDateTime(item.checkAt);
        const ingredientNameText = item.ingredientName ?? "—";
        const quantityText = formatNumber(item.disposalQuantity);
        const priceText = `${formatNumber(item.disposalPrice)}원`;
        const disposalTypeText = item.disposalType ?? "—";
        const processingMethodText = item.processingMethod ?? "—";

        // 폐기 상태
        const statusClass = getStatusClass(item.disposalStatus);
        const statusText = item.disposalStatus ?? "—";

        // HTML 구조만 생성
        // 서버 응답 데이터를 innerHTML에 직접 삽입하지 않음
        tr.innerHTML = `
    <td class="nowrap"
        style="white-space:nowrap !important;word-break:keep-all;">
    </td>

    <td></td>

    <td class="fw-semibold"></td>

    <td class="cost-cell nowrap"></td>

    <td></td>

    <td></td>

    <td>
        <span class="status"></span>
    </td>

    <td class="nowrap"
        style="white-space:nowrap !important;word-break:keep-all;">
    </td>

    <td class="text-center">
        <button type="button"
                class="btn btn-outline-primary btn-sm py-0 px-2 nowrap disposal-detail-btn">
            상세
        </button>
    </td>
`;

        // 서버 응답 데이터는 textContent를 통해 안전하게 삽입
        const cells = tr.cells;

        cells[0].textContent = reqAtText;
        cells[1].textContent = ingredientNameText;
        cells[2].textContent = quantityText;
        cells[3].textContent = priceText;
        cells[4].textContent = disposalTypeText;
        cells[5].textContent = processingMethodText;
        cells[7].textContent = checkAtText;

// 폐기 상태 출력 및 CSS 클래스 적용
        const statusElement = tr.querySelector(".status");

        statusElement.textContent = statusText;
        statusElement.classList.add(statusClass);

        // 상세 버튼에 폐기 ID 설정
        const detailButton = tr.querySelector(".disposal-detail-btn");

        detailButton.dataset.disposalId = item.disposalId ?? "";
        console.log("버튼 PK : ", item.disposalId ?? "");

        fragment.appendChild(tr);
    });

    tbody.appendChild(fragment);
}


// ==================================================
// 하단 조회 범위 갱신
// ==================================================

/**
 * 현재 조회 범위 및 전체 검색 건수 갱신
 *
 * 예시 : 1–5 / 총 63건
 *
 * @param {Object} data PageInfo 응답 데이터
 */
function updateListFooter(data) {

    // 하단 조회 범위 요소가 없으면 종료
    if (!disposalPagination) return;

    // 조회 결과가 없는 경우
    if (data.total === 0) {

        disposalPagination.textContent = "총 0건";

        return;
    }

    // 현재 조회 범위 및 전체 검색 건수 출력
    disposalPagination.textContent =
        `${formatNumber(data.startRow)}–` +
        `${formatNumber(data.endRow)} / ` +
        `총 ${formatNumber(data.total)}건`;
}


// ==================================================
// 페이지네이션 갱신
// ==================================================

/**
 * 기존 페이지 버튼의 속성을 변경한다.
 *
 * 버튼을 생성하거나 삭제하지 않는다.
 * 기존 숫자 버튼 5개를 계속 사용한다.
 *
 * @param {number} currentPage 현재 조회된 페이지 번호
 * @param {number} totalPages 전체 페이지 개수
 */
const updatePagination = function (currentPage, totalPages) {

    const blockSize = pageButtons.length;

    // 현재 페이지가 속한 블록의 시작 번호 계산
    //
    // currentPage = 1  → startPage = 1
    // currentPage = 5  → startPage = 1
    // currentPage = 6  → startPage = 6
    // currentPage = 12 → startPage = 11
    const startPage =
        Math.floor(
            (Math.max(1, currentPage) - 1) / blockSize
        ) * blockSize + 1;

    // 현재 페이지 블록의 마지막 번호
    const endPage = Math.min(
        startPage + blockSize - 1,
        totalPages
    );

    // ----------------------------------------------
    // 숫자 페이지 버튼 갱신
    // ----------------------------------------------

    pageButtons.forEach((button, index) => {

        // 각 버튼이 가리킬 페이지 번호
        const page = startPage + index;

        // 전체 페이지 개수를 초과하는지 확인
        const isOutOfRange = page > totalPages;

        // 현재 조회 중인 페이지인지 확인
        const isActive =
            page === currentPage && !isOutOfRange;

        // 화면에 표시되는 숫자 변경
        button.textContent = page;

        // 클릭 시 서버에 요청할 페이지 번호 변경
        button.dataset.page = page;

        // 전체 페이지를 초과하면 숨김 처리
        button.parentElement.hidden = isOutOfRange;

        // 숨겨진 버튼은 비활성화
        button.disabled = isOutOfRange;

        // 현재 조회 중인 페이지 강조
        button.parentElement.classList.toggle(
            "active",
            isActive
        );

        // 현재 페이지 접근성 속성 갱신
        if (isActive) {
            button.setAttribute("aria-current", "page");
        } else {
            button.removeAttribute("aria-current");
        }
    });

    // ----------------------------------------------
    // 이전 페이지 블록 버튼 갱신
    // ----------------------------------------------

    // 이전 블록의 첫 번째 페이지 번호 설정
    prevPageBtn.dataset.page = Math.max(
        1,
        startPage - blockSize
    );

    // 첫 번째 블록이거나 조회 결과가 없으면 비활성화
    prevPageBtn.disabled =
        startPage === 1 || totalPages === 0;

    // Bootstrap 비활성화 스타일 적용
    prevPageBtn.parentElement.classList.toggle(
        "disabled",
        prevPageBtn.disabled
    );

    // ----------------------------------------------
    // 다음 페이지 블록 버튼 갱신
    // ----------------------------------------------

    // 다음 블록의 첫 번째 페이지 번호 설정
    nextPageBtn.dataset.page = endPage + 1;

    // 마지막 블록이면 비활성화
    nextPageBtn.disabled = endPage >= totalPages;

    // Bootstrap 비활성화 스타일 적용
    nextPageBtn.parentElement.classList.toggle(
        "disabled",
        nextPageBtn.disabled
    );
};


// ==================================================
// 폐기 목록 비동기 조회
// ==================================================

/**
 * 검색 조건 및 페이지 번호를 기반으로
 * 폐기 목록을 비동기로 조회한다.
 *
 * @param {number} page 요청할 페이지 번호
 */
const getEventHandling = function (page) {

    // 유효하지 않은 페이지 번호 방어
    if (!Number.isInteger(page) || page < 1) {
        return;
    }

    // 검색 조건 수집
    const queryMap = {
        ingredientName: ingredientName.value.trim(),
        disposalStatus: disposalStatus.value,
        processingMethod: processingMethod.value,
        startDate: startDate.value,
        endDate: endDate.value,
        sortType: sortType.value,
        page: page,
        size: pageSize.value
    };

    console.log("요청 파라미터:", queryMap);

    // GET 요청 파라미터 생성
    const params = new URLSearchParams(queryMap);

    fetch("/api/storeDisposalList?" + params.toString(), {
        method: "GET",
    })
        .then((response) => {

            // HTTP 응답 상태 확인
            if (!response.ok) {

                throw new Error(
                    "서버 요청 실패: " + response.status
                );
            }

            return response.json();
        })
        .then((data) => {
            console.log(data);
            // 폐기 목록 갱신
            renderDisposalRows(data);

            // 하단 조회 범위 갱신
            updateListFooter(data);

            // 기존 페이지 버튼의 속성 갱신
            updatePagination(data.pageNum, data.pages);
        })
        .catch((error) => {
            console.error("조회 실패:", error);
            tbody.innerHTML = `
                <tr>
                    <td colspan="10"
                        class="text-center text-danger py-4">
                        목록을 불러오는 중 오류가 발생했습니다.
                    </td>
                </tr>
            `;
        });
};


// ==================================================
// 검색 조건 초기화
// ==================================================

/**
 * 검색 조건을 기본값으로 초기화한다.
 */
const resetFilter = function () {
    // 텍스트 및 날짜 입력값 초기화
    if (ingredientName) ingredientName.value = "";
    if (disposalStatus) disposalStatus.selectedIndex = 0;
    if (processingMethod) processingMethod.selectedIndex = 0;
    if (startDate) startDate.value = "";
    if (endDate) endDate.value = "";

    // 셀렉트 박스 기본값으로 초기화
    if (sortType) sortType.selectedIndex = 0;
    if (pageSize) pageSize.selectedIndex = 0;
};


// ==================================================
// 페이지 이동
// ==================================================

/**
 * 선택한 페이지로 비동기 조회 요청
 *
 * @param {number} page 이동할 페이지 번호
 */
const changePage = function (page) {

    getEventHandling(page);
};


// ==================================================
// 이벤트 바인딩
// ==================================================

// 검색 버튼 클릭 이벤트
// 검색 조건이 변경되면 첫 번째 페이지부터 조회
document.querySelector("#disposalSearchBtn")
    .addEventListener("click", () => {

        getEventHandling(1);
    });


// 초기화 버튼 클릭 이벤트
document.querySelector("#disposalResetBtn")
    .addEventListener("click", resetFilter);


// 이전 페이지 블록 이동
prevPageBtn.addEventListener("click", () => {

    // 이전 버튼이 가지고 있는 페이지 번호 추출
    const page = Number(prevPageBtn.dataset.page);

    changePage(page);
});


// 다음 페이지 블록 이동
nextPageBtn.addEventListener("click", () => {

    // 다음 버튼이 가지고 있는 페이지 번호 추출
    const page = Number(nextPageBtn.dataset.page);

    changePage(page);
});


// 숫자 페이지 버튼 이벤트 등록
// 기존 버튼을 유지하므로 최초 한 번만 등록
pageButtons.forEach(button => {

    button.addEventListener("click", () => {

        // 클릭 시점에 버튼이 가지고 있는 페이지 번호 추출
        const page = Number(button.dataset.page);

        // 해당 페이지 비동기 조회
        changePage(page);
    });
});


// 페이지당 조회 개수 변경 이벤트
// 조회 개수가 변경되면 첫 번째 페이지부터 조회
pageSize.addEventListener("change", () => {
    getEventHandling(1);
});

sortType.addEventListener("change", () => {
    getEventHandling(1);
});