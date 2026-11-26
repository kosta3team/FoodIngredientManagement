package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListVO {

    private Long orderId;
    private Timestamp orderAt;
    private String ingredientName;
    private Integer ingredientCount;
    private Integer totalPrice;
    private String orderStatus;
    private Timestamp storeAt;
}
