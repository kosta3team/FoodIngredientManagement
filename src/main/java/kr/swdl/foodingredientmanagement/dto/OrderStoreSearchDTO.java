package kr.swdl.foodingredientmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderStoreSearchDTO {
    private Long storeId;
    private Long orderId;

    private String startDate;
    private String endDate;
    private String dateType;
    private String orderStatus;

    private Integer page = 1;
    private Integer size = 8;
}
