package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.controller.request.StoreInventorySearchRequestDTO;
import kr.swdl.foodingredientmanagement.controller.response.StoreInventoryDetailResponseDTO;
import kr.swdl.foodingredientmanagement.controller.response.StoreInventoryListResponseDTO;
import kr.swdl.foodingredientmanagement.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 직영점 재고 상세
    @GetMapping("/detail")
    public ResponseEntity<StoreInventoryDetailResponseDTO> getStoreInventoryDetail(@RequestParam Long ingredientId) {
        StoreInventoryDetailResponseDTO detail = inventoryService.getStoreInventoryDetail(ingredientId);
        return detail == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(detail);
    }
}
