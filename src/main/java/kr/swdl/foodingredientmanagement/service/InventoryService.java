package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.controller.request.HqInventorySearchRequestDTO;
import kr.swdl.foodingredientmanagement.controller.response.HqInventoryDetailResponseDTO;
import kr.swdl.foodingredientmanagement.controller.response.HqInventoryListResponseDTO;
import kr.swdl.foodingredientmanagement.repository.vo.InventoryListVO;
import kr.swdl.foodingredientmanagement.repository.vo.InventoryOrderVO;
import kr.swdl.foodingredientmanagement.repository.vo.InventoryUsageListVO;
import kr.swdl.foodingredientmanagement.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    // 본사 전체 재고 목록 조회
    public List<InventoryListVO> getInventoryList() {
        return inventoryRepository.getInventoryList(null, null, null, null, null, null);
    }

    // 본사 재고 목록 조회 (식재료명, 직영점명, 입고일시, 사용기한)
    public List<InventoryListVO> getInventoryList(String ingredientName,
                                                  String storeName,
                                                  LocalDate storeStartDate,
                                                  LocalDate storeEndDate,
                                                  LocalDate manageStartDate,
                                                  LocalDate manageEndDate) {
        // 직영점명 앞뒤 공백 제거, 빈 값이면 조건 제외
        ingredientName = trimToNull(ingredientName);
        storeName = trimToNull(storeName);

        return inventoryRepository.getInventoryList(ingredientName, storeName,
                storeStartDate, storeEndDate, manageStartDate, manageEndDate);
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        value = value.trim();
        return value.isEmpty() ? null : value; }

    // 직영점 재고 목록 조회 (분류, 식재료명 필터)
    public List<InventoryListVO> getStoreInventoryList(Long storeId,
                                                       String subCategoryName,
                                                       Long ingredientId) {
        return inventoryRepository.getStoreInventoryList(storeId, subCategoryName, ingredientId);
    }

    // 특정 직영점의 특정 재고 상세 조회
    public InventoryListVO getInventoryDetail(Long storeId, Long ingredientId) {
        return inventoryRepository.getInventoryDetail(storeId, ingredientId);
    }

    // 재고 입고 정보 조회
    public InventoryOrderVO getInventoryOrder(Long orderDetailId) {
        return inventoryRepository.getInventoryOrder(orderDetailId);
    }

    // 재고 사용 이력 조회
    public List<InventoryUsageListVO> getInventoryUsageList(Long storeId, Long ingredientId) {
        return inventoryRepository.getInventoryUsageList(storeId, ingredientId);
    }


    // 본사 재고 목록 검색
    public List<HqInventoryListResponseDTO> getHqInventoryList(HqInventorySearchRequestDTO request) {
        List<InventoryListVO> list = getInventoryList(
                request.getIngredientName(),
                request.getStoreName(),
                request.getStoreStartDate(),
                request.getStoreEndDate(),
                request.getManageStartDate(),
                request.getManageEndDate());

        return list.stream()
                .map(this::toHqInventoryListResponse)
                .toList();
    }

    private HqInventoryListResponseDTO toHqInventoryListResponse(InventoryListVO dto) {
        return HqInventoryListResponseDTO.builder()
                .storeName(dto.getStoreName())
                .ingredientName(dto.getIngredientName())
                .storageType(dto.getStorageType())
                .currentQuantity(dto.getCurrentQuantity())
                .orderUnit(dto.getOrderUnit())
                .storeAt(toLocalDateTime(dto.getStoreAt()))
                .consumptionAt(toLocalDateTime(dto.getConsumptionAt()))
                .userName(dto.getUserName())
                .storeId(dto.getStoreId())
                .ingredientId(dto.getIngredientId())
                .orderDetailId(dto.getOrderDetailId())
                .build();
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }





    // 본사 재고 상세 (화면용)!!!!!!!!!!!!!
    public HqInventoryDetailResponseDTO getHqInventoryDetail(Long storeId, Long ingredientId, Long orderDetailId) {
        InventoryListVO detail = getInventoryDetail(storeId, ingredientId);
        InventoryOrderVO latest = getInventoryOrder(orderDetailId);
        if (detail == null) return null;   // 없는 직영점/식재료

        List<HqInventoryDetailResponseDTO.UsageItem> usageList =
                getInventoryUsageList(storeId, ingredientId).stream()
                        .map(u -> HqInventoryDetailResponseDTO.UsageItem.builder()
                                .managementStartAt(toLocalDateTime(u.getManagementStartAt()))
                                .consumptionAt(toLocalDateTime(u.getConsumptionAt()))
                                .storeAt(toLocalDateTime(u.getStoreAt()))
                                .expirationAt(toLocalDateTime(u.getExpirationAt()))
                                .usageQuantity(u.getUsageQuantity())
                                .orderUnit(u.getOrderUnit())
                                .build())
                        .toList();

        return HqInventoryDetailResponseDTO.builder()
                .storeName(detail.getStoreName())
                .ingredientName(detail.getIngredientName())
                .storageType(detail.getStorageType())
                .managementMethod(detail.getManagementMethod())
                .currentQuantity(detail.getCurrentQuantity())
                .orderUnit(detail.getOrderUnit())
                .capacity(detail.getCapacity())
                .capacityUnit(detail.getCapacityUnit())
                .expirationPeriod(detail.getExpirationPeriod())
                .consumptionPeriod(detail.getConsumptionPeriod())
                .summerConsumptionPeriod(detail.getSummerConsumptionPeriod())
                .usageList(usageList)
                .build();
    }
}
