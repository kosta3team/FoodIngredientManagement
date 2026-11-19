package kr.swdl.foodingredientmanagement.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InventoryOrderResponseDTO {

    // 입고 정보
    private Timestamp manufactureAt;
    private Timestamp expirationDate;
    private Timestamp storeAt;
    private int orderQuantity;
    private String orderUnit;

    private Long orderDetailId;

}
