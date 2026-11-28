
package kr.swdl.foodingredientmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HQOrderController {

    // ================= 본사 발주 관리 =================

    @GetMapping("/hq/orders")
    public String hqOrderList() {
        return "order/hqOrderList";
    }

    @GetMapping("/hq/orders/detail")
    public String hqOrderDetail() {
        return "order/hqOrderDetail";
    }

    // ================= 직영점 발주 관리 =================
    @GetMapping("/store/orders")
    public String storeOrderList() {
        return "order/storeOrderList";
    }

    @GetMapping("/store/orders/detail")
    public String storeOrderDetail() {
        return "order/storeOrderDetail";
    }

    @GetMapping("/store/orders/request")
    public String storeOrderRequest() {
        return "order/storeOrderRequest";
    }

    @GetMapping("/store/orders/items/add")
    public String storeOrderDetailAdd() {
        return "order/storeOrderDetailAdd";
    }
}
