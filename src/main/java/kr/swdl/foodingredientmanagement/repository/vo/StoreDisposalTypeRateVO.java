package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data
public class StoreDisposalTypeRateVO {
    private Long storeId;
    private String storeName;
    private String disposalType;
    private Long disposalCount;
    private Long totalDisposalCount;
    private double disposalRate;
    private double disposalRatePct;
}
