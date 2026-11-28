package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data
public class StoreIngredientDisposalQuantityRateVO {
    private Long storeId;
    private String storeName;
    private Long ingredientId;
    private String ingredientName;
    private String orderUnit;
    private Long totalOrderQuantity;
    private Long totalDisposalQuantity;
    private double disposalRatePct;
}
