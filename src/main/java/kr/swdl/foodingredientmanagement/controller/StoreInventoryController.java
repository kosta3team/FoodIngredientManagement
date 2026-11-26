package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.controller.request.StoreInventorySearchRequestDTO;
import kr.swdl.foodingredientmanagement.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : StoreInventoryController
 * @since : 2026. 11. 26.
 */
@Controller
@RequiredArgsConstructor
public class StoreInventoryController {
    private final InventoryService inventoryService;

    // 직영점 재고 목록 화면
    @GetMapping("/storeInventoryList")
    public String storeInventoryList(Model model, @ModelAttribute("filter") StoreInventorySearchRequestDTO filter){
        model.addAttribute("list", inventoryService.getStoreInventoryList(filter));
        model.addAttribute("subCategoryList", inventoryService.getSubCategoryList());
        return "inventory/storeInventoryList";
    }
}
