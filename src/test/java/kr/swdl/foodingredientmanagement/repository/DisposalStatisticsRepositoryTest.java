package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.vo.DisposalTypeRateVO;
import kr.swdl.foodingredientmanagement.repository.vo.IngredientDisposalQuantityRateVO;
import kr.swdl.foodingredientmanagement.repository.vo.StoreDisposalTypeRateVO;
import kr.swdl.foodingredientmanagement.repository.vo.StoreIngredientDisposalQuantityRateVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Slf4j
public class DisposalStatisticsRepositoryTest {

    @Autowired
    private DisposalStatisticsRepository disposalStatisticsRepository;

//    @Test
//    public void 발주대비_폐기율_현재일시_누적() {
//        double rate = disposalStatisticsRepository.getOrderDisposalRateUntilNow(null, null);
//        log.info("{}", rate);
//    }
//
//    @Test
//    public void 직영점별_발주_대비_폐기율() {
//        List<StoreOrderDisposalRateDTO> orderDisposalRatesByStore = disposalStatisticsRepository.getOrderDisposalRatesByStore(null, null);
////        List<StoreOrderDisposalRateDTO> orderDisposalRatesByStore = mbDisposalStatisticsRepository.getOrderDisposalRatesByStore(LocalDate.now().minusDays(40), LocalDate.now().minusDays(5));
//        for (StoreOrderDisposalRateDTO info : orderDisposalRatesByStore) {
//            log.info("{}", info);
//        }
//    }
//
//    @Test
//    public void 특정직영점_식재료별_발주_대비_폐기율() {
//        List<IngredientDisposalRateToOrderByStoreDTO> list = disposalStatisticsRepository.getStoreIngredientDisposalRates(1L, null, null);
//        for (IngredientDisposalRateToOrderByStoreDTO info : list) {
//            log.info("{}", info);
//        }
//    }
//
//    @Test
//    public void 전체직영점_식재료별_폐기_비용() {
//        List<DisposalCostByIngredientDTO> disposalCostsByIngredient = disposalStatisticsRepository.getDisposalCostsByIngredient(null, null);
//        for (DisposalCostByIngredientDTO info : disposalCostsByIngredient) {
//            log.info("{}", info);
//        }
//    }
//
//    @Test
//    public void 특정직영점_식재료별_폐기_비용() {
//        List<DisposalCostByIngredientDTO> disposalCostsByIngredientForStore = disposalStatisticsRepository.getDisposalCostsByIngredientForStore(1L, null, null);
//        for (DisposalCostByIngredientDTO info : disposalCostsByIngredientForStore) {
//            log.info("{}", info);
//        }
//    }
//
//    @Test
//    public void 직영점별_폐기_비용_순위() {
//        List<DisposalCostRankingStoreInfoDTO> storeDisposalCostRanking = disposalStatisticsRepository.getStoreDisposalCostRanking(LocalDate.now().minusDays(4));
//        log.info("{}", LocalDate.now().minusDays(4));
//        for (DisposalCostRankingStoreInfoDTO info : storeDisposalCostRanking) {
//            log.info("{}", info);
//        }
//    }
//
//    @Test
//    public void 일자별_폐기_비용_및_수량() {
//        List<DailyDisposalTotalDTO> dailyDisposalTotalsForPeriod = disposalStatisticsRepository.getDailyDisposalTotalsForPeriod(LocalDate.now(), 7);
//        for (DailyDisposalTotalDTO info : dailyDisposalTotalsForPeriod) {
//            log.info("{}", info);
//        }
//    }
//
//    @Test
//    void 특정_직영점_식재료별_발주_수량_대비_폐기_수량_비율_조회() {
//
//        Long storeId = 1L;
//
//        List<StoreIngredientDisposalQuantityRateVO> result =
//                disposalStatisticsRepository
//                        .getStoreIngredientDisposalQuantityRates(storeId);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//
//        result.forEach(vo -> {
//
//            Assertions.assertEquals(storeId, vo.getStoreId());
//            Assertions.assertNotNull(vo.getIngredientId());
//            Assertions.assertNotNull(vo.getIngredientName());
//            Assertions.assertNotNull(vo.getOrderUnit());
//            Assertions.assertNotNull(vo.getTotalOrderQuantity());
//            Assertions.assertNotNull(vo.getTotalDisposalQuantity());
//            Assertions.assertNotNull(vo.getDisposalRatePct());
//
//            log.info(
//                    "직영점: {} | 식재료: {} | 발주 수량: {}{} | 폐기 수량: {}{} | 폐기율: {}%",
//                    vo.getStoreName(),
//                    vo.getIngredientName(),
//                    vo.getTotalOrderQuantity(),
//                    vo.getOrderUnit(),
//                    vo.getTotalDisposalQuantity(),
//                    vo.getOrderUnit(),
//                    vo.getDisposalRatePct()
//            );
//        });
//    }
//
//    @Test
//    void 전체_식재료별_발주_수량_대비_폐기_수량_비율_조회() {
//
//        List<IngredientDisposalQuantityRateVO> result =
//                disposalStatisticsRepository
//                        .getIngredientDisposalQuantityRates();
//
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//
//        result.forEach(vo -> {
//
//            Assertions.assertNotNull(vo.getIngredientId());
//            Assertions.assertNotNull(vo.getIngredientName());
//            Assertions.assertNotNull(vo.getOrderUnit());
//            Assertions.assertNotNull(vo.getTotalOrderQuantity());
//            Assertions.assertNotNull(vo.getTotalDisposalQuantity());
//            Assertions.assertNotNull(vo.getDisposalRatePct());
//
//            log.info(
//                    "식재료: {} | 발주 수량: {}{} | 폐기 수량: {}{} | 폐기율: {}%",
//                    vo.getIngredientName(),
//                    vo.getTotalOrderQuantity(),
//                    vo.getOrderUnit(),
//                    vo.getTotalDisposalQuantity(),
//                    vo.getOrderUnit(),
//                    vo.getDisposalRatePct()
//            );
//        });
//    }
//
//    @Test
//    void 특정_직영점_폐기_유형별_비율_조회() {
//
//        Long storeId = 1L;
//
//        List<StoreDisposalTypeRateVO> result =
//                disposalStatisticsRepository
//                        .getDisposalTypeRatesByStore(storeId);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//
//        result.forEach(vo -> {
//
//            Assertions.assertEquals(storeId, vo.getStoreId());
//            Assertions.assertNotNull(vo.getStoreName());
//            Assertions.assertNotNull(vo.getDisposalType());
//            Assertions.assertNotNull(vo.getDisposalCount());
//            Assertions.assertNotNull(vo.getTotalDisposalCount());
//            Assertions.assertNotNull(vo.getDisposalRate());
//            Assertions.assertNotNull(vo.getDisposalRatePct());
//
//            log.info(
//                    "직영점: {} | 폐기 유형: {} | 폐기 건수: {} | 전체 폐기 건수: {} | 비율: {} | 폐기율: {}%",
//                    vo.getStoreName(),
//                    vo.getDisposalType(),
//                    vo.getDisposalCount(),
//                    vo.getTotalDisposalCount(),
//                    vo.getDisposalRate(),
//                    vo.getDisposalRatePct()
//            );
//        });
//
//        // 유형별 폐기 건수 합계 검증
//        long totalCount = result.stream()
//                .mapToLong(StoreDisposalTypeRateVO::getDisposalCount)
//                .sum();
//
//        Assertions.assertEquals(
//                result.get(0).getTotalDisposalCount().longValue(),
//                totalCount
//        );
//
//        // 유형별 폐기 비율 합계 검증
//        double totalRate = result.stream()
//                .mapToDouble(StoreDisposalTypeRateVO::getDisposalRate)
//                .sum();
//
//        Assertions.assertEquals(1.0, totalRate, 0.001);
//
//        log.info("전체 폐기 건수: {}", totalCount);
//        log.info("전체 폐기 비율: {}", totalRate);
//    }
//
//    @Test
//    void 전체_폐기_유형별_비율_조회() {
//
//        List<DisposalTypeRateVO> result =
//                disposalStatisticsRepository
//                        .getDisposalTypeRates();
//
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//
//        result.forEach(vo -> {
//
//            Assertions.assertNotNull(vo.getDisposalType());
//            Assertions.assertNotNull(vo.getDisposalCount());
//            Assertions.assertNotNull(vo.getTotalDisposalCount());
//            Assertions.assertNotNull(vo.getDisposalRate());
//            Assertions.assertNotNull(vo.getDisposalRatePct());
//
//            log.info(
//                    "폐기 유형: {} | 폐기 건수: {} | 전체 폐기 건수: {} | 비율: {} | 폐기율: {}%",
//                    vo.getDisposalType(),
//                    vo.getDisposalCount(),
//                    vo.getTotalDisposalCount(),
//                    vo.getDisposalRate(),
//                    vo.getDisposalRatePct()
//            );
//        });
//
//        // 유형별 폐기 건수 합계 검증
//        long totalCount = result.stream()
//                .mapToLong(DisposalTypeRateVO::getDisposalCount)
//                .sum();
//
//        Assertions.assertEquals(
//                result.get(0).getTotalDisposalCount().longValue(),
//                totalCount
//        );
//
//        // 유형별 폐기 비율 합계 검증
//        double totalRate = result.stream()
//                .mapToDouble(DisposalTypeRateVO::getDisposalRate)
//                .sum();
//
//        Assertions.assertEquals(1.0, totalRate, 0.001);
//
//        log.info("전체 폐기 건수: {}", totalCount);
//        log.info("전체 폐기 비율: {}", totalRate);
//    }
}
