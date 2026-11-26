package kr.swdl.foodingredientmanagement.dto;

import lombok.Data;

@Data
public class OrderStatusDTO {
    private Long orderId;
    private Long orderDetailId;
    private String orderStatus;
    private String rejectReason;
}
