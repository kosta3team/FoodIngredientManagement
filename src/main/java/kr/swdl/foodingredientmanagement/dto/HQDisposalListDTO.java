package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class HQDisposalListDTO {
    private Timestamp disposalReqAt;
    private String storeName;
    private String ingredientName;
    private int disposalQuantity;
    private int disposalPrice;
    private String disposalType;
    private String processingMethod;
    private String disposalStatus;
    private Timestamp checkAt;
}
