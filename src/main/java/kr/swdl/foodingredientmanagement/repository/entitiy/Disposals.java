package kr.swdl.foodingredientmanagement.repository.entitiy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Disposals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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