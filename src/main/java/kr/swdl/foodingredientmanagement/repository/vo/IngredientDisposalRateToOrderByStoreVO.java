package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data
public class IngredientDisposalRateToOrderByStoreVO {
    private Long storeId;
    private String storeName;
    private Long ingredientId;
    private String ingredientName;
    private int totalOrderCount;
    private int totalDisposalCount;
    private double disposalRatePct;
}
