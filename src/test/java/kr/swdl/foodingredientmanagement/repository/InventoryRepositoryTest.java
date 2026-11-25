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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList();
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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList("계란", null, null, null, null, null);

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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList(null, "광주충장", null, null, null, null);

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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList(null, null,  LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 16), null, null);

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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList(null, null, null, null,  LocalDate.of(2026, 10, 3), LocalDate.of(2026, 11, 16));

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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList("불갈비", "대전둔산", null, null, null, null);

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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList("불갈비", null, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), null, null);

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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList("불갈비", null, null, null, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));

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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList(null, "대전둔산", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30),null, null);

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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList(null, "강남", null, null,LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 30));

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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList("불갈비", "대전둔산", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30) ,null, null);

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
        List<InventoryListDTO> list = inventoryRepository.getInventoryList("불갈비", "강남", null, null ,LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
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
        InventoryListDTO inventoryListDTO = inventoryRepository.getInventoryDetail(1L, 1L);

        assertThat(inventoryListDTO)
                .isNotNull()
                .extracting("storeId", "ingredientId")
                .containsExactly(1L, 1L);

        log.info("상세: {}", inventoryListDTO);

    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회_입고정보(){
        InventoryOrderDTO inventoryOrderDTO = inventoryRepository.getInventoryOrder(1L);

        assertThat(inventoryOrderDTO).isNotNull();
        assertThat(inventoryOrderDTO.getOrderDetailId()).isEqualTo(1L);

        log.info("입고정보: {}", inventoryOrderDTO);
    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회_사용이력(){
        List<InventoryUsageListDTO> list = inventoryRepository.getInventoryUsageList(1L, 12L);

        assertThat(list).isNotEmpty();

        for(InventoryUsageListDTO inventoryUsageListDTO : list){
            log.info("사용 일시:  {} " , inventoryUsageListDTO.getConsumptionAt());
            log.info("관리 시작 일시:  {} " , inventoryUsageListDTO.getManagementStartAt());
            log.info("유통 기한:  {} " , inventoryUsageListDTO.getExpirationAt());
            log.info("제조 일자:  {} " , inventoryUsageListDTO.getManufactureAt());
            log.info("입고 일시:  {} " , inventoryUsageListDTO.getStoreAt());
            log.info("입고 수량:  {} " , inventoryUsageListDTO.getOrderQuantity());
            log.info("입고 단위:  {} " , inventoryUsageListDTO.getOrderUnit());
            log.info("사용 수량:  {} " , inventoryUsageListDTO.getUsageQuantity());
        }
    }

    @Test
    void 직영점_전체_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.getStoreInventoryList(1L, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("분류:  {} " , inventoryListDTO.getSubCategoryName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }



    @Test
    void 직영점_분류_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.getStoreInventoryList(1L, "소스류", null);

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("분류:  {} " , inventoryListDTO.getSubCategoryName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 직영점_식재료명_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.getStoreInventoryList(1L, null, 12L);

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("분류:  {} " , inventoryListDTO.getSubCategoryName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 직영점_분류_식재료명_재고_목록_조회(){
        List<InventoryListDTO> list = inventoryRepository.getStoreInventoryList(6L, "육가공류", 14L);

        assertThat(list).isNotEmpty();

        for(InventoryListDTO inventoryListDTO : list){
            log.info("직영점 명:  {} " , inventoryListDTO.getStoreName());
            log.info("분류:  {} " , inventoryListDTO.getSubCategoryName());
            log.info("식재료 명:  {} " , inventoryListDTO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListDTO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListDTO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListDTO.getOrderUnit());
            log.info("담당자:  {} " , inventoryListDTO.getUserName());
        }
    }

    @Test
    void 직영점_재고_상세_조회_잔여수량_식재료(){
        InventoryListDTO inventoryListDTO = inventoryRepository.getInventoryDetail(5L, 14L);

        assertThat(inventoryListDTO)
                .isNotNull()
                .extracting("storeId", "ingredientId")
                .containsExactly(5L, 14L);

        log.info("상세: {}", inventoryListDTO);
    }

    @Test
    void 직영점_재고_상세_조회_사용이력(){
        List<InventoryUsageListDTO> list = inventoryRepository.getInventoryUsageList(8L, 14L);

        assertThat(list).isNotEmpty();

        for(InventoryUsageListDTO inventoryUsageListDTO : list){
            log.info("사용 일시:  {} " , inventoryUsageListDTO.getConsumptionAt());
            log.info("관리 시작 일시:  {} " , inventoryUsageListDTO.getManagementStartAt());
            log.info("유통 기한:  {} " , inventoryUsageListDTO.getExpirationAt());
            log.info("제조 일자:  {} " , inventoryUsageListDTO.getManufactureAt());
            log.info("입고 일시:  {} " , inventoryUsageListDTO.getStoreAt());
            log.info("입고 수량:  {} " , inventoryUsageListDTO.getOrderQuantity());
            log.info("입고 단위:  {} " , inventoryUsageListDTO.getOrderUnit());
            log.info("사용 수량:  {} " , inventoryUsageListDTO.getUsageQuantity());
        }
    }

}
