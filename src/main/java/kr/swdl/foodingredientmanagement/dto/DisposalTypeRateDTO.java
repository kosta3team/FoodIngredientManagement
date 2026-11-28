package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisposalTypeRateDTO {
    private String disposalType;
    private Long disposalCount;
    private Long totalDisposalCount;
    private double disposalRate;
    private double disposalRatePct;
}
