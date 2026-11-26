package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data
public class OrderStatusVO {
    private Long orderId;
    private Long orderDetailId;
    private String orderStatus;
}
