package kr.swdl.foodingredientmanagement.dto;

import jakarta.annotation.sql.DataSourceDefinition;
import lombok.Builder;
import lombok.Data;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : IngredientInsertDTO
 * @since : 2026. 11. 18.
 */
@Data
@Builder
public class IngredientInsertDTO {
    private Long ingredientId;

    // 식재료 기본 정보
    private String ingredientName;
    private String majorCategoryName;
    private String subCategoryName;
    private String storageType;
    private String orderUnit;
    private Integer capacity;
    private String capacityUnit;

    // 보관   // 소비
    private String managementMethod;
    private Integer consumptionPeriod;
    private Integer summerConsumptionPeriod;
    private Integer expirationPeriod;

    // 공급
    private Integer supplyPrice;
    private Integer supplyQuantity;
}
