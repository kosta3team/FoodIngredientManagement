package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HqDisposalSerachFilterDTO {
    private String storeName;
    private String ingredientName;
    private String disposalStatus;
    private String startDate;
    private String endDate;
    private String disposalType;
    private String processingMethod;
    private String sortType;
    private Long userId;

    private List<Long> storeIds;
}
