package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data
public class DisposalCostByIngredientVO {
    private Long ingredientId;
    private String ingredientName;
    private int totalDisposalPrice;
    private int totalDisposalQuantity;
}
