package kr.swdl.foodingredientmanagement.hierarchicalTemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FoodController {

    // 대시 보드 (static/index.html)
    @GetMapping({"/", "dashboard"})
    public String dashboard(){return "redirect:/index.html";}

    // 본사 발주 요청 조회
    @GetMapping("/orders/list")
    public String hqOrderList(){return "orders/hqOrderList";}

    // 본사 발주 요청 상세 조회
    @GetMapping("/orders/detail/{id}")
    public String hqOrderDetail(@PathVariable String id){return "orders/hqOrderDetail";}

    // 직영점 발주 요청
    @GetMapping("/store/orders/request")
    public String storeOrderRequest(){return "store/orderRequest";}

    // 직영점 발주 요청 수정
    @GetMapping("/store/orders/request/{id}")
    public String storeOrderEdit(@PathVariable String id){return "store/orderRequest";}

    // 직영점 발주 요청 조회
    @GetMapping("/store/orders/list")
    public String storeOrderList(){return "store/orderList";}

    // 직영점 발주 요청 상세 조회
    @GetMapping("store/orders/detail/{id}")
    public String storeOrderDetail(@PathVariable String id){return "store/orderDetail";}

    // 재고 조회 - 현재는 본사만. 추후에 직영점 재고확인도 필요
    @GetMapping("/inventory/list")
    public String inventoryList(){return "inventory/inventoryList";}

    // 재고 상세 조회 - 현재는 본사만. 추후에 직영점 재고상세확인도 필요
    @GetMapping("/inventory/detail/{id}")
    public String inventoryDetail(@PathVariable Long id){return "inventory/inventoryDetail";}

    // 식재료 목록 조회
    @GetMapping("/ingredients/list")
    public String ingredientList(){return "ingredients/ingredientList";}

    // 식재료 목록 상세 조회
    @GetMapping("/ingredients/detail/{id}")
    public String ingredientDetail(@PathVariable String id){return "ingredients/ingredientDetail";}

    // 식재료 등록
    @GetMapping("/ingredients/request")
    public String ingredientRequest(){return "ingredients/ingredientRequest";}

    // 폐기 목록 조회 - 직영점
    @GetMapping("/store/disposal/list")
    public String storeDisposalList(){return "disposal/storeDisposalList";}

    // 폐기 목록 상세 조회 - 직영점
    @GetMapping("/store/disposal/detail/{id}")
    public String storeDisposalDetail(@PathVariable String id){return "disposal/storeDisposalDetail";}

    // 폐기 등록 - 직영점
    @GetMapping("/store/disposal/request")
    public String storeDisposalRequest(){return "disposal/storeDisposalRequest";}


    // 폐기 조회 - 본사
    @GetMapping("/hq/disposal/list")
    public String hqDisposalList(){return "disposal/hqdisposalList";}

    // 폐기 상세 조회 - 본사
    @GetMapping("/hq/disposal/detail/{id}")
    public String hqDisposalDetail(@PathVariable Long id){return "disposal/hqDisposalDetail";}

    // 폐기 등록 - 본사
    @GetMapping("/hq/disposal/request")
    public String hqDisposalRequest(){return "disposal/hqDisposalRequest";}
}
