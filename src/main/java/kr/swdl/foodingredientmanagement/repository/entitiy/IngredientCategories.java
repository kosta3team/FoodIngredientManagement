package kr.swdl.foodingredientmanagement.repository.entitiy;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class IngredientCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientCategoryId;
    private String majorCategoryName;
    private String subCategoryName;

}