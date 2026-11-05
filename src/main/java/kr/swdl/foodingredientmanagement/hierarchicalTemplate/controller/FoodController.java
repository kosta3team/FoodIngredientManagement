package kr.swdl.foodingredientmanagement.hierarchicalTemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** 화면 연결 전용. 데이터는 FoodRestController의 AJAX API가 담당한다. */
@Controller
public class FoodController {

    // 팀원이 만든 static/index.html 대시보드를 그대로 사용한다.
    @GetMapping({"/", "/dashboard"})
    public String dashboard() { return "redirect:/index.html"; }

    @GetMapping("/orders/list")
    public String hqOrderList() { return "orders/hqOrderList"; }

    @GetMapping("/orders/detail/{id}")
    public String hqOrderDetail(@PathVariable String id) { return "orders/hqOrderDetail"; }

    @GetMapping("/store/orders/request")
    public String storeOrderRequest() { return "store/orderRequest"; }

    @GetMapping("/store/orders/request/{id}")
    public String storeOrderEdit(@PathVariable String id) { return "store/orderRequest"; }

    @GetMapping("/store/orders/list")
    public String storeOrderList() { return "store/orderList"; }

    @GetMapping("/store/orders/detail/{id}")
    public String storeOrderDetail(@PathVariable String id) { return "store/orderDetail"; }

    @GetMapping("/inventory/list")
    public String inventoryList() { return "inventory/inventoryList"; }

    @GetMapping("/inventory/detail/{id}")
    public String inventoryDetail(@PathVariable Long id) { return "inventory/inventoryDetail"; }

    @GetMapping("/ingredients/list")
    public String ingredientList() { return "ingredients/ingredientList"; }

    @GetMapping("/ingredients/request")
    public String ingredientRequest() { return "ingredients/ingredientRequest"; }

    @GetMapping("/ingredients/detail/{id}")
    public String ingredientDetail(@PathVariable String id) { return "ingredients/ingredientDetail"; }

    @GetMapping("/store/disposal/list")
    public String storeDisposalList() { return "disposal/storeDisposalList"; }

    @GetMapping("/store/disposal/detail/{id}")
    public String storeDisposalDetail(@PathVariable Long id) { return "disposal/storeDisposalDetail"; }

    @GetMapping("/store/disposal/request")
    public String storeDisposalRequest() { return "disposal/storeDisposalRequest"; }

    @GetMapping("/hq/disposal/list")
    public String hqDisposalList() { return "disposal/hqDisposalList"; }

    @GetMapping("/hq/disposal/detail/{id}")
    public String hqDisposalDetail(@PathVariable Long id) { return "disposal/hqDisposalDetail"; }

    @GetMapping("/hq/disposal/request")
    public String hqDisposalRequest() { return "disposal/hqDisposalRequest"; }
}
