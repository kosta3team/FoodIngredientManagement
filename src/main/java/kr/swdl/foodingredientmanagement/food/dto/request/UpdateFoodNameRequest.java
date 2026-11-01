package kr.swdl.foodingredientmanagement.food.dto.request;


import kr.swdl.foodingredientmanagement.food.dto.FoodDTO;
import lombok.Getter;

@Getter
public class UpdateFoodNameRequest {
    private int foodNo;
    private String name;

    public FoodDTO toDTO() {
        return FoodDTO.builder()
                .foodNo(getFoodNo())
                .name(getName()).build();
    }
}
