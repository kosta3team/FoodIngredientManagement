package kr.swdl.foodingredientmanagement.dto;


import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InventoryUsageListDTO {

    private Timestamp consumptionAt;
    private Timestamp managementStartAt;
    private int usageQuantity;
    private String orderUnit;

    private Timestamp expirationAt;
    private Timestamp manufactureAt;
    private Timestamp storeAt;
    private int orderQuantity;




}
