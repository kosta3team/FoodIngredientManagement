package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDisposalTypeRateDTO {
    private Long storeId;
    private String storeName;
    private String disposalType;
    private Long disposalCount;
    private Long totalDisposalCount;
    private double disposalRate;
    private double disposalRatePct;
}
