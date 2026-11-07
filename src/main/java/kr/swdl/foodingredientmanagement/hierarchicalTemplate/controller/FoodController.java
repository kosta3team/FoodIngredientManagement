package kr.swdl.foodingredientmanagement.hierarchicalTemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FoodController {

    // UI 스켈레톤: GET 경로는 한 벌의 정적 HTML로 리다이렉트한다.
    // 추후 DB/Thymeleaf 연결 시 컨트롤러와 템플릿 구성을 교체할 수 있다.

    // 기존 대시보드
    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/index.html";
    }

    // 본사 폐기 목록
    @GetMapping("/hq/disposal/list")
    public String hq_disposal_list() {
        return "redirect:/pages/disposal/hqDisposalList.html";
    }

    // 본사 폐기 상세
    @GetMapping("/hq/disposal/detail/{id}")
    public String hq_disposal_detail_id() {
        return "redirect:/pages/disposal/hqDisposalDetail.html";
    }

    // 본사 폐기 등록
    @GetMapping("/hq/disposal/request")
    public String hq_disposal_request() {
        return "redirect:/pages/disposal/hqDisposalRequest.html";
    }

    // 직영점 폐기 목록
    @GetMapping("/store/disposal/list")
    public String store_disposal_list() {
        return "redirect:/pages/disposal/storeDisposalList.html";
    }

    // 직영점 폐기 상세
    @GetMapping("/store/disposal/detail/{id}")
    public String store_disposal_detail_id() {
        return "redirect:/pages/disposal/storeDisposalDetail.html";
    }

    // 직영점 폐기 등록
    @GetMapping("/store/disposal/request")
    public String store_disposal_request() {
        return "redirect:/pages/disposal/storeDisposalRequest.html";
    }

    // 식재료 목록
    @GetMapping("/hq/ingredients/list")
    public String hq_ingredients_list() {
        return "redirect:/pages/ingredients/ingredientList.html";
    }

    // 식재료 상세
    @GetMapping("/hq/ingredients/detail/{id}")
    public String hq_ingredients_detail_id() {
        return "redirect:/pages/ingredients/ingredientDetail.html";
    }

    // 식재료 등록
    @GetMapping("/hq/ingredients/request")
    public String hq_ingredients_request() {
        return "redirect:/pages/ingredients/ingredientRequest.html";
    }

    // 재고 목록
    @GetMapping("/hq/inventory/list")
    public String hq_inventory_list() {
        return "redirect:/pages/inventory/inventoryList.html";
    }

    // 재고 상세
    @GetMapping("/hq/inventory/detail/{id}")
    public String hq_inventory_detail_id() {
        return "redirect:/pages/inventory/inventoryDetail.html";
    }

    // 본사 발주 목록
    @GetMapping("/hq/orders/list")
    public String hq_orders_list() {
        return "redirect:/pages/orders/hqOrderList.html";
    }

    // 본사 발주 상세
    @GetMapping("/hq/orders/detail/{id}")
    public String hq_orders_detail_id() {
        return "redirect:/pages/orders/hqOrderDetail.html";
    }

    // 직영점 발주 목록
    @GetMapping("/store/orders/list")
    public String store_orders_list() {
        return "redirect:/pages/store/orderList.html";
    }

    // 직영점 발주 상세
    @GetMapping("/store/orders/detail/{id}")
    public String store_orders_detail_id() {
        return "redirect:/pages/store/orderDetail.html";
    }

    // 직영점 발주 등록
    @GetMapping("/store/orders/request")
    public String store_orders_request() {
        return "redirect:/pages/store/orderRequest.html";
    }

    // 레시피 목록
    @GetMapping("/hq/recipes/list")
    public String hq_recipes_list() {
        return "redirect:/pages/recipes/recipeList.html";
    }

    // 레시피 상세
    @GetMapping("/hq/recipes/detail/{id}")
    public String hq_recipes_detail_id() {
        return "redirect:/pages/recipes/recipeDetail.html";
    }

    // 기존 사이드바 폐기 목록 - 기존 메뉴 경로 유지
    @GetMapping("/waste")
    public String menu_waste() {
        return "redirect:/pages/disposal/hqDisposalList.html";
    }

    // 기존 사이드바 폐기 등록 - 기존 메뉴 경로 유지
    @GetMapping("/waste/create")
    public String menu_waste_create() {
        return "redirect:/pages/disposal/hqDisposalRequest.html";
    }

    // 기존 사이드바 폐기 수정 화면 - 기존 메뉴 경로 유지
    @GetMapping("/waste/update")
    public String menu_waste_update() {
        return "redirect:/pages/disposal/hqDisposalDetail.html";
    }

    // 기존 사이드바 폐기 삭제 화면 - 기존 메뉴 경로 유지
    @GetMapping("/waste/delete")
    public String menu_waste_delete() {
        return "redirect:/pages/disposal/hqDisposalList.html";
    }

    // 기존 사이드바 재고 목록 - 기존 메뉴 경로 유지
    @GetMapping("/inventory")
    public String menu_inventory() {
        return "redirect:/pages/inventory/inventoryList.html";
    }

    // 기존 사이드바 재고 등록 관련 상세 - 기존 메뉴 경로 유지
    @GetMapping("/inventory/create")
    public String menu_inventory_create() {
        return "redirect:/pages/inventory/inventoryDetail.html";
    }

    // 기존 사이드바 재고 수정 관련 상세 - 기존 메뉴 경로 유지
    @GetMapping("/inventory/update")
    public String menu_inventory_update() {
        return "redirect:/pages/inventory/inventoryDetail.html";
    }

    // 기존 사이드바 재고 삭제 관련 목록 - 기존 메뉴 경로 유지
    @GetMapping("/inventory/delete")
    public String menu_inventory_delete() {
        return "redirect:/pages/inventory/inventoryList.html";
    }

    // 기존 사이드바 식재료 목록 - 기존 메뉴 경로 유지
    @GetMapping("/ingredients")
    public String menu_ingredients() {
        return "redirect:/pages/ingredients/ingredientList.html";
    }

    // 기존 사이드바 식재료 등록 - 기존 메뉴 경로 유지
    @GetMapping("/ingredients/create")
    public String menu_ingredients_create() {
        return "redirect:/pages/ingredients/ingredientRequest.html";
    }

    // 기존 사이드바 식재료 수정 관련 상세 - 기존 메뉴 경로 유지
    @GetMapping("/ingredients/update")
    public String menu_ingredients_update() {
        return "redirect:/pages/ingredients/ingredientDetail.html";
    }

    // 기존 사이드바 식재료 삭제 관련 목록 - 기존 메뉴 경로 유지
    @GetMapping("/ingredients/delete")
    public String menu_ingredients_delete() {
        return "redirect:/pages/ingredients/ingredientList.html";
    }

    // 기존 사이드바 발주 목록 - 기존 메뉴 경로 유지
    @GetMapping("/orders")
    public String menu_orders() {
        return "redirect:/pages/orders/hqOrderList.html";
    }

    // 기존 사이드바 발주 등록 - 기존 메뉴 경로 유지
    @GetMapping("/orders/create")
    public String menu_orders_create() {
        return "redirect:/pages/store/orderRequest.html";
    }

    // 기존 사이드바 발주 수정 관련 상세 - 기존 메뉴 경로 유지
    @GetMapping("/orders/update")
    public String menu_orders_update() {
        return "redirect:/pages/orders/hqOrderDetail.html";
    }

    // 기존 사이드바 발주 삭제 관련 목록 - 기존 메뉴 경로 유지
    @GetMapping("/orders/delete")
    public String menu_orders_delete() {
        return "redirect:/pages/orders/hqOrderList.html";
    }

    // 기존 사이드바 발주 승인 화면 - 기존 메뉴 경로 유지
    @GetMapping("/orders/approve")
    public String menu_orders_approve() {
        return "redirect:/pages/orders/hqOrderDetail.html";
    }
}
