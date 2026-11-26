package kr.swdl.foodingredientmanagement.controller.request;

import lombok.Data;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : StoreInventorySearchRequestDTO
 * @since : 2026. 11. 26.
 */
@Data
public class StoreInventorySearchRequestDTO {
    private String subCategoryName;
    private String storeName;
    private String ingredientName;

}
