package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.InventoryListDTO;
import kr.swdl.foodingredientmanagement.dto.InventoryOrderDTO;
import kr.swdl.foodingredientmanagement.dto.InventoryUsageListDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class InventoryRepositoryTest {
    @Autowired
    InventoryRepository inventoryRepository;

    @Test
    void 본사_전체_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList();
        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(1L, null, null, null, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_직영점명_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(null, 9L, null, null, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_입고일시_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(null, null,  LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 16), null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_사용기한_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(null, null, null, null,  LocalDate.of(2026, 10, 3), LocalDate.of(2026, 11, 16));

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_직영점명_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(14L, 6L, null, null, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_입고일시_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(14L, null, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_사용기한_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(14L, null, null, null, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_직영점명_입고일시_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(null, 6L, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30),null, null);

        assertThat(list).isNotEmpty();


        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_직영점명_사용기한_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(null, 1L, null, null,LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 30));

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_직영점명_입고일시_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(14L, 6L, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30) ,null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_직영점명_사용기한_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findInventoryList(14L, 1L, null, null ,LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회(){
        InventoryListDTO inventoryListDTO = inventoryRepository.findInventoryDetail(1L, 1L);

        assertThat(inventoryListDTO)
                .isNotNull()
                .extracting("storeId", "ingredientId")
                .containsExactly(1L, 1L);

        log.info("상세: {}", inventoryListDTO);

    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회_입고정보(){
        InventoryOrderDTO inventoryOrderDTO = inventoryRepository.findInventoryOrder(1L);

        assertThat(inventoryOrderDTO).isNotNull();
        assertThat(inventoryOrderDTO.getOrderDetailId()).isEqualTo(1L);

        log.info("입고정보: {}", inventoryOrderDTO);
    }

//    @Test
//    void 본사_특정_직영점_특정_재고_상세_조회_사용이력(){
//        List <InventoryUsageListResponseDTO> list = mbInventoryRepository.findInventoryUsageList(1L, 12L);
//        list.forEach(System.out::println);
//    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회_사용이력(){
        List <InventoryUsageListDTO> list = inventoryRepository.findInventoryUsageList(1L, 12L);

        assertThat(list).isNotEmpty();
        



    }



























    @Test
    void 직영점_전체_재고_목록_조회(){

        List<InventoryListDTO> list = inventoryRepository.findStoreInventoryList(1L, null, null);
        list.forEach(System.out::println);
    }

    @Test
    void 직영점_분류_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findStoreInventoryList(1L, "소스류", null);
        list.forEach(System.out::println);
    }

    @Test
    void 직영점_식재료명_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findStoreInventoryList(1L, null, 12L);
        list.forEach(System.out::println);
    }

    @Test
    void 직영점_분류_식재료명_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.findStoreInventoryList(5L, "육가공류", 14L);
        list.forEach(System.out::println);

    }

    @Test
    void 직영점_재고_상세_조회_잔여수량_식재료(){
        InventoryListDTO inventoryListDTO = inventoryRepository.findInventoryDetail(5L, 14L);
        System.out.println(inventoryListDTO);

    }

    @Test
    void 직영점_재고_상세_조회_사용이력(){
        List <InventoryUsageListDTO> list  = inventoryRepository.findInventoryUsageList(5L, 14L);
        list.forEach(System.out::println);

    }
}
