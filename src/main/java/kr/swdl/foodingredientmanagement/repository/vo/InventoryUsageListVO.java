package kr.swdl.foodingredientmanagement.repository.vo;


import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InventoryUsageListVO {

    private Timestamp consumptionAt;
    private Timestamp managementStartAt;
    private int usageQuantity;
    private String orderUnit;

    private Timestamp expirationAt;
    private Timestamp manufactureAt;
    private Timestamp storeAt;
    private int orderQuantity;




}
