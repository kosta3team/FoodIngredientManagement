package kr.swdl.foodingredientmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/hqDisposalList")
    public String hq(){
        return "disposal/hqDisposalList";
    }

    @GetMapping("/storeDisposalList")
    public String store(){
        return "disposal/storeDisposalList";
    }

    @GetMapping("/hqDisposalStatistics")
    public String test(){
        return "disposal/hqDisposalStatistics";
    }

    @GetMapping("/ingredientList")
    public String ingredientList(){
        return "ingredient/ingredientList";
    }

    @GetMapping("/storeInventoryList")
    public String storeInventoryList(){
        return "inventory/storeInventoryList";
    }

    @GetMapping("/hqInventoryList")
    public String hqInventoryList(){
        return "inventory/hqInventoryList";
    }

    @GetMapping("/hqOrderDetail")
    public String hqOrderDetail(){
        return "order/hqOrderDetail";
    }

    @GetMapping("/hqOrderList")
    public String hqOrderList(){
        return "order/hqOrderList";
    }

    @GetMapping("/storeOrderDetail")
    public String storeOrderDSetail(){
        return "order/storeOrderDetail";
    }

    @GetMapping("/storeOrderDetailAdd")
    public String storeOrderDetailAdd(){
        return "order/storeOrderDetailAdd";
    }

    @GetMapping("/storeOrderList")
    public String sotreOrderList(){
        return "order/storeOrderList";
    }

    @GetMapping("/storeOrderRequest")
    public String storeOrderRequest(){
        return "order/storeOrderRequest";
    }
}
