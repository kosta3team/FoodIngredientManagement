package kr.swdl.foodingredientmanagement.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : StoreInventoryUsageResponseDTO
 * @since : 2026. 11. 27.
 */
@Data
@Builder
public class StoreInventoryUsageResponseDTO {
    private LocalDateTime consumptionAt;      // 소비기한
    private LocalDateTime managementStartAt;  // 관리시작일시
    private LocalDateTime expirationAt;       // 유통기한
    private LocalDateTime manufactureAt;      // 제조일자
    private LocalDateTime storeAt;            // 입고일자
    private int orderQuantity;                // 입고수량
    private int usageQuantity;                // 사용개수
    private String orderUnit;                 // 단위
}
