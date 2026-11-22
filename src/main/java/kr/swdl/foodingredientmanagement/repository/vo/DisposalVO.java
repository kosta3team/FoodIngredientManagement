package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class DisposalVO {
    private Long disposalId;
    private String disposalUnit;
    private Timestamp disposalReqAt;
    private Timestamp disposalAppAt;
    private Long reqUserId;
    private String disposalStatus;
    private String processingMethod;
    private String disposalType;
    private String disposalReason;
    private String disposalDetailReason;
    private String rejectReason;
    private int disposalQuantity;
    private int disposalPrice;
    private Long orderDetailId;
}
