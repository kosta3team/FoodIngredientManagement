package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.dto.InventoryListDTO;
import kr.swdl.foodingredientmanagement.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : InventoryController
 * @since : 2026. 11. 25.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryRestController {

    private final InventoryService inventoryService;

    // 검색 조건용 식재료명 목록
    @GetMapping("/ingredients")
    public List<InventoryListDTO> getIngredientNameList() {
        return inventoryService.getIngredientNameList();
    }

}
