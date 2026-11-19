package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.*;
import lombok.extern.slf4j.Slf4j;
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

    @Test
    public void 발주대비_폐기율_현재일시_누적() {
        double rate = disposalStatisticsRepository.getOrderDisposalRateUntilNow(null, null);
        log.info("{}", rate);
    }

    @Test
    public void 직영점별_발주_대비_폐기율() {
        List<StoreOrderDisposalRateDTO> orderDisposalRatesByStore = disposalStatisticsRepository.getOrderDisposalRatesByStore(null, null);
//        List<StoreOrderDisposalRateDTO> orderDisposalRatesByStore = mbDisposalStatisticsRepository.getOrderDisposalRatesByStore(LocalDate.now().minusDays(40), LocalDate.now().minusDays(5));
        for (StoreOrderDisposalRateDTO info : orderDisposalRatesByStore) {
            log.info("{}", info);
        }
    }

    @Test
    public void 특정직영점_식재료별_발주_대비_폐기율() {
        List<IngredientDisposalRateToOrderByStoreDTO> list = disposalStatisticsRepository.getStoreIngredientDisposalRates(1L, null, null);
        for (IngredientDisposalRateToOrderByStoreDTO info : list) {
            log.info("{}", info);
        }
    }

    @Test
    public void 전체직영점_식재료별_폐기_비용() {
        List<DisposalCostByIngredientDTO> disposalCostsByIngredient = disposalStatisticsRepository.getDisposalCostsByIngredient(null, null);
        for (DisposalCostByIngredientDTO info : disposalCostsByIngredient) {
            log.info("{}", info);
        }
    }

    @Test
    public void 특정직영점_식재료별_폐기_비용() {
        List<DisposalCostByIngredientDTO> disposalCostsByIngredientForStore = disposalStatisticsRepository.getDisposalCostsByIngredientForStore(1L, null, null);
        for (DisposalCostByIngredientDTO info : disposalCostsByIngredientForStore) {
            log.info("{}", info);
        }
    }

    @Test
    public void 직영점별_폐기_비용_순위() {
        List<DisposalCostRankingStoreInfoDTO> storeDisposalCostRanking = disposalStatisticsRepository.getStoreDisposalCostRanking(LocalDate.now().minusDays(4));
        log.info("{}", LocalDate.now().minusDays(4));
        for (DisposalCostRankingStoreInfoDTO info : storeDisposalCostRanking) {
            log.info("{}", info);
        }
    }

    @Test
    public void 일자별_폐기_비용_및_수량() {
        List<DailyDisposalTotalDTO> dailyDisposalTotalsForPeriod = disposalStatisticsRepository.getDailyDisposalTotalsForPeriod(LocalDate.now(), 7);
        for (DailyDisposalTotalDTO info : dailyDisposalTotalsForPeriod) {
            log.info("{}", info);
        }
    }


}
