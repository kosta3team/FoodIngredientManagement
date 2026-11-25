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
public class HqInventoryDetailResponseDTO {
    // 기본 정보
    private String storeName;
    private String ingredientName;
    private String storageType;
    private String managementMethod;
    private int currentQuantity;
    private String orderUnit;
    private int capacity;
    private String capacityUnit;
    private int expirationPeriod;
    private int consumptionPeriod;
    private int summerConsumptionPeriod;

    // 사용 이력
    private List<UsageItem> usageList;
    @Data
    @Builder
    public static class UsageItem {
        private LocalDateTime managementStartAt;
        private LocalDateTime consumptionAt;
        private LocalDateTime storeAt;
        private LocalDateTime expirationAt;
        private int usageQuantity;
        private String orderUnit;
    }
}
