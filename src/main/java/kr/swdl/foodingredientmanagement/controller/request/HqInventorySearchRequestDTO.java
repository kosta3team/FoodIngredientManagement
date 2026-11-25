package kr.swdl.foodingredientmanagement.controller.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Please explain the class!!
 *
 * @author : jungdayoung
 * @fileName : HqInventorySearchRequestDTO
 * @since : 2026. 11. 25.
 */
@Data
public class HqInventorySearchRequestDTO {
    private Long ingredientId;
    private String storeName;
    @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate storeStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate storeEndDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate manageStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate manageEndDate;
}
