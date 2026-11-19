package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class StoreDisposalListDTO {
    private Timestamp disposalReqAt;
    private String ingredientName;
    private int disposalQuantity;
    private int disposalPrice;
    private String disposalType;
    private String processingMethod;
    private String disposalStatus;
    private Timestamp checkAt;
}
