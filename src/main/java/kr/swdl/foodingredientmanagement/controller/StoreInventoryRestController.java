package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.controller.request.StoreInventorySearchRequestDTO;
import kr.swdl.foodingredientmanagement.controller.response.StoreInventoryListResponseDTO;
import kr.swdl.foodingredientmanagement.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : StoreInventoryRestController
 * @since : 2026. 11. 26.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store/inventory")
public class StoreInventoryRestController {

    private final InventoryService inventoryService;

    @GetMapping("/list")
    public List<StoreInventoryListResponseDTO> getStoreInventoryList(@ModelAttribute StoreInventorySearchRequestDTO request){
        return inventoryService.getStoreInventoryList(request);
    }
}
