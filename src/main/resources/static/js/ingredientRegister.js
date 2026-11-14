
document.addEventListener("DOMContentLoaded", function () {

    // 중복 생성 방지
    if (document.getElementById("ingredientRegisterModal")) return;

    // 등록 모달 생성
    const modalHTML = `
        <div class="modal fade" id="ingredientRegisterModal"
             tabindex="-1" aria-labelledby="ingredientRegisterTitle"
             aria-hidden="true">

            <div class="modal-dialog modal-lg modal-dialog-scrollable h-100 my-0 py-3">
                <div class="modal-content h-100">

                    <div class="modal-header">
                        <h5 class="modal-title fw-bold" id="ingredientRegisterTitle">
                            식재료 등록
                        </h5>
                        <button type="button" class="btn-close"
                                data-bs-dismiss="modal" aria-label="닫기"></button>
                    </div>

                    <form id="ingredientRegisterForm"
      class="d-flex flex-column flex-grow-1 overflow-hidden">

                        <div class="modal-body overflow-y-auto">

                            <!-- 1. 기본 식재료 정보 -->
                            <div class="bg-light rounded p-3 mb-3">
                                <h6 class="fw-bold mb-3">기본 식재료 정보</h6>

                                <div class="row g-3">

                                    <div class="col-md-6">
                                        <label for="ingredientName"
                                               class="form-label fw-semibold">식재료명</label>
                                        <input type="text" class="form-control"
                                               id="ingredientName" name="name"
                                               placeholder="예: 토마토" required>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="majorCategory"
                                               class="form-label fw-semibold">식재료 대분류</label>
                                        <select class="form-select"
                                                id="majorCategory" name="majorCategory"
                                                required>
                                            <option value="">대분류 선택</option>
                                            <option value="UNPROCESSED">비가공</option>
                                            <option value="PROCESSED">가공</option>
                                        </select>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="ingredientCategory"
                                               class="form-label fw-semibold">식재료 소분류</label>
                                        <select class="form-select"
                                                id="ingredientCategory" name="categoryId"
                                                required disabled>
                                            <option value="">대분류를 먼저 선택하세요</option>
                                        </select>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="inboundUnit"
                                               class="form-label fw-semibold">입고 단위</label>
                                        <input type="text" class="form-control"
                                               id="inboundUnit" name="unit"
                                               placeholder="예: 박스, 봉, EA" required>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="capacity"
                                               class="form-label fw-semibold">용량</label>
                                        <input type="number" class="form-control"
                                               id="capacity" name="capacity"
                                               min="0.01" step="0.01"
                                               placeholder="예: 1" required>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="capacityUnit"
                                               class="form-label fw-semibold">용량 단위</label>
                                        <select class="form-select"
                                                id="capacityUnit" name="unitType"
                                                required>
                                            <option value="">단위 선택</option>
                                            <option value="KG">kg</option>
                                            <option value="G">g</option>
                                            <option value="L">L</option>
                                            <option value="ML">ml</option>
                                            <option value="EA">개</option>
                                        </select>
                                    </div>

                                </div>
                            </div>

                            <!-- 2. 보관 및 소비기한 정보 -->
                            <div class="bg-light rounded p-3 mb-3">
                                <h6 class="fw-bold mb-3">보관 및 소비기한 정보</h6>

                                <div class="row g-3">

                                    <div class="col-md-6">
                                        <label for="storageType"
                                               class="form-label fw-semibold">보관유형</label>
                                        <select class="form-select"
                                                id="storageType" name="storageType"
                                                required>
                                            <option value="">보관유형 선택</option>
                                            <option value="FROZEN">냉동</option>
                                            <option value="REFRIGERATED">냉장</option>
                                            <option value="ROOM_TEMPERATURE">상온</option>
                                        </select>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="expirationDays"
                                               class="form-label fw-semibold">유통기한 (일)</label>
                                        <input type="number" class="form-control"
                                               id="expirationDays" name="expirationDate"
                                               min="1" placeholder="예: 30" required>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="managementStandard"
                                               class="form-label fw-semibold">관리방식</label>
                                        <select class="form-select"
                                                id="managementStandard"
                                                name="managementStandard" required>
                                            <option value="">관리방식 선택</option>
                                            <option value="PREPARATION">손질</option>
                                            <option value="OPENING">개봉</option>
                                            <option value="THAWING">해동</option>
                                        </select>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="consumptionPeriod"
                                               class="form-label fw-semibold">소비기한 (일)</label>
                                        <input type="number" class="form-control"
                                               id="consumptionPeriod"
                                               name="consumptionPeriod"
                                               min="1" placeholder="예: 3" required>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="summerConsumptionPeriod"
                                               class="form-label fw-semibold">여름 소비기한 (일)</label>
                                        <input type="number" class="form-control"
                                               id="summerConsumptionPeriod"
                                               name="summerConsumptionPeriod"
                                               min="1" placeholder="예: 2">
                                    </div>

                                </div>
                            </div>

                            <!-- 3. 공급 정보 -->
                            <div class="bg-light rounded p-3">
                                <h6 class="fw-bold mb-3">공급 정보</h6>

                                <div class="row g-3">

                                    <div class="col-md-6">
                                        <label for="supplyPrice"
                                               class="form-label fw-semibold">공급가액 (원)</label>
                                        <input type="number" class="form-control"
                                               id="supplyPrice" name="price"
                                               min="0" placeholder="예: 15000" required>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="supplyQuantity"
                                               class="form-label fw-semibold">공급 수량</label>
                                        <input type="number" class="form-control"
                                               id="supplyQuantity" name="quantity"
                                               min="0.01" step="0.01"
                                               placeholder="예: 10" required>
                                    </div>

                                </div>
                            </div>

                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary"
                                    data-bs-dismiss="modal">취소</button>
                            <button type="submit" class="btn btn-primary">등록</button>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML("beforeend", modalHTML);

    const modalElement = document.getElementById("ingredientRegisterModal");
    const form = document.getElementById("ingredientRegisterForm");
    const majorCategory = document.getElementById("majorCategory");
    const categorySelect = document.getElementById("ingredientCategory");

    // 등록 모달 열기
    window.openIngredientRegisterModal = function () {
        bootstrap.Modal.getOrCreateInstance(modalElement).show();
    };

    // 대분류 선택 시 소분류 조회
    majorCategory.addEventListener("change", async function () {

        const selectedMajor = majorCategory.value;

        categorySelect.replaceChildren();

        if (!selectedMajor) {
            categorySelect.add(new Option("대분류를 먼저 선택하세요", ""));
            categorySelect.disabled = true;
            return;
        }

        categorySelect.add(new Option("소분류 선택", ""));
        categorySelect.disabled = true;

        try {
            const response = await fetch(
                `/api/ingredient-categories?majorCategory=${encodeURIComponent(selectedMajor)}`
            );

            if (!response.ok) {
                throw new Error("소분류 조회에 실패했습니다.");
            }

            const categories = await response.json();

            categories.forEach(function (category) {
                categorySelect.add(
                    new Option(category.name, category.id)
                );
            });

            categorySelect.disabled = false;

        } catch (error) {
            alert(error.message);
        }
    });

    // 식재료 등록
    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        if (!form.reportValidity()) return;

        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());

        data.capacity = Number(data.capacity);
        data.expirationDate = Number(data.expirationDate);
        data.consumptionPeriod = Number(data.consumptionPeriod);
        data.summerConsumptionPeriod =
            data.summerConsumptionPeriod === ""
                ? null : Number(data.summerConsumptionPeriod);
        data.price = Number(data.price);
        data.quantity = Number(data.quantity);
        data.categoryId = Number(data.categoryId);

        try {
            const response = await fetch("/api/ingredients", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                throw new Error("식재료 등록에 실패했습니다.");
            }

            alert("식재료가 등록되었습니다.");

            bootstrap.Modal.getInstance(modalElement).hide();

            form.reset();
            categorySelect.replaceChildren(
                new Option("대분류를 먼저 선택하세요", "")
            );
            categorySelect.disabled = true;

            // 식재료 목록 갱신
            if (typeof renderIngredients === "function") {
                renderIngredients();
            }

        } catch (error) {
            alert(error.message);
        }
    });

});
