package kr.swdl.foodingredientmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchDTO {

    private String searchType;
    private String searchKeyword;

    private String storeName;
    private Long orderId;

    private String startDate;
    private String endDate;

    private String orderStatus;

    private Integer page = 1;
    private Integer size = 10;
}
