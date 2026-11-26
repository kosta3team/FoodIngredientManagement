package kr.swdl.foodingredientmanagement.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : InventoryUsageResponseDTO
 * @since : 2026. 11. 26.
 */
@Data
@Builder
public class InventoryUsageResponseDTO {
    private LocalDateTime consumptionAt;      // 소비기한
    private LocalDateTime managementStartAt;  // 관리시작일시
    private int usageQuantity;                // 사용개수
    private String orderUnit;                 // 단위
}
