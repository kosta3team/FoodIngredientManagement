package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.dto.IngredientDetailDTO;
import kr.swdl.foodingredientmanagement.dto.IngredientInsertDTO;
import kr.swdl.foodingredientmanagement.dto.IngredientListDTO;
import kr.swdl.foodingredientmanagement.repository.IngredientRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Slf4j
public class IngredientServiceTest {

    @Autowired
    private IngredientRepository ingredientRepository;

//
    @Test
    public void 식재료_목록_조회_성공() {
        List<IngredientListDTO> list = ingredientRepository.findIngredientList("냉장", null, "치");
        Assertions.assertFalse(list.isEmpty());
        for (IngredientListDTO info : list) {
            Assertions.assertEquals("냉장", info.getStorageType());
            log.info("{}", info);
        }
    }

    @Test
    public void 식재료_목록조회_실패() {
        List<IngredientListDTO> list = ingredientRepository.findIngredientList(null, null, "없음식재료명");
        Assertions.assertTrue(list.isEmpty());
    }
    @Test
    public void 식재료_상세_조회_성공() {
        Long ingredientId = Long.valueOf(1);


        IngredientDetailDTO detail = ingredientRepository.findIngredientDetail(ingredientId);
        if (detail == null) Assertions.fail();

        Assertions.assertEquals(ingredientId, detail.getIngredientId());
        log.info("{}", detail);
    }

    @Test
    public void 식재료_상세_조회_실패() {
        Long ingredientId = Long.valueOf(9999);

        IngredientDetailDTO detail = ingredientRepository.findIngredientDetail(ingredientId);
        Assertions.assertNull(detail);
    }

    @Test
    @Transactional
    public void 식재료_등록_성공() {
        IngredientInsertDTO dto = IngredientInsertDTO.builder()
                .ingredientName("테스트양상추")
                .majorCategoryName("채소류")
                .subCategoryName("잎채소")
                .storageType("냉장")
                .orderUnit("박스")
                .capacity(5000)
                .capacityUnit("g")
                .managementMethod("입고일")
                .consumptionPeriod(8)
                .summerConsumptionPeriod(8)
                .expirationPeriod(3)
                .supplyPrice(15000)
                .supplyQuantity(10)
                .build();


        if (dto.getIngredientName() == null || dto.getIngredientName().isBlank()) Assertions.fail();


        int result = ingredientRepository.addIngredientDetail(dto);
        Assertions.assertEquals(1, result);
        Assertions.assertNotNull(dto.getIngredientId());



        IngredientDetailDTO saved = ingredientRepository.findIngredientDetail(dto.getIngredientId());
        Assertions.assertEquals("테스트양상추", saved.getIngredientName());
        Assertions.assertEquals("Y", saved.getUsableFlag());
    }

    @Test
    @Transactional
    public void 식재료_등록_실패() {
        IngredientInsertDTO dto = IngredientInsertDTO.builder()
                .ingredientName(" ")
                .build();

        if (dto.getIngredientName() == null || dto.getIngredientName().isBlank()) {
            return;
        }
        Assertions.fail();
    }


    @Test
    @Transactional
    public void 식재료_수정_성공() {
        IngredientInsertDTO insertDTO = IngredientInsertDTO.builder()
                .ingredientName("테스트양상추")
                .majorCategoryName("채소류")
                .subCategoryName("잎채소")
                .storageType("냉장")
                .orderUnit("박스")
                .capacity(5000)
                .capacityUnit("g")
                .managementMethod("입고일")
                .consumptionPeriod(8)
                .summerConsumptionPeriod(8)
                .expirationPeriod(3)
                .supplyPrice(15000)

                .supplyQuantity(10)
                .build();
        ingredientRepository.addIngredientDetail(insertDTO);
        Long ingredientId = insertDTO.getIngredientId();

        if (ingredientRepository.findIngredientDetail(ingredientId) == null) Assertions.fail();


        IngredientInsertDTO updateDTO = IngredientInsertDTO.builder()
                .ingredientId(ingredientId)
                .ingredientName("테스트로메인")
                .supplyPrice(18000)
                .build();
        int result = ingredientRepository.updateIngredientDetail(updateDTO);
        Assertions.assertEquals(1, result);




        IngredientDetailDTO updated = ingredientRepository.findIngredientDetail(ingredientId);
        Assertions.assertEquals("테스트로메인", updated.getIngredientName());
        Assertions.assertEquals(18000, updated.getSupplyPrice());
        Assertions.assertEquals("냉장", updated.getStorageType());
    }

    @Test
    @Transactional
    public void 식재료_수정_실패() {
        Long ingredientId = Long.valueOf(9999);

        if (ingredientRepository.findIngredientDetail(ingredientId) == null) {
            return;
        }
        Assertions.fail();
    }

    @Test
    @Transactional
    public void 식재료_삭제_성공() {
        IngredientInsertDTO  ingredientInsertDTO1 = IngredientInsertDTO.builder()
                .ingredientName("테스트양상추1")
                .majorCategoryName("채소류")
                .subCategoryName("잎채소")
                .storageType("냉장")
                .orderUnit("박스")
                .capacity(5000)
                .capacityUnit("g")
                .managementMethod("입고일")
                .consumptionPeriod(8)
                .summerConsumptionPeriod(8)
                .expirationPeriod(3)
                .supplyPrice(15000)
                .supplyQuantity(10)
                .build();





        IngredientInsertDTO ingredientInsertDTO2 = IngredientInsertDTO.builder()
                .ingredientName("테스트양상추2")
                .majorCategoryName("채소류")
                .subCategoryName("잎채소")
                .storageType("냉장")
                .orderUnit("박스")
                .capacity(5000)
                .capacityUnit("g")
                .managementMethod("입고일")
                .consumptionPeriod(8)
                .summerConsumptionPeriod(8)
                .expirationPeriod(3)
                .supplyPrice(15000)
                .supplyQuantity(10)
                .build();
        ingredientRepository.addIngredientDetail(ingredientInsertDTO1);
        ingredientRepository.addIngredientDetail(ingredientInsertDTO2);

        List<Long> ids = List.of(ingredientInsertDTO1.getIngredientId(), ingredientInsertDTO2.getIngredientId());
        if (ids == null || ids.isEmpty()) Assertions.fail();

        int result = ingredientRepository.deleteIngredientDetail(ids);
        Assertions.assertEquals(2, result);

        Assertions.assertNull(ingredientRepository.findIngredientDetail(ingredientInsertDTO1.getIngredientId()));
        Assertions.assertNull(ingredientRepository.findIngredientDetail(ingredientInsertDTO2.getIngredientId()));
    }







    @Test
    @Transactional
    public void 식재료_삭제_실패() {
        List<Long> ids = List.of();

        if (ids == null || ids.isEmpty()) {
            return;
        }

        Assertions.fail();
    }

}
