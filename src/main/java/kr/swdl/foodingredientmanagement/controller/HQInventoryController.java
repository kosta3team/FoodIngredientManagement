package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.controller.request.HQInventorySearchRequestDTO;
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
 * @fileName : HQInventoryController
 * @since : 2026. 11. 26.
 */
@Controller
@RequiredArgsConstructor
public class HQInventoryController {
    private final InventoryService inventoryService;

    // 본사 재고 목록 화면
    @GetMapping("/hqInventoryList")
    public String hqInventoryList(Model model,
                                  @ModelAttribute("filter") HQInventorySearchRequestDTO filter) {
        model.addAttribute("list", inventoryService.getHqInventoryList(filter));
        return "inventory/hqInventoryList";
    }
}
