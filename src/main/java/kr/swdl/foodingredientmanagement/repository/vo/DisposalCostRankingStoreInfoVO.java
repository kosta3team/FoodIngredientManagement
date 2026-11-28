package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

@Data
public class DisposalCostRankingStoreInfoVO {
    private Long storeId;
    private String storeName;
    private int totalDisposalPrice;
    private int totalDisposalQuantity;
}
