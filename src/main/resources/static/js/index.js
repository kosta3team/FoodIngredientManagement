const statisticsUrl = document.body.dataset.statisticsUrl;

const costStore = document.querySelector('#costStore');
const typeStore = document.querySelector('#typeStore');
const ingredientStore = document.querySelector('#ingredientStore');
const ingredientCountStore = document.querySelector('#ingredientCountStore');

const storeIngredientPriceRows =
    document.querySelector('#storeIngredientPriceRows');

const storeTypeRows =
    document.querySelector('#storeTypeRows');

const storeIngredientQuantityRows =
    document.querySelector('#storeIngredientQuantityRows');

const storeIngredientCountRows =
    document.querySelector('#storeIngredientCountRows');


/* ==================== 공통 포맷 ==================== */

function formatNumber(value) {
    return value == null
        ? '—'
        : Number(value).toLocaleString('ko-KR');
}


function formatRate(value) {
    return value == null
        ? '—'
        : Number(value).toFixed(2) + '%';
}


/* ==================== 공통 테이블 처리 ==================== */

function createRow(...values) {

    const tr = document.createElement('tr');

    values.forEach(value => {

        const td = document.createElement('td');

        td.textContent = value ?? '—';

        tr.appendChild(td);

    });

    return tr;
}


function renderEmpty(tbody, colspan, message) {

    tbody.replaceChildren();

    const tr = document.createElement('tr');
    const td = document.createElement('td');

    td.colSpan = colspan;
    td.className = 'empty';
    td.textContent = message;

    tr.appendChild(td);
    tbody.appendChild(tr);
}


/* ==================== 공통 비동기 조회 ==================== */

function fetchStatistics(url, tbody, colspan, render) {

    fetch(url, {
        method: 'GET'
    })
        .then(response => {

            if (!response.ok) {
                throw new Error(
                    '통계 조회 실패: ' + response.status
                );
            }

            return response.json();

        })
        .then(data => {

            tbody.replaceChildren();

            if (data.length === 0) {

                renderEmpty(
                    tbody,
                    colspan,
                    '조회된 데이터가 없습니다.'
                );

                return;
            }

            data.forEach(row => {

                tbody.appendChild(render(row));

            });

        })
        .catch(error => {

            console.error(error);

            renderEmpty(
                tbody,
                colspan,
                '통계 조회에 실패했습니다.'
            );

        });
}


/* ==================== 1. 매장 식재료별 폐기 비용 ==================== */

costStore.addEventListener('change', () => {

    const storeId = costStore.value;

    if (!storeId) return;

    fetchStatistics(

        `${statisticsUrl}/stores/${storeId}/ingredient-costs`,

        storeIngredientPriceRows,

        3,

        row => createRow(
            row.ingredientName,
            formatNumber(row.totalDisposalQuantity),
            formatNumber(row.totalDisposalPrice) + '원'
        )

    );

});


/* ==================== 2. 매장 폐기 유형별 비율 ==================== */

typeStore.addEventListener('change', () => {

    const storeId = typeStore.value;

    if (!storeId) return;

    fetchStatistics(

        `${statisticsUrl}/stores/${storeId}/type-rates`,

        storeTypeRows,

        3,

        row => {

            const tr = createRow(
                row.disposalType,
                formatNumber(row.disposalCount) + '건'
            );

            const td = document.createElement('td');

            const span = document.createElement('span');

            span.textContent = formatRate(row.disposalRatePct);


            const rateBar = document.createElement('div');

            rateBar.className = 'rate-bar';


            const rateBarFill = document.createElement('div');

            rateBarFill.className = 'rate-bar-fill';

            rateBarFill.style.width =
                Math.max(
                    0,
                    Math.min(
                        100,
                        Number(row.disposalRatePct) || 0
                    )
                ) + '%';


            rateBar.appendChild(rateBarFill);

            td.appendChild(span);
            td.appendChild(rateBar);

            tr.appendChild(td);

            return tr;

        }

    );

});


/* ==================== 3. 매장 식재료별 수량 폐기율 ==================== */

ingredientStore.addEventListener('change', () => {

    const storeId = ingredientStore.value;

    if (!storeId) return;

    fetchStatistics(

        `${statisticsUrl}/stores/${storeId}/ingredient-quantity-rates`,

        storeIngredientQuantityRows,

        4,

        row => createRow(

            row.ingredientName,

            formatNumber(row.totalOrderQuantity)
            + (row.orderUnit ?? ''),

            formatNumber(row.totalDisposalQuantity)
            + (row.orderUnit ?? ''),

            formatRate(row.disposalRatePct)

        )

    );

});


/* ==================== 4. 매장 식재료별 건수 폐기율 ==================== */

ingredientCountStore.addEventListener('change', () => {

    const storeId = ingredientCountStore.value;

    if (!storeId) return;

    fetchStatistics(

        `${statisticsUrl}/stores/${storeId}/ingredient-count-rates`,

        storeIngredientCountRows,

        4,

        row => createRow(

            row.ingredientName,

            formatNumber(row.totalOrderCount),

            formatNumber(row.totalDisposalCount),

            formatRate(row.disposalRatePct)

        )

    );

});