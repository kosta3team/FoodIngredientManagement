package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.vo.InventoryListVO;
import kr.swdl.foodingredientmanagement.repository.vo.InventoryOrderVO;
import kr.swdl.foodingredientmanagement.repository.vo.InventoryUsageListVO;
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
        List<InventoryListVO> list = inventoryRepository.getInventoryList();
        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList("계란", null, null, null, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_직영점명_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList(null, "광주충장", null, null, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_입고일시_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList(null, null,  LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 16), null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_사용기한_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList(null, null, null, null,  LocalDate.of(2026, 10, 3), LocalDate.of(2026, 11, 16));

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_직영점명_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList("불갈비", "대전둔산", null, null, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_입고일시_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList("불갈비", null, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_사용기한_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList("불갈비", null, null, null, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_직영점명_입고일시_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList(null, "대전둔산", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30),null, null);

        assertThat(list).isNotEmpty();


        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_직영점명_사용기한_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList(null, "강남", null, null,LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 30));

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_직영점명_입고일시_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList("불갈비", "대전둔산", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30) ,null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_식재료명_직영점명_사용기한_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getInventoryList("불갈비", "강남", null, null ,LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("입고 일시:  {} " , inventoryListVO.getStoreAt());
            log.info("소비 기한:  {} " , inventoryListVO.getConsumptionAt());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회(){
        InventoryListVO inventoryListVO = inventoryRepository.getInventoryDetail(1L, 1L);

        assertThat(inventoryListVO)
                .isNotNull()
                .extracting("storeId", "ingredientId")
                .containsExactly(1L, 1L);

        log.info("상세: {}", inventoryListVO);

    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회_입고정보(){
        InventoryOrderVO inventoryOrderVO = inventoryRepository.getInventoryOrder(1L);

        assertThat(inventoryOrderVO).isNotNull();
        assertThat(inventoryOrderVO.getOrderDetailId()).isEqualTo(1L);

        log.info("입고정보: {}", inventoryOrderVO);
    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회_사용이력(){
        List<InventoryUsageListVO> list = inventoryRepository.getInventoryUsageList(1L, 12L);

        assertThat(list).isNotEmpty();

        for(InventoryUsageListVO inventoryUsageListVO : list){
            log.info("사용 일시:  {} " , inventoryUsageListVO.getConsumptionAt());
            log.info("관리 시작 일시:  {} " , inventoryUsageListVO.getManagementStartAt());
            log.info("유통 기한:  {} " , inventoryUsageListVO.getExpirationAt());
            log.info("제조 일자:  {} " , inventoryUsageListVO.getManufactureAt());
            log.info("입고 일시:  {} " , inventoryUsageListVO.getStoreAt());
            log.info("입고 수량:  {} " , inventoryUsageListVO.getOrderQuantity());
            log.info("입고 단위:  {} " , inventoryUsageListVO.getOrderUnit());
            log.info("사용 수량:  {} " , inventoryUsageListVO.getUsageQuantity());
        }
    }

    @Test
    void 직영점_전체_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getStoreInventoryList(1L, null, null);

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("분류:  {} " , inventoryListVO.getSubCategoryName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }



    @Test
    void 직영점_분류_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getStoreInventoryList(1L, "소스류", null);

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("분류:  {} " , inventoryListVO.getSubCategoryName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 직영점_식재료명_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getStoreInventoryList(1L, null, 12L);

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("분류:  {} " , inventoryListVO.getSubCategoryName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 직영점_분류_식재료명_재고_목록_조회(){
        List<InventoryListVO> list = inventoryRepository.getStoreInventoryList(6L, "육가공류", 14L);

        assertThat(list).isNotEmpty();

        for(InventoryListVO inventoryListVO : list){
            log.info("직영점 명:  {} " , inventoryListVO.getStoreName());
            log.info("분류:  {} " , inventoryListVO.getSubCategoryName());
            log.info("식재료 명:  {} " , inventoryListVO.getIngredientName());
            log.info("보관유형:  {} " , inventoryListVO.getStorageType());
            log.info("잔여 수량:  {} " , inventoryListVO.getCurrentQuantity());
            log.info("입고 단위:  {} " , inventoryListVO.getOrderUnit());
            log.info("담당자:  {} " , inventoryListVO.getUserName());
        }
    }

    @Test
    void 직영점_재고_상세_조회_잔여수량_식재료(){
        InventoryListVO inventoryListVO = inventoryRepository.getInventoryDetail(5L, 14L);

        assertThat(inventoryListVO)
                .isNotNull()
                .extracting("storeId", "ingredientId")
                .containsExactly(5L, 14L);

        log.info("상세: {}", inventoryListVO);
    }

    @Test
    void 직영점_재고_상세_조회_사용이력(){
        List<InventoryUsageListVO> list = inventoryRepository.getInventoryUsageList(8L, 14L);

        assertThat(list).isNotEmpty();

        for(InventoryUsageListVO inventoryUsageListVO : list){
            log.info("사용 일시:  {} " , inventoryUsageListVO.getConsumptionAt());
            log.info("관리 시작 일시:  {} " , inventoryUsageListVO.getManagementStartAt());
            log.info("유통 기한:  {} " , inventoryUsageListVO.getExpirationAt());
            log.info("제조 일자:  {} " , inventoryUsageListVO.getManufactureAt());
            log.info("입고 일시:  {} " , inventoryUsageListVO.getStoreAt());
            log.info("입고 수량:  {} " , inventoryUsageListVO.getOrderQuantity());
            log.info("입고 단위:  {} " , inventoryUsageListVO.getOrderUnit());
            log.info("사용 수량:  {} " , inventoryUsageListVO.getUsageQuantity());
        }
    }

}
