package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class StoreDisposalListVO {
    private Timestamp disposalReqAt;
    private String ingredientName;
    private int disposalQuantity;
    private int disposalPrice;
    private String disposalType;
    private String processingMethod;
    private String disposalStatus;
    private Timestamp checkAt;
}
