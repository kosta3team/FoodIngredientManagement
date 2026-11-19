package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.DisposalStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisposalstatisticsService {
    private final DisposalStatisticsRepository disposalStatisticsRepository;

    public double getOrderDisposalRateUntilNow(LocalDate startDate, LocalDate endDate) {
        return disposalStatisticsRepository.getOrderDisposalRateUntilNow(startDate, endDate);
    }

    public List<StoreOrderDisposalRateDTO> getOrderDisposalRatesByStore(LocalDate startDate, LocalDate endDate) {
        return disposalStatisticsRepository.getOrderDisposalRatesByStore(startDate, endDate);
    }

    public List<IngredientDisposalRateToOrderByStoreDTO> getStoreIngredientDisposalRates(Long StoreId, LocalDate startDate, LocalDate endDate) {
        return disposalStatisticsRepository.getStoreIngredientDisposalRates(StoreId, startDate, endDate);
    }

    public List<DisposalCostByIngredientDTO> 전체직영점_식재료별_폐기_비용(LocalDate startDate, LocalDate endDate) {
        return disposalStatisticsRepository.getDisposalCostsByIngredient(startDate, endDate);
    }

    public List<DisposalCostByIngredientDTO> 특정직영점_식재료별_폐기_비용(Long StoreId, LocalDate startDate, LocalDate endDate) {
        return disposalStatisticsRepository.getDisposalCostsByIngredientForStore(StoreId, startDate, endDate);
    }

    public List<DisposalCostRankingStoreInfoDTO> 직영점별_폐기_비용_순위(LocalDate date) {
        return disposalStatisticsRepository.getStoreDisposalCostRanking(LocalDate.now().minusDays(4));
    }

    public List<DailyDisposalTotalDTO> 일자별_폐기_비용_및_수량(LocalDate today, int day) {
        return disposalStatisticsRepository.getDailyDisposalTotalsForPeriod(today, day);
    }
}
