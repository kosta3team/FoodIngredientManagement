package kr.swdl.foodingredientmanagement.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : HqInventoryListResponseDTO
 * @since : 2026. 11. 25.
 */
@Data
@Builder
public class HqInventoryListResponseDTO {
    private String storeName;
    private String ingredientName;
    private String storageType;
    private int currentQuantity;
    private String orderUnit;
    private LocalDateTime storeAt;
    private LocalDateTime consumptionAt;
    private String userName;

    private Long storeId;
    private Long ingredientId;
    private Long orderDetailId;
}
