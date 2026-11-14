package kr.swdl.foodingredientmanagement.repository.entitiy;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;

public class DisposalAppInfos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long disposalAppInfoId;
    private Long appUserId;
    private Timestamp checkAt;
    private Long checkUserId;
    private String disposalImg;
    private Long disposalId;
}
