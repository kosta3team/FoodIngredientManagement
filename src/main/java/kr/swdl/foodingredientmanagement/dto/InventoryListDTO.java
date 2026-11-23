package kr.swdl.foodingredientmanagement.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InventoryListDTO {
    private String storeName;

    private String ingredientName;
    private String storageType;
    private String orderUnit;

    private int currentQuantity;
    private Timestamp storeAt;

    private Timestamp managementStartAt;

    private String userName;
    private String subCategoryName;



    private String managementMethod;


    private int capacity;

    private String capacityUnit;
    private int expirationPeriod;
    private int consumptionPeriod;
    private int summerConsumptionPeriod;


    private Long orderDetailId;



    private Timestamp consumptionAt;

    private Long storeId;
    private Long IngredientId;



}
