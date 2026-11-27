package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.controller.request.HQInventorySearchRequestDTO;
import kr.swdl.foodingredientmanagement.controller.response.HQInventoryListResponseDTO;
import kr.swdl.foodingredientmanagement.controller.response.StoreInventoryDetailResponseDTO;
import kr.swdl.foodingredientmanagement.repository.vo.InventoryListVO;
import kr.swdl.foodingredientmanagement.repository.vo.InventoryOrderVO;
import kr.swdl.foodingredientmanagement.repository.vo.InventoryUsageListVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Slf4j
public class InventoryServiceTest {
    @Autowired
    private InventoryService inventoryService;

    @Test
    public void 본사_전체_재고_목록_조회_성공() {
        List<InventoryListVO> list = inventoryService.getInventoryList();
        Assertions.assertFalse(list.isEmpty());
        for (InventoryListVO info : list) {
            Assertions.assertTrue(info.getCurrentQuantity() > 0);
            log.info("{}", info);
        }
    }

    @Test
    public void 본사_식재료명_재고_목록_조회_성공() {
        List<InventoryListVO> list = inventoryService.getInventoryList("불갈비", null, null, null, null, null);

        Assertions.assertFalse(list.isEmpty());
        for (InventoryListVO info : list) {
            Assertions.assertEquals("불갈비 패티", info.getIngredientName());
            log.info("{}", info);
        }
    }

    @Test
    public void 본사_식재료명_재고_목록_조회_실패() {
        List<InventoryListVO> list = inventoryService.getInventoryList("999", null, null, null, null, null);
        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    public void 본사_직영점명_재고_목록_조회_성공() {
        List<InventoryListVO> list = inventoryService.getInventoryList(null, "대전둔산", null, null, null, null);

        Assertions.assertFalse(list.isEmpty());
        for (InventoryListVO info : list) {
            Assertions.assertEquals("대전둔산직영점", info.getStoreName());
            log.info("{}", info);
        }
    }

    @Test
    public void 본사_직영점명_재고_목록_조회_실패() {
        List<InventoryListVO> list = inventoryService.getInventoryList(null, "없는직영점", null, null, null, null);
        Assertions.assertTrue(list.isEmpty());

    }

    @Test
    public void 본사_직영점명_공백_재고_목록_조회_성공() {
        List<InventoryListVO> all = inventoryService.getInventoryList();
        List<InventoryListVO> list = inventoryService.getInventoryList(null, "   ", null, null, null, null);

        Assertions.assertEquals(all.size(), list.size());
    }

    @Test
    public void 본사_입고일시_재고_목록_조회_성공() {
        LocalDate start = LocalDate.of(2026, 11, 1);
        LocalDate end = LocalDate.of(2026, 11, 30);

        List<InventoryListVO> list = inventoryService.getInventoryList(null, null, start, end, null, null);

        Assertions.assertFalse(list.isEmpty());
        for (InventoryListVO info : list) {
            LocalDate storeDate = info.getStoreAt().toLocalDateTime().toLocalDate();
            Assertions.assertFalse(storeDate.isBefore(start));
            Assertions.assertFalse(storeDate.isAfter(end));
            log.info("{}", info);
        }
    }

    @Test
    public void 본사_입고일시_재고_목록_조회_실패() {
        List<InventoryListVO> list = inventoryService.getInventoryList(
                null, null, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 31), null, null);

        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    public void 본사_사용기한_재고_목록_조회_성공() {
        LocalDate start = LocalDate.of(2026, 10, 3);
        LocalDate end = LocalDate.of(2026, 11, 16);

        List<InventoryListVO> list = inventoryService.getInventoryList(null, null, null, null, start, end);

        Assertions.assertFalse(list.isEmpty());
        for (InventoryListVO info : list) {
            LocalDate consumptionDate = info.getConsumptionAt().toLocalDateTime().toLocalDate();
            Assertions.assertFalse(consumptionDate.isBefore(start));
            Assertions.assertFalse(consumptionDate.isAfter(end));
            log.info("{}", info);
        }
    }

    @Test
    public void 본사_사용기한_재고_목록_조회_실패() {
        List<InventoryListVO> list = inventoryService.getInventoryList(null, null, null, null, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 31));

        Assertions.assertTrue(list.isEmpty());
    }


    @Test
    public void 직영점_전체_재고_목록_조회_성공() {
        List<InventoryListVO> list = inventoryService.getStoreInventoryList(1L, null, null);

        Assertions.assertFalse(list.isEmpty());
        for (InventoryListVO info : list) {
            Assertions.assertEquals("강남직영점", info.getStoreName());
            log.info("{}", info);
        }
    }

