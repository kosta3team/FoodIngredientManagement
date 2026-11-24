package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.dto.IngredientDetailDTO;
import kr.swdl.foodingredientmanagement.dto.IngredientInsertDTO;
import kr.swdl.foodingredientmanagement.dto.IngredientListDTO;
import kr.swdl.foodingredientmanagement.exception.CreateFailException;
import kr.swdl.foodingredientmanagement.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    //  식재료 목록 조회 (본사)
    @Transactional(readOnly = true)
    public List<IngredientListDTO> findIngredientList(String storageType,
                                                     String subCategoryName,
                                                     String ingredientName) {
        return ingredientRepository.findIngredientList(storageType, subCategoryName, ingredientName);
    }

    //         식재료    상세 조회 (본사)
    @Transactional(readOnly = true)
    public IngredientDetailDTO findIngredientDetail(Long ingredientId) {
        if (ingredientId == null) {
            throw new IllegalArgumentException("식재료 넘버가 없습니다");
        }

        IngredientDetailDTO detail = ingredientRepository.findIngredientDetail(ingredientId);
        if (detail == null) {
            throw new IllegalArgumentException("식재료정보를 찾을 수 없음::" + ingredientId);
        }
        return detail;
    }

    // 식재료 정보 등록 (본사)
    public Long addIngredientDetail(IngredientInsertDTO ingredientInsertDTO) {
        if (ingredientInsertDTO.getIngredientName() == null || ingredientInsertDTO.getIngredientName().isBlank()) {
            throw new IllegalArgumentException("식재료명은 필수");
        }

        int result = ingredientRepository.addIngredientDetail(ingredientInsertDTO);
        if (result != 1) {
            throw new CreateFailException("식재료 등록에 실패!!!!!");
        }

        log.info("식재료 등록 완료 ingredientId:     {}", ingredientInsertDTO.getIngredientId());
        return ingredientInsertDTO.getIngredientId();
    }

    // 식재료 정보 수정 (본사)
    public void updateIngredient(IngredientInsertDTO ingredientInsertDTO) {
        if (ingredientInsertDTO.getIngredientId() == null) {
            throw new IllegalArgumentException("수정할 식재료 아이디가ㅇ벗ㅇㅁ없음");
        }

        if (ingredientRepository.findIngredientDetail(ingredientInsertDTO.getIngredientId()) == null) {
            throw new IllegalArgumentException("식재료 정보없음 ingredientId:" + ingredientInsertDTO.getIngredientId());
        }

        int result = ingredientRepository.updateIngredientDetail(ingredientInsertDTO);
        if (result != 1) {
            throw new IllegalStateException("식재료 수정 실패 ingredientId:" + ingredientInsertDTO.getIngredientId());
        }
    }

    // 식재료 정보 삭제 (본사
    public int deleteIngredient(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("삭제할 식재료를 선택해주세요.!");
        }
        int result = ingredientRepository.deleteIngredientDetail(ids);
        return result;
    }
}
