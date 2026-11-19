package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.IngredientCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientCategoriesRepository extends JpaRepository<IngredientCategories, Long> {
    List<IngredientCategories> findAllByMajorCategoryName(String majorCategoryName);
    @Query("SELECT DISTINCT majorCategoryName  FROM IngredientCategories")
    List<String> findMajorCategoryName();
    @Query("SELECT subCategoryName  FROM IngredientCategories WHERE majorCategoryName = :majorCategoryName")
    List<String> findSubCategoryNameByMajorCategoryName(@Param("majorCategoryName") String majorCategoryName);
}
