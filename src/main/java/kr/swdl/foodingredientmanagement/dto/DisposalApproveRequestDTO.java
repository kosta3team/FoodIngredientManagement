package kr.swdl.foodingredientmanagement.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DisposalApproveRequestDTO {
    private String disposalStatus;
    private String rejectReason;
}
