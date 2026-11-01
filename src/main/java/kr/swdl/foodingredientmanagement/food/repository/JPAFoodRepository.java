package kr.swdl.foodingredientmanagement.food.repository;

import kr.swdl.foodingredientmanagement.entitiy.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAFoodRepository extends JpaRepository<Food, Integer> {
}
