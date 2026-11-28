package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data
public class StoreOrderDisposalRateVO {
    private Long storeId;
    private String storeName;
    private int totalOrderCount;
    private int totalDisposalCount;
    private double disposalRatePct;
}
