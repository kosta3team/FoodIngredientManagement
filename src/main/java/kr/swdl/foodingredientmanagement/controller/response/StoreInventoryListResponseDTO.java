package kr.swdl.foodingredientmanagement.controller.response;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : StoreInventoryListResponseDTO
 * @since : 2026. 11. 26.
 */
@Data
@Builder
public class StoreInventoryListResponseDTO {

    // 분류 , 식재료명, 보관유형, 잔여수량 + 단위, 담당자

    private String subCategoryName;
    private String ingredientName;
    private String storageType;
    private int currentQuantity;
    private String orderUnit;
    private String userName;


    private Long storeId;
    private Long ingredientId;


}
