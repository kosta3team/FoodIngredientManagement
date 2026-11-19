package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data
public class OrderDetailVO {
    private String ingredientName;
    private Integer orderQuantity;
    private String orderUnit;
    private Integer supplyPrice;
    private Integer orderPrice;
    private String userName;
    private String orderStatus;
    private String rejectReason;
}
