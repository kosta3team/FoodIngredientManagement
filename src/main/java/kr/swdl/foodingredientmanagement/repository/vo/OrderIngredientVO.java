package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data   
public class OrderIngredientVO {
    private Long ingredientId;
    private String ingredientName;
    private int supplyPrice;
    private int supplyQuantity;
}
