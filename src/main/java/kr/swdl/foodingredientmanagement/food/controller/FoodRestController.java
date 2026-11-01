package kr.swdl.foodingredientmanagement.food.controller;

import kr.swdl.foodingredientmanagement.food.dto.FoodDTO;
import kr.swdl.foodingredientmanagement.food.dto.request.AddFoodRequest;
import kr.swdl.foodingredientmanagement.food.dto.request.UpdateFoodNameRequest;
import kr.swdl.foodingredientmanagement.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/food")
public class FoodRestController {

    private final FoodService foodService;

    @PostMapping
    public void addFood(@RequestBody AddFoodRequest addFoodRequest) {
        foodService.addFood(addFoodRequest.toDTO());
    }

    @GetMapping("/{foodNo}")
    public ResponseEntity<?> getFood(@PathVariable int foodNo) {
        FoodDTO foodDTO  = foodService.getFoodDTO(foodNo);
        if (null == foodDTO) {
            return ResponseEntity.badRequest().body("잘못된 요청");
        }
        return ResponseEntity.ok(foodDTO);
    }

    @GetMapping
    public ResponseEntity<?> getAllFood() {
        return ResponseEntity.ok(foodService.getAllFood());
    }

    @PutMapping
    public void updateFoodName(@RequestBody UpdateFoodNameRequest updateFoodNameRequest) {
        foodService.updateFoodName(updateFoodNameRequest.toDTO());
    }

    @DeleteMapping("/{foodNo}")
    public void deleteFood(@PathVariable int foodNo) {
        foodService.deleteFood(foodNo);
    }

}
