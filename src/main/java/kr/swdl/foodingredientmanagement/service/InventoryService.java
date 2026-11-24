package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.dto.InventoryListDTO;
import kr.swdl.foodingredientmanagement.dto.InventoryOrderDTO;
import kr.swdl.foodingredientmanagement.dto.InventoryUsageListDTO;
import kr.swdl.foodingredientmanagement.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    // 본사 전체 재고 목록 조회
    public List<InventoryListDTO> getInventoryList() {
        return inventoryRepository.getInventoryList(null, null, null, null, null, null);
    }

    // 본사 재고 목록 조회 (식재료명, 직영점명, 입고일시, 사용기한)
    public List<InventoryListDTO> getInventoryList(Long ingredientId,
                                                    Long storeId,
                                                    LocalDate storeStartDate,
                                                    LocalDate storeEndDate,
                                                    LocalDate manageStartDate,
                                                    LocalDate manageEndDate) {
        return inventoryRepository.getInventoryList(ingredientId, storeId,
                storeStartDate, storeEndDate, manageStartDate, manageEndDate);
    }

    // 직영점 재고 목록 조회 (분류, 식재료명 필터)
    public List<InventoryListDTO> getStoreInventoryList(Long storeId,
                                                         String subCategoryName,
                                                         Long ingredientId) {
        return inventoryRepository.getStoreInventoryList(storeId, subCategoryName, ingredientId);
    }

    // 특정 직영점의 특정 재고 상세 조회
    public InventoryListDTO getInventoryDetail(Long storeId, Long ingredientId) {
        return inventoryRepository.getInventoryDetail(storeId, ingredientId);
    }

    // 재고 입고 정보 조회
    public InventoryOrderDTO getInventoryOrder(Long orderDetailId) {
        return inventoryRepository.getInventoryOrder(orderDetailId);
    }

    // 재고 사용 이력 조회
    public List<InventoryUsageListDTO> getInventoryUsageList(Long storeId, Long ingredientId) {
        return inventoryRepository.getInventoryUsageList(storeId, ingredientId);
    }

    // 검색 조건용 식재료명 목록 조회
    public List<InventoryListDTO> getIngredientNameList() {
        return inventoryRepository.getIngredientNameList();
    }
}
