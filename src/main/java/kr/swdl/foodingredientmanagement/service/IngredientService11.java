package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.repository.IngredientRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class IngredientService11 {

//    // repository 연결
//    private final IngredientRepository ingredientRepository;
//
//    // 생성자를 통해 Repository 주입
//    public IngredientService(IngredientRepository ingredientRepository){
//        this.ingredientRepository = ingredientRepository;
//    }
//
//    // 1. 식재료 목록 전체 조회
//    public List<Map<String,Object>> getIngredientList(){
//        List<Map<String, Object>> result =
//                ingredientRepository.getIngredientList();
//        return result;
//    }
//
//    // 2. 식재료 목록 소분류별 조회
//    public List<Map<String, Object>> getSubCategoryList(String subCategoryName){
//        List<Map<String, Object>> result =
//                ingredientRepository.getSubCategoryList(subCategoryName);
//        return result;
//    }
//
//    // 3. 식재료 목록 보관유형별 조회
//    public List<Map<String,Object>> getStorageTypeList(String storageType){
//        List<Map<String,Object>> result = ingredientRepository.getStorageTypeList(storageType);
//        return result;
//    }
//
//    // 4. 식재료 선택 삭제 (단일, 다중)
//    @Transactional
//    public boolean deleteIngredients(List<Long> ids){
//
//        // ids 에 대한 예외 처리
//        if (ids == null || ids.isEmpty()){
//            throw new IllegalArgumentException("삭제할 식재료가 없습니다. -idx 없음")
//        }
//
//        boolean result = ingredientRepository.deleteIngredients(ids);
//        return result;
//    }
//
//    // 5. 식재료 정보 등록
//    @Transactional
//    public boolean addIngredient(Map<String, Object> ingredient){
//        boolean result = ingredientRepository.addIngredient(ingredient);
//        return result;
//    }
//
//    // 6. 식재료 상세 정보
//    public Map<String, Object> getIngredientDetail(Long ingredientId){
//        Map<String, Object> result = ingredientRepository.getIngredientDetail(ingredientId);
//        return result;
//    }
//
//    // 7. 식재료 상세 수정
//    @Transactional
//    public boolean updateIngredient(Map<String, Object> ingredient){
//
//        boolean result = ingredientRepository.updateIngredient(ingredient);
//        return result;
//    }
}
