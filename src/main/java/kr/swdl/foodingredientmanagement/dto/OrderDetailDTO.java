package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class OrderDetailDTO {
    private Long orderDetailId;
    private int orderQuantity;
    private int supplyPrice;
    private Timestamp manufactureAt;
    private Timestamp storeAt;
    private Timestamp appAt;
    private String orderStatus;
    private int orderPrice;
    private String rejectReason;
    private int currentQuantity;
    private Long orderId;
    private Long ingredientId;
}
