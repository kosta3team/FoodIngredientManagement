package kr.swdl.foodingredientmanagement.controller.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : StoreInventoryDetailResponseDTO
 * @since : 2026. 11. 27.
 */
@Data
@Builder
public class StoreInventoryDetailResponseDTO {
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

    // 사용 이력
    private List<StoreInventoryUsageResponseDTO> usageList;
}

