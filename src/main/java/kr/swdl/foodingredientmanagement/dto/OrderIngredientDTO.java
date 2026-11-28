package kr.swdl.foodingredientmanagement.dto;

import lombok.Data;

@Data
public class OrderIngredientDTO {

    private Long ingredientId;
    private String ingredientName;
    private Integer supplyPrice;
}