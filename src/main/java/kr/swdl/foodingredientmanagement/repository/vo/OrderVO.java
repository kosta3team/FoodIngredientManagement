package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderVO {
    private Long orderId;
    private Timestamp orderAt;
    private String storeName;
    private String firstIngredientName;
    private Long ingredientCount;
    private Integer totalPrice;
    private Timestamp storeAt;
}
