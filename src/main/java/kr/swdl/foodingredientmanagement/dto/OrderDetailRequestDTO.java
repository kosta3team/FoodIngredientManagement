package kr.swdl.foodingredientmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequestDTO {
    private Long orderDetailId;

    private Long ingredientId;

    private Integer orderQuantity;

    private Integer supplyPrice;

    private Integer orderPrice;

    private Timestamp manufactureAt;

    private Timestamp storeAt;

    private Timestamp appAt;

    private String orderStatus;

    private String rejectReason;

    private Integer currentQuantity;

    private Long orderId;
}
