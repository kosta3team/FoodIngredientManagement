package kr.swdl.foodingredientmanagement.repository.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
