package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProgressVO {
    private Long orderId;
    private Timestamp orderAt;
    private Integer totalPrice;
    private String orderStatus;
    private Timestamp appAt;
    private Timestamp storeAt;
}

