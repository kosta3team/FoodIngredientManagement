package kr.swdl.foodingredientmanagement.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : HqInventoryDetailResponseDTO
 * @since : 2026. 11. 25.
 */
@Data
@Builder
public class HQInventoryDetailResponseDTO {
    // 기본 정보
    private String storeName;
    private String ingredientName;
    private int currentQuantity;
    private String orderUnit;
    private int capacity;
    private String capacityUnit;
    private String storageType;
    private String managementMethod;
    private int expirationPeriod;
    private int consumptionPeriod;
    private int summerConsumptionPeriod;
    private String userName;

    // 입고 정보 (최근 입고 1건)
    private LocalDateTime manufactureAt;
    private LocalDateTime expirationDate;
    private LocalDateTime storeAt;
    private int orderQuantity;

    // 사용 이력
    private List<HQInventoryUsageResponseDTO> usageList;

}
