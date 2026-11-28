package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreIngredientDisposalQuantityRateDTO {
    private Long storeId;
    private String storeName;
    private Long ingredientId;
    private String ingredientName;
    private String orderUnit;
    private Long totalOrderQuantity;
    private Long totalDisposalQuantity;
    private double disposalRatePct;
}
