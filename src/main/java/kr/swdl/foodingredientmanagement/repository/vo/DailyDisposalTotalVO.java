package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DailyDisposalTotalVO {
    private Timestamp disposalDate;
    private int disposalCount;
    private int totalDisposalQuantity;
    private int totalDisposalPrice;
}
