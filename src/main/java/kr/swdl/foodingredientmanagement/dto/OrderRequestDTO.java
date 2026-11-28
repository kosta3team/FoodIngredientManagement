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
public class OrderRequestDTO {

    private Long orderId;
    private Timestamp orderAt;
    private Timestamp appAt;
    private Integer totalPrice;
    private Long userId;
    private Long storeId;

    private String storeName;
    private String firstIngredientName;
    private Long ingredientCount;
    private Timestamp storeAt;
    private String orderStatus;

    // 검색 조건
    private String startDate;
    private String endDate;
    private String dateType;
    
    private Integer size;
    private Integer page;
}
