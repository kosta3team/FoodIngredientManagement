package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.InventoryListResponseDTO;
import kr.swdl.foodingredientmanagement.dto.InventoryOrderResponseDTO;
import kr.swdl.foodingredientmanagement.dto.InventoryUsageListResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class MBInventoryRepositoryTest {
    @Autowired
    MBInventoryRepository mbInventoryRepository;

    @Test
    void 본사_전체_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList();
        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(1L, null, null, null, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_직영점명_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(null, 9L, null, null, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_입고일시_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(null, null,  LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 16), null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_사용기한_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(null, null, null, null,  LocalDate.of(2026, 10, 3), LocalDate.of(2026, 11, 16));

        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_직영점명_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(14L, 5L, null, null, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_입고일시_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(14L, null, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 8), null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_사용기한_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(14L, null, null, null, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 8));

        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_직영점명_입고일시_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(null, 6L, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30),null, null);

        assertThat(list).isNotEmpty();


        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_직영점명_사용기한_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(null, 5L, null, null,LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));

        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_직영점명_입고일시_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(14L, 5L, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30) ,null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_직영점명_사용기한_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findInventoryList(14L, 5L, null, null ,LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
        assertThat(list).isNotEmpty();

        for(InventoryListResponseDTO inventoryListResponseDTO : list){
            log.info("직영점 명:  {} " , inventoryListResponseDTO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListResponseDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListResponseDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListResponseDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListResponseDTO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListResponseDTO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListResponseDTO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListResponseDTO.getUserName());
        }
    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회(){
        InventoryListResponseDTO inventoryListResponseDTO = mbInventoryRepository.findInventoryDetail(1L, 1L);

        assertThat(inventoryListResponseDTO)
                .isNotNull()
                .extracting("storeId", "ingredientId")
                .containsExactly(1L, 1L);

        log.info("상세: {}", inventoryListResponseDTO);

    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회_입고정보(){
        InventoryOrderResponseDTO inventoryOrderResponseDTO = mbInventoryRepository.findInventoryOrder(1L);

        assertThat(inventoryOrderResponseDTO).isNotNull();
        assertThat(inventoryOrderResponseDTO.getOrderDetailId()).isEqualTo(1L);

        log.info("입고정보: {}", inventoryOrderResponseDTO);
    }

//    @Test
//    void 본사_특정_직영점_특정_재고_상세_조회_사용이력(){
//        List <InventoryUsageListResponseDTO> list = mbInventoryRepository.findInventoryUsageList(1L, 12L);
//        list.forEach(System.out::println);
//    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회_사용이력(){
        List <InventoryUsageListResponseDTO> list = mbInventoryRepository.findInventoryUsageList(1L, 12L);

        assertThat(list).isNotEmpty();
        



    }



























    @Test
    void 직영점_전체_재고_목록_조회(){

        List<InventoryListResponseDTO> list = mbInventoryRepository.findStoreInventoryList(1L, null, null);
        list.forEach(System.out::println);
    }

    @Test
    void 직영점_분류_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findStoreInventoryList(1L, "소스류", null);
        list.forEach(System.out::println);
    }

    @Test
    void 직영점_식재료명_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findStoreInventoryList(1L, null, 12L);
        list.forEach(System.out::println);
    }

    @Test
    void 직영점_분류_식재료명_재고_목록_조회(){
        List<InventoryListResponseDTO> list = mbInventoryRepository.findStoreInventoryList(5L, "육가공류", 14L);
        list.forEach(System.out::println);

    }

    @Test
    void 직영점_재고_상세_조회_잔여수량_식재료(){
        InventoryListResponseDTO inventoryListResponseDTO = mbInventoryRepository.findInventoryDetail(5L, 14L);
        System.out.println(inventoryListResponseDTO);

    }

    @Test
    void 직영점_재고_상세_조회_사용이력(){
        List <InventoryUsageListResponseDTO> list  = mbInventoryRepository.findInventoryUsageList(5L, 14L);
        list.forEach(System.out::println);

    }
}
