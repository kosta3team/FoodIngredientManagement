package kr.swdl.foodingredientmanagement.food.dto;

import kr.swdl.foodingredientmanagement.entitiy.FoodProcessingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {
    private int foodNo;
    private FoodProcessingType processedFood;
    private String name;
    int amount;
    private String unit;   // 봉 / 판 / 통 / 망..
    private String capacity;   // 개 / 그람 / 밀리리터
}
