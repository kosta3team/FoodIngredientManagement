package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetailItemVO {
    private String ingredientName;
    private Integer orderQuantity;
    private String orderUnit;
    private Integer supplyPrice;
    private Integer orderPrice;
    private String userName;
}
