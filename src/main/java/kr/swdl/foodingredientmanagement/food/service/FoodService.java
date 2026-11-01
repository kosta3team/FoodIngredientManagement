package kr.swdl.foodingredientmanagement.food.service;

import kr.swdl.foodingredientmanagement.food.dto.FoodDTO;
import kr.swdl.foodingredientmanagement.food.repository.JPAFoodRepository;
import kr.swdl.foodingredientmanagement.food.repository.MBFoodRepository;
import kr.swdl.foodingredientmanagement.entitiy.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final JPAFoodRepository jpaFoodRepository;  // JPA 사용 레포지토리
    private final MBFoodRepository mbFoodRepository;    // MyBatis 사용 레포지토리

    public void addFood(FoodDTO foodDTO) {
        Food food = jpaFoodRepository.save(toEntity(foodDTO));
    }

    public FoodDTO getFoodDTO(int foodNo) {
        return toDTO(getFood(foodNo));
    }

    public List<FoodDTO> getAllFood() {
        return jpaFoodRepository.findAll().stream().map(food -> toDTO(food)).toList();
    }

    public void deleteFood(int foodNo) {
        jpaFoodRepository.deleteById(foodNo);
    }

    public FoodDTO updateFoodName(FoodDTO requestDTO) {
        Food food = getFood(requestDTO.getFoodNo());
        food.setName(requestDTO.getName());
        jpaFoodRepository.save(food);
        return toDTO(food);
    }

    private Food getFood(int foodNo) {
        return jpaFoodRepository.findById(foodNo).orElseThrow(/*사용자 정의 에러*/);
    }

    private Food toEntity(FoodDTO foodDTO) {
        return Food.builder()
                .foodNo(foodDTO.getFoodNo())
                .processedFood(foodDTO.getProcessedFood())
                .name(foodDTO.getName())
                .amount(foodDTO.getAmount())
                .unit(foodDTO.getUnit())
                .capacity(foodDTO.getCapacity()).build();
    }

    private FoodDTO toDTO(Food food) {
        return FoodDTO.builder()
                .foodNo(food.getFoodNo())
                .processedFood(food.getProcessedFood())
                .name(food.getName())
                .amount(food.getAmount())
                .unit(food.getUnit())
                .capacity(food.getCapacity()).build();
    }
}
