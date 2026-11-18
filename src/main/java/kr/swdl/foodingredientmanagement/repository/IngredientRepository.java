package kr.swdl.foodingredientmanagement.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IngredientRepository {
    // 1. 식재료 목록 전체 조회
    List<Map<String,Object>> getIngredientList();

    // 2. 식재료 목록 소분류별 조회
    List<Map<String, Object>> getSubCategoryList(
            @Param("subCategoryName") String subCategoryName
    );

    // 3. 식재료 목록 보관유형별 조회
    List<Map<String,Object>> getStorageTypeList(
        @Param("storageType") String storageType
    );

    // 4. 식재료 선택 삭제 (단일, 다중)
    boolean deleteIngredients(
            @Param("ids") List<Long> ids
    );

    // 5. 식재료 정보 등록
    boolean addIngredient(
            Map<String, Object> ingredient
    );

    // 6. 식재료 상세 정보
    Map<String, Object> getIngredientDetail(
            @Param("ingredientId") Long ingredientId
    );

    // 7. 식재료 상세 수정
    boolean updateIngredient(
            Map<String, Object> ingredient
    );
}
