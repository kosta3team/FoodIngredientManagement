package kr.swdl.foodingredientmanagement.hierarchicalTemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FoodController {
    // 대시보드 - 기존 화면 경로
    @GetMapping("/dashboard")
    public String dashboard() {

        return "dashboard";
    }

    // 발주 목록 조회 - 본사
    @GetMapping("/orders/list")
    public String hqOrderList() { return "orders/hqOrderList"; }

    // 발주 상세 조회 - 본사
    @GetMapping("/orders/detail/{id}")
    public String hqOrderDetail(@PathVariable String id) { return "orders/hqOrderDetail"; }

    // 발주 요청 - 직영점
    @GetMapping("/store/orders/request")
    public String storeOrderRequest() { return "store/orderRequest"; }

    // 발주 요청 수정 화면 - 직영점
    @GetMapping("/store/orders/request/{id}")
    public String storeOrderEdit(@PathVariable String id) { return "store/orderRequest"; }

    // 발주 목록 조회 - 직영점
    @GetMapping("/store/orders/list")
    public String storeOrderList() { return "store/orderList"; }

    // 발주 상세 조회 - 직영점
    @GetMapping("/store/orders/detail/{id}")
    public String storeOrderDetail(@PathVariable String id) { return "store/orderDetail"; }

    // 재고 목록 조회 - 본사
    @GetMapping("/inventory/list")
    public String inventoryList() { return "inventory/inventoryList"; }

    // 재고 상세 조회 - 본사
    @GetMapping("/inventory/detail/{id}")
    public String inventoryDetail(@PathVariable Long id) { return "inventory/inventoryDetail"; }

    // 식재료 목록 조회 - 본사
    @GetMapping("/ingredients/list")
    public String ingredientList() { return "ingredients/ingredientList"; }

    // 식재료 상세 조회 - 본사
    @GetMapping("/ingredients/detail/{id}")
    public String ingredientDetail(@PathVariable String id) { return "ingredients/ingredientDetail"; }

    // 식재료 등록 화면 - 본사
    @GetMapping("/ingredients/request")
    public String ingredientRequest() { return "ingredients/ingredientRequest"; }

    // 폐기 목록 조회 - 직영점
    @GetMapping("/store/disposal/list")
    public String storeDisposalList() { return "disposal/storeDisposalList"; }

    // 폐기 상세 조회 - 직영점
    @GetMapping("/store/disposal/detail/{id}")
    public String storeDisposalDetail(@PathVariable String id) { return "disposal/storeDisposalDetail"; }

    // 폐기 등록 화면 - 직영점
    @GetMapping("/store/disposal/request")
    public String storeDisposalRequest() { return "disposal/storeDisposalRequest"; }

    // 폐기 목록 조회 - 본사
    @GetMapping("/hq/disposal/list")
    public String hqDisposalList() { return "disposal/hqDisposalList"; }

    // 폐기 상세 조회 - 본사
    @GetMapping("/hq/disposal/detail/{id}")
    public String hqDisposalDetail(@PathVariable Long id) { return "disposal/hqDisposalDetail"; }

    // 폐기 등록 화면 - 본사
    @GetMapping("/hq/disposal/request")
    public String hqDisposalRequest() { return "disposal/hqDisposalRequest"; }
}
