package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailListVO {
    private Long orderDetailId;
    private String ingredientName;
    private int orderQuantity;
    private String orderUnit;
    private int supplyPrice;
    private int orderPrice;
    private String orderStatus;
    private String rejectReason;

}
