package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.controller.request.HqInventorySearchRequestDTO;
import kr.swdl.foodingredientmanagement.controller.response.HqInventoryDetailResponseDTO;
import kr.swdl.foodingredientmanagement.controller.response.HqInventoryListResponseDTO;
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
public class InventoryRestController {

    private final InventoryService inventoryService;

    @GetMapping("/list")
    public List<HqInventoryListResponseDTO> getHqInventoryList(@ModelAttribute HqInventorySearchRequestDTO request) {
        return inventoryService.getHqInventoryList(request);
    }

    // 본사 재고 상세
    @GetMapping("/detail")
    public ResponseEntity<HqInventoryDetailResponseDTO> getHqInventoryDetail(@RequestParam Long storeId,
                                                                             @RequestParam Long ingredientId,
                                                                             @RequestParam Long orderDetailId) {
        HqInventoryDetailResponseDTO detail = inventoryService.getHqInventoryDetail(storeId, ingredientId, orderDetailId);
        return detail == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(detail);
    }
}
