package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderInfoVO {
    private Long orderId;
    private Timestamp orderAt;
    private String storeName;
    private Timestamp appAt;
    private Timestamp storeAt;
    private Integer totalPrice;
    private String userName;
}
