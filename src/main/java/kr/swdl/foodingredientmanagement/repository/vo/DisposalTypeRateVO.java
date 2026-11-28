package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data
public class DisposalTypeRateVO {
    private String disposalType;
    private Long disposalCount;
    private Long totalDisposalCount;
    private double disposalRate;
    private double disposalRatePct;
}
