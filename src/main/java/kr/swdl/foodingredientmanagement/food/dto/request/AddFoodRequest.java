package kr.swdl.foodingredientmanagement.food.dto.request;

import kr.swdl.foodingredientmanagement.food.dto.FoodDTO;
import kr.swdl.foodingredientmanagement.entitiy.FoodProcessingType;
import lombok.Getter;

@Getter
public class AddFoodRequest {
    private FoodProcessingType processedFood;
    private String name;
    private int amount;
    private String unit;   // 봉 / 판 / 통 / 망..
    private String capacity;   // 개 / 그람 / 밀리리터

    public FoodDTO toDTO() {
        return FoodDTO.builder()
                .processedFood(getProcessedFood())
                .name(getName())
                .amount(getAmount())
                .unit(getUnit())
                .capacity(getCapacity()).build();
    }
}
