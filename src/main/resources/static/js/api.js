// MariaDB 데이터는 Controller의 REST API를 통해 조회한다.
// 추후 Service/Repository를 추가해도 HTML의 요청 URL은 유지할 수 있다.
async function apiRequest(url, method, data) {
    const options = { method: method || "GET", headers: {} };
    if (data !== undefined) {
        options.headers["Content-Type"] = "application/json";
        options.body = JSON.stringify(data);
    }
    const response = await fetch(url, options);
    if (!response.ok) {
        let message = "HTTP " + response.status;
        try {
            const error = await response.json();
            message = error.message || message;
        } catch (ignored) { /* JSON이 아닌 오류 응답은 HTTP 코드로 표시한다. */ }
        throw new Error(message);
    }
    if (response.status === 204) return null;
    return response.json();
}

function formatMoney(number) {
    return Number(number || 0).toLocaleString("ko-KR") + "원";
}

function orderStatusName(status) {
    return {
        REQUESTED: "요청접수", PENDING: "대기",
        APPROVED: "승인", REJECTED: "거절"
    }[status] || status;
}

function orderStatusBadge(status) {
    return {
        REQUESTED: "text-bg-secondary", PENDING: "text-bg-warning",
        APPROVED: "text-bg-success", REJECTED: "text-bg-danger"
    }[status] || "text-bg-secondary";
}

function orderAmount(order) {
    return (order.items || []).reduce(function (total, item) {
        return total + Number(item.price) * Number(item.quantity);
    }, 0);
}

function orderSummary(order) {
    if (!order.items || !order.items.length) return "품목 없음";
    const first = order.items[0];
    return first.name + (order.items.length > 1 ? " 외 " + (order.items.length - 1) + "건" : "");
}

function addTextCell(row, value, className) {
    const cell = document.createElement("td");
    cell.textContent = value == null ? "" : value;
    if (className) cell.className = className;
    row.appendChild(cell);
    return cell;
}
