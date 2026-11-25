package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDisposalSerachFilterDTO {
    private String ingredientName;
    private String disposalStatus;
    private String processingMethod;
    private String startDate;
    private String endDate;
    private String sortType;
    private Long storeId;
}
