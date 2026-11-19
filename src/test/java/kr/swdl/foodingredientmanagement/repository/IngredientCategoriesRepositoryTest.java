package kr.swdl.foodingredientmanagement.repository;


import kr.swdl.foodingredientmanagement.repository.entitiy.IngredientCategories;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class IngredientCategoriesRepositoryTest {

    @Autowired
    private IngredientCategoriesRepository ingredientCategoriesRepository;

    @Test
    public void 폐기유형_대분류_가져오기() {
        // TODO: 서비스 계층
        List<String> list = ingredientCategoriesRepository.findAll().stream()
                .map(IngredientCategories::getMajorCategoryName)
                .distinct()
                .toList();
        for (String majorCategoryName : list) {
            log.info("{}", majorCategoryName);
        }
        Assertions.assertTrue(list.size() > 0);
    }

    @Test
    public void 폐기유형_대분류_가져오기_커스텀쿼리() {
        // TODO: 서비스 계층
        List<String> list = ingredientCategoriesRepository.findMajorCategoryName();
        for (String majorCategoryName : list) {
            log.info("{}", majorCategoryName);
        }
        Assertions.assertTrue(list.size() > 0);
    }

    @Test
    public void 특정_식재료_대분류_하위목록() {
        List<String> list = ingredientCategoriesRepository.findAllByMajorCategoryName("가공식품")
                .stream().map(IngredientCategories::getSubCategoryName)
                .toList();
        for (String subCategoryName : list) {
            log.info("{}", subCategoryName);
        }
        Assertions.assertTrue(list.size() > 0);
    }

    @Test
    public void 특정_식재료_대분류_하위목록_커스텀쿼리() {
        List<String> list = ingredientCategoriesRepository.findSubCategoryNameByMajorCategoryName("가공식품");
        for (String subCategoryName : list) {
            log.info("{}", subCategoryName);
        }
        Assertions.assertTrue(list.size() > 0);
    }
}
