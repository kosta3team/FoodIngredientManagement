package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.dto.IngredientDetailDTO;
import kr.swdl.foodingredientmanagement.dto.IngredientInsertDTO;
import kr.swdl.foodingredientmanagement.dto.IngredientListDTO;
import kr.swdl.foodingredientmanagement.repository.MBIngredientRepository;
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
    private MBIngredientRepository mbIngredientRepository;

//
    @Test
    public void 식재료_목록_조회_성공() {
        List<IngredientListDTO> list = mbIngredientRepository.findIngredientList("냉장", null, "치");
        Assertions.assertFalse(list.isEmpty());
        for (IngredientListDTO info : list) {
            Assertions.assertEquals("냉장", info.getStorageType());
            log.info("{}", info);
        }
    }

    @Test
    public void 식재료_목록조회_실패() {
        List<IngredientListDTO> list = mbIngredientRepository.findIngredientList(null, null, "없음식재료명");
        Assertions.assertTrue(list.isEmpty());
    }
    @Test
    public void 식재료_상세_조회_성공() {
        Long ingredientId = Long.valueOf(1);


        IngredientDetailDTO detail = mbIngredientRepository.findIngredientDetail(ingredientId);
        if (detail == null) Assertions.fail();

        Assertions.assertEquals(ingredientId, detail.getIngredientId());
        log.info("{}", detail);
    }

    @Test
    public void 식재료_상세_조회_실패() {
        Long ingredientId = Long.valueOf(9999);

        IngredientDetailDTO detail = mbIngredientRepository.findIngredientDetail(ingredientId);
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


        int result = mbIngredientRepository.addIngredientDetail(dto);
        Assertions.assertEquals(1, result);
        Assertions.assertNotNull(dto.getIngredientId());



        IngredientDetailDTO saved = mbIngredientRepository.findIngredientDetail(dto.getIngredientId());
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
        mbIngredientRepository.addIngredientDetail(insertDTO);
        Long ingredientId = insertDTO.getIngredientId();

        if (mbIngredientRepository.findIngredientDetail(ingredientId) == null) Assertions.fail();


        IngredientInsertDTO updateDTO = IngredientInsertDTO.builder()
                .ingredientId(ingredientId)
                .ingredientName("테스트로메인")
                .supplyPrice(18000)
                .build();
        int result = mbIngredientRepository.updateIngredientDetail(updateDTO);
        Assertions.assertEquals(1, result);




        IngredientDetailDTO updated = mbIngredientRepository.findIngredientDetail(ingredientId);
        Assertions.assertEquals("테스트로메인", updated.getIngredientName());
        Assertions.assertEquals(18000, updated.getSupplyPrice());
        Assertions.assertEquals("냉장", updated.getStorageType());
    }

    @Test
    @Transactional
    public void 식재료_수정_실패() {
        Long ingredientId = Long.valueOf(9999);

        if (mbIngredientRepository.findIngredientDetail(ingredientId) == null) {
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
        mbIngredientRepository.addIngredientDetail(ingredientInsertDTO1);
        mbIngredientRepository.addIngredientDetail(ingredientInsertDTO2);

        List<Long> ids = List.of(ingredientInsertDTO1.getIngredientId(), ingredientInsertDTO2.getIngredientId());
        if (ids == null || ids.isEmpty()) Assertions.fail();

        int result = mbIngredientRepository.deleteIngredientDetail(ids);
        Assertions.assertEquals(2, result);

        Assertions.assertNull(mbIngredientRepository.findIngredientDetail(ingredientInsertDTO1.getIngredientId()));
        Assertions.assertNull(mbIngredientRepository.findIngredientDetail(ingredientInsertDTO2.getIngredientId()));
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
