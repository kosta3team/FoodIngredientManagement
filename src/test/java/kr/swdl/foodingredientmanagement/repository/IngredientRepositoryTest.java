package kr.swdl.foodingredientmanagement.repository;

import org.apache.ibatis.annotations.Param;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=none")
public class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    // 1. 식재료 목록 전체 조회
    @Test
    void getIngredientList(){
        // 1) 메서드 호출
        List<Map<String,Object>> result =
                ingredientRepository.getIngredientList();

        // 2) 결과 검증
        assertThat(result).isNotNull();

        // 3) 결과 확인
        System.out.println("1. 식재료 목록 전체 조회 = " + result);
    }

    // 2. 식재료 목록 소분류별 조회
    @Test
    void findSubCategoryList(){

        // 1) 메서드 호출
        List<Map<String, Object>> result1 =
                ingredientRepository.getSubCategoryList("채소류");

        List<Map<String, Object>> result2 =
                ingredientRepository.getSubCategoryList("빵류");

        List<Map<String, Object>> result3 =
                ingredientRepository.getSubCategoryList("유지류");

        // 2) 결과 검증
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNotNull();

        // 3) 결과 확인
        System.out.println("2. 식재료 목록 소분류별 조회 result1 = " + result1);
        System.out.println("2. 식재료 목록 소분류별 조회 result2 = " + result2);
        System.out.println("2. 식재료 목록 소분류별 조회 result3 = " + result3);
    }

    // 3. 식재료 목록 보관유형별 조회
    @Test
    void getStorageTypeList(){
        // 1) 메서드 호출
        List<Map<String,Object>> result1 =
                ingredientRepository.getStorageTypeList("냉장");

        List<Map<String,Object>> result2 =
                ingredientRepository.getStorageTypeList("냉동");

        // 2) 결과 검증
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();

        // 3) 결과 확인
        System.out.println("3. 식재료 목록 보관유형별 조회 result1 = " + result1);
        System.out.println("3. 식재료 목록 보관유형별 조회 result2 = " + result2);
    }

    // 1) 메서드 호출
    // 2) 결과 검증
    // 3) 결과 확인

    // 4. 식재료 선택 삭제 (단일, 다중)
    @Test
    void deleteIngredients(){

        // 1) 메서드 호출
        boolean result1 =
                ingredientRepository.deleteIngredients(List.of(1L));

        boolean result2 =
                ingredientRepository.deleteIngredients(List.of(1L,2L,3L));

        // 2) 결과 검증
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();

        // 3) 결과 확인
        System.out.println("4. 식재료 선택 삭제 (단일, 다중) result1 = " + result1);
        System.out.println("4. 식재료 선택 삭제 (단일, 다중) result2 = " + result2);
    }

    // 5-1. 식재료 등록를 위한 데이터
    private Map<String, Object> addTestIngredient(){

        Map<String, Object> ingredient = new HashMap<>();

        ingredient.put("ingredient_name", "TEST_크리스피");
        ingredient.put("major_category_name", "비가공식품");
        ingredient.put("sub_category_name", "채소류");
        ingredient.put("capacity", 1);
        ingredient.put("capacity_unit", "EA");
        ingredient.put("order_unit", "봉");
        ingredient.put("storage_type", "냉장");
        ingredient.put("supply_price", 3000);
        ingredient.put("expiration_period", 8);
        ingredient.put("management_method", "손질");
        ingredient.put("consumption_period", 3);
        ingredient.put("summer_consumption_period", 2);
        ingredient.put("supply_quantity", 10);

        return ingredient;
    }

    // 5-2. 식재료 정보 등록
    @Test
    void addIngredient(){
        // 1) 등록 데이터
        Map<String, Object> ingredient = addTestIngredient();

        // 2) 메서드 호출
        boolean result = ingredientRepository.addIngredient(ingredient);

        // 3) 결과 검증
        assertThat(result).isTrue();

        // 4) 결과 확인
        System.out.println("5. 식재료 정보 등록 result = " + result);
    }

    // 6. 식재료 상세 정보
    @Test
    void getIngredientDetail(){
        Map<String, Object> result = ingredientRepository.getIngredientDetail(1L);

        // 2) 결과 검증
        assertThat(result).isNotNull();

        // 3) 결과 확인
        System.out.println("6. 식재료 상세 정보 result = " + result);
    }

    // 7. 식재료 상세 수정
    @Test
    void updateIngredient(){

        // 1) 데이터 준비
        Map<String, Object> ingredientSamle = addTestIngredient();
        ingredientSamle.put("ingredient_id", -1L);
        ingredientSamle.put("supply_price", 3500);

        // 2) 메서드 호출
        boolean result = ingredientRepository.updateIngredient(ingredientSamle);

        // 3) 결과 검증
        assertThat(result).isFalse();

        // 4) 결과 확인
        System.out.println("7. 식재료 상세 수정 result = " + result);
    }
}