    @Test
    public void 직영점_전체_재고_목록_조회_실패() {
        List<InventoryListVO> list = inventoryService.getStoreInventoryList(9999L, null, null);
        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    public void 직영점_분류_재고_목록_조회_성공() {
        List<InventoryListVO> list = inventoryService.getStoreInventoryList(1L, "소스류", null);
        Assertions.assertFalse(list.isEmpty());
        for (InventoryListVO info : list) {
            Assertions.assertEquals("소스류", info.getSubCategoryName());
            log.info("{}", info);
        }
    }

    @Test
    public void 직영점_분류_재고_목록_조회_실패() {
        List<InventoryListVO> list = inventoryService.getStoreInventoryList(1L, "실패실패", null);
        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    public void 직영점_분류_식재료명_재고_목록_조회_성공() {
        List<InventoryListVO> list = inventoryService.getStoreInventoryList(6L, "육가공류", "불갈비 패티");

        Assertions.assertFalse(list.isEmpty());
        for (InventoryListVO info : list) {
            Assertions.assertEquals("육가공류", info.getSubCategoryName());
            Assertions.assertEquals("불갈비 패티", info.getIngredientName());
            log.info("{}", info);
        }
    }

    @Test
    public void 직영점_분류_식재료명_재고_목록_조회_실패() {
        List<InventoryListVO> list = inventoryService.getStoreInventoryList(5L, "육가공류", "불갈비 패티");
        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    public void 재고_상세_조회_성공() {
        InventoryListVO detail = inventoryService.getInventoryDetail(1L, 1L);

        Assertions.assertNotNull(detail);
        Assertions.assertEquals(1L, detail.getStoreId());
        Assertions.assertEquals(1L, detail.getIngredientId());
        log.info("{}", detail);
    }

    @Test
    public void 재고_상세_조회_실패() {
        InventoryListVO detail = inventoryService.getInventoryDetail(9999L, 9999L);
        Assertions.assertNull(detail);
    }


    @Test
    public void 재고_입고정보_조회_성공() {
        InventoryOrderVO order = inventoryService.getInventoryOrder(1L);
        Assertions.assertNotNull(order);
        Assertions.assertEquals(1L, order.getOrderDetailId());
        log.info("{}", order);
    }

    @Test
    public void 재고_입고정보_조회_실패() {
        InventoryOrderVO order = inventoryService.getInventoryOrder(9999L);
        Assertions.assertNull(order);
    }

    @Test
    public void 재고_사용이력_조회_성공() {
        List<InventoryUsageListVO> list = inventoryService.getInventoryUsageList(8L, 14L);
        Assertions.assertFalse(list.isEmpty());
        for (InventoryUsageListVO info : list) {
            Assertions.assertTrue(info.getUsageQuantity() > 0);
            log.info("{}", info);
        }
    }

    @Test
    public void 재고_사용이력_조회_실패() {
        List<InventoryUsageListVO> list = inventoryService.getInventoryUsageList(5L, 14L);
        Assertions.assertTrue(list.isEmpty());
    }


    @Test
    public void 본사_재고_목록_검색_응답DTO_변환_성공() {
        HQInventorySearchRequestDTO request = new HQInventorySearchRequestDTO();
        request.setStoreName("강남");

        List<HQInventoryListResponseDTO> list = inventoryService.getHqInventoryList(request);

        Assertions.assertFalse(list.isEmpty());
        for (HQInventoryListResponseDTO info : list) {
            Assertions.assertTrue(info.getStoreName().contains("강남"));
            Assertions.assertNotNull(info.getStoreAt());
        }
    }

    @Test
    public void 본사_재고_목록_검색_응답DTO_변환_실패() {
        HQInventorySearchRequestDTO request = new HQInventorySearchRequestDTO();
        request.setStoreName("없는직영점");

        List<HQInventoryListResponseDTO> list = inventoryService.getHqInventoryList(request);

        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    public void 재고_분류_목록_조회_성공() {
        List<String> list = inventoryService.getSubCategoryList();

        Assertions.assertFalse(list.isEmpty());
        Assertions.assertTrue(list.contains("육가공류"));
        Assertions.assertEquals(list.size(), list.stream().distinct().count());   // 중복 없음
        log.info("{}", list);
    }

    @Test
    public void 직영점_재고_상세_조회_성공() {
        StoreInventoryDetailResponseDTO detail = inventoryService.getStoreInventoryDetail(1L);
        Assertions.assertNotNull(detail);
        log.info("{}", detail);
    }

    @Test
    public void 직영점_재고_상세_조회_실패() {
        StoreInventoryDetailResponseDTO detail = inventoryService.getStoreInventoryDetail(99999L);
        Assertions.assertNull(detail);
    }
}
