package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.controller.request.HQInventorySearchRequestDTO;
import kr.swdl.foodingredientmanagement.controller.response.HQInventoryDetailResponseDTO;
import kr.swdl.foodingredientmanagement.controller.response.HQInventoryListResponseDTO;
import kr.swdl.foodingredientmanagement.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class HQInventoryRestController {

    private final InventoryService inventoryService;

    @GetMapping("/list")
    public List<HQInventoryListResponseDTO> getHqInventoryList(@ModelAttribute HQInventorySearchRequestDTO request) {
        return inventoryService.getHqInventoryList(request);
    }

    // 본사 재고 상세
    @GetMapping("/detail")
    public ResponseEntity<HQInventoryDetailResponseDTO> getHqInventoryDetail(@RequestParam Long storeId,
                                                                             @RequestParam Long ingredientId,
                                                                             @RequestParam Long orderDetailId) {
        HQInventoryDetailResponseDTO detail = inventoryService.getHqInventoryDetail(storeId, ingredientId, orderDetailId);
        return detail == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(detail);
    }
}
