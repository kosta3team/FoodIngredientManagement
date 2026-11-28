package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data
public class IngredientDisposalQuantityRateVO {
    private Long ingredientId;
    private String ingredientName;
    private String orderUnit;
    private Long totalOrderQuantity;
    private Long totalDisposalQuantity;
    private double disposalRatePct;
}
