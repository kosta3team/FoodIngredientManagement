package kr.swdl.foodingredientmanagement.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : IngredientDetailDTO
 * @since : 2026. 11. 18.
 */
@Data
public class IngredientDetailDTO {
    private Long ingredientId;                // 식재료 ID
    private String ingredientName;            // 식재료명
    private String majorCategoryName;         // 대분류
    private String subCategoryName;           // 소분류
    private String orderUnit;                 // 발주 단위
    private int capacity;                 // 용량
    private String capacityUnit;              // 용량 단위
    private String storageType;               // 보관 유형
    private int supplyPrice;              // 공급 단가
    private int supplyQuantity;           // 공급 수량
    private String managementMethod;          // 관리 방법
    private int expirationPeriod;         // 유통기한
    private int consumptionPeriod;        // 소비기한
    private Integer summerConsumptionPeriod;  // 하절기 소비기한
    private String usableFlag;                // 사용 여부
    private Timestamp registAt;           // 등록일시





}





