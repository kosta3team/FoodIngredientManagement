package kr.swdl.foodingredientmanagement.hierarchicalTemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Set;

@Controller
public class FoodController {

    // 업무 화면은 templates 안에 한 벌만 둔다.
    // 기존 /hq/... 주소는 /templates/... 로 이동하며 Thymeleaf가 렌더링한다.

    // 기존 대시보드
    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/index.html";
    }

    // 본사 폐기 목록
    @GetMapping("/hq/disposal/list")
    public String hq_disposal_list() {
        return "redirect:/templates/disposal/hqDisposalList.html";
    }

    // 본사 폐기 상세
    @GetMapping("/hq/disposal/detail/{id}")
    public String hq_disposal_detail_id() {
        return "redirect:/templates/disposal/hqDisposalDetail.html";
    }

    // 본사 폐기 등록
    @GetMapping("/hq/disposal/request")
    public String hq_disposal_request() {
        return "redirect:/templates/disposal/hqDisposalRequest.html";
    }

    // 직영점 폐기 목록
    @GetMapping("/store/disposal/list")
    public String store_disposal_list() {
        return "redirect:/templates/disposal/storeDisposalList.html";
    }

    // 직영점 폐기 상세
    @GetMapping("/store/disposal/detail/{id}")
    public String store_disposal_detail_id() {
        return "redirect:/templates/disposal/storeDisposalDetail.html";
    }

    // 직영점 폐기 등록
    @GetMapping("/store/disposal/request")
    public String store_disposal_request() {
        return "redirect:/templates/disposal/storeDisposalRequest.html";
    }

    // 식재료 목록
    @GetMapping("/hq/ingredients/list")
    public String hq_ingredients_list() {
        return "redirect:/templates/ingredients/ingredientList.html";
    }

    // 식재료 상세
    @GetMapping("/hq/ingredients/detail/{id}")
    public String hq_ingredients_detail_id() {
        return "redirect:/templates/ingredients/ingredientDetail.html";
    }

    // 식재료 등록
    @GetMapping("/hq/ingredients/request")
    public String hq_ingredients_request() {
        return "redirect:/templates/ingredients/ingredientRequest.html";
    }

    // 재고 목록
    @GetMapping("/hq/inventory/list")
    public String hq_inventory_list() {
        return "redirect:/templates/inventory/inventoryList.html";
    }

    // 재고 상세
    @GetMapping("/hq/inventory/detail/{id}")
    public String hq_inventory_detail_id() {
        return "redirect:/templates/inventory/inventoryDetail.html";
    }

    // 본사 발주 목록
    @GetMapping("/hq/orders/list")
    public String hq_orders_list() {
        return "redirect:/templates/orders/hqOrderList.html";
    }

    // 본사 발주 상세
    @GetMapping("/hq/orders/detail/{id}")
    public String hq_orders_detail_id() {
        return "redirect:/templates/orders/hqOrderDetail.html";
    }

    // 직영점 발주 목록
    @GetMapping("/store/orders/list")
    public String store_orders_list() {
        return "redirect:/templates/store/orderList.html";
    }

    // 직영점 발주 상세
    @GetMapping("/store/orders/detail/{id}")
    public String store_orders_detail_id() {
        return "redirect:/templates/store/orderDetail.html";
    }

    // 직영점 발주 등록
    @GetMapping("/store/orders/request")
    public String store_orders_request() {
        return "redirect:/templates/store/orderRequest.html";
    }

    // 레시피 목록
    @GetMapping("/hq/recipes/list")
    public String hq_recipes_list() {
        return "redirect:/templates/recipes/recipeList.html";
    }

    // 레시피 상세
    @GetMapping("/hq/recipes/detail/{id}")
    public String hq_recipes_detail_id() {
        return "redirect:/templates/recipes/recipeDetail.html";
    }

    // 기존 사이드바 폐기 목록 - 기존 메뉴 경로 유지
    @GetMapping("/waste")
    public String menu_waste() {
        return "redirect:/templates/disposal/hqDisposalList.html";
    }

    // 기존 사이드바 폐기 등록 - 기존 메뉴 경로 유지
    @GetMapping("/waste/create")
    public String menu_waste_create() {
        return "redirect:/templates/disposal/hqDisposalRequest.html";
    }

    // 기존 사이드바 폐기 수정 화면 - 기존 메뉴 경로 유지
    @GetMapping("/waste/update")
    public String menu_waste_update() {
        return "redirect:/templates/disposal/hqDisposalDetail.html";
    }

    // 기존 사이드바 폐기 삭제 화면 - 기존 메뉴 경로 유지
    @GetMapping("/waste/delete")
    public String menu_waste_delete() {
        return "redirect:/templates/disposal/hqDisposalList.html";
    }

    // 기존 사이드바 재고 목록 - 기존 메뉴 경로 유지
    @GetMapping("/inventory")
    public String menu_inventory() {
        return "redirect:/templates/inventory/inventoryList.html";
    }

    // 기존 사이드바 재고 등록 관련 상세 - 기존 메뉴 경로 유지
    @GetMapping("/inventory/create")
    public String menu_inventory_create() {
        return "redirect:/templates/inventory/inventoryDetail.html";
    }

    // 기존 사이드바 재고 수정 관련 상세 - 기존 메뉴 경로 유지
    @GetMapping("/inventory/update")
    public String menu_inventory_update() {
        return "redirect:/templates/inventory/inventoryDetail.html";
    }

    // 기존 사이드바 재고 삭제 관련 목록 - 기존 메뉴 경로 유지
    @GetMapping("/inventory/delete")
    public String menu_inventory_delete() {
        return "redirect:/templates/inventory/inventoryList.html";
    }

    // 기존 사이드바 식재료 목록 - 기존 메뉴 경로 유지
    @GetMapping("/ingredients")
    public String menu_ingredients() {
        return "redirect:/templates/ingredients/ingredientList.html";
    }

    // 기존 사이드바 식재료 등록 - 기존 메뉴 경로 유지
    @GetMapping("/ingredients/create")
    public String menu_ingredients_create() {
        return "redirect:/templates/ingredients/ingredientRequest.html";
    }

    // 기존 사이드바 식재료 수정 관련 상세 - 기존 메뉴 경로 유지
    @GetMapping("/ingredients/update")
    public String menu_ingredients_update() {
        return "redirect:/templates/ingredients/ingredientDetail.html";
    }

    // 기존 사이드바 식재료 삭제 관련 목록 - 기존 메뉴 경로 유지
    @GetMapping("/ingredients/delete")
    public String menu_ingredients_delete() {
        return "redirect:/templates/ingredients/ingredientList.html";
    }

    // 기존 사이드바 발주 목록 - 기존 메뉴 경로 유지
    @GetMapping("/orders")
    public String menu_orders() {
        return "redirect:/templates/orders/hqOrderList.html";
    }

    // 기존 사이드바 발주 등록 - 기존 메뉴 경로 유지
    @GetMapping("/orders/create")
    public String menu_orders_create() {
        return "redirect:/templates/store/orderRequest.html";
    }

    // 기존 사이드바 발주 수정 관련 상세 - 기존 메뉴 경로 유지
    @GetMapping("/orders/update")
    public String menu_orders_update() {
        return "redirect:/templates/orders/hqOrderDetail.html";
    }

    // 기존 사이드바 발주 삭제 관련 목록 - 기존 메뉴 경로 유지
    @GetMapping("/orders/delete")
    public String menu_orders_delete() {
        return "redirect:/templates/orders/hqOrderList.html";
    }

    // 기존 사이드바 발주 승인 화면 - 기존 메뉴 경로 유지
    @GetMapping("/orders/approve")
    public String menu_orders_approve() {
        return "redirect:/templates/orders/hqOrderDetail.html";
    }

    // 파일 직접 미리보기의 상대경로와 Spring Boot의 URL을 일치시킨다.
    // 존재하는 화면만 열어 임의의 템플릿 파일에 접근할 수 없도록 한다.
    private static final Set<String> UI_PAGES = Set.of(
        "disposal/hqDisposalDetail",
        "disposal/hqDisposalList",
        "disposal/hqDisposalRequest",
        "disposal/storeDisposalDetail",
        "disposal/storeDisposalList",
        "disposal/storeDisposalRequest",
        "ingredients/ingredientDetail",
        "ingredients/ingredientList",
        "ingredients/ingredientRequest",
        "inventory/inventoryDetail",
        "inventory/inventoryList",
        "orders/hqOrderDetail",
        "orders/hqOrderList",
        "recipes/recipeDetail",
        "recipes/recipeList",
        "store/orderDetail",
        "store/orderList",
        "store/orderRequest"
    );

    @GetMapping("/templates/{section}/{page}.html")
    public String templatePage(@PathVariable String section, @PathVariable String page) {
        String view = section + "/" + page;
        if (!UI_PAGES.contains(view)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return view;
    }

    // 이전 /pages/... 주소로 접속한 경우에도 404 대신 새 URL로 이동시킨다.
    @GetMapping("/pages/{section}/{page}.html")
    public String legacyPage(@PathVariable String section, @PathVariable String page) {
        String view = section + "/" + page;
        if (!UI_PAGES.contains(view)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/templates/" + view + ".html";
    }
}
