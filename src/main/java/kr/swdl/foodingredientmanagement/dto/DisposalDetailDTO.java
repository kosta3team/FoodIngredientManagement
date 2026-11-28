package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Data
@Builder
public class DisposalDetailDTO {

    // 기본 정보
    private Long disposalId;
    private Long orderDetailId;
    private String disposalUnit;
    private String disposalStatus;

    // 폐기 정보
    private String storeName;
    private String ingredientName;
    private String processingMethod;
    private Integer disposalQuantity;
    private String orderUnit;

    // 폐기 요청 정보
    private Timestamp disposalReqAt;
    private Long reqUserId;
    private String reqUserName;
    private String reqUserRole;

    // 폐기 승인 정보
    private Timestamp disposalAppAt;
    private Long appUserId;
    private String appUserName;
    private String appUserRole;

    // 폐기 유형 및 사유
    private String disposalType;
    private String disposalReason;
    private String disposalDetailReason;
    private String rejectReason;

    // 금액 정보
    private Integer supplyPrice;
    private Integer disposalPrice;

    // 폐기 확인 정보
    private Long disposalAppInfoId;
    private Timestamp checkAt;
    private Long checkUserId;
    private String checkUserName;
    private String checkUserRole;
    private String disposalImg;
}