package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DisposalStatisticsRepository {

    double getOrderDisposalRateUntilNow(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    List<StoreOrderDisposalRateVO> getOrderDisposalRatesByStore(@Param("startDate") LocalDate startDate,
                                                                @Param("endDate") LocalDate endDate);

    List<IngredientDisposalRateToOrderByStoreVO> getStoreIngredientDisposalRates(@Param("storeId") Long storeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<DisposalCostByIngredientVO> getDisposalCostsByIngredient(@Param("startDate") LocalDate startDate,
                                                                   @Param("endDate") LocalDate endDate);

    List<DisposalCostByIngredientVO> getDisposalCostsByIngredientForStore(@Param("storeId") Long storeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<DisposalCostRankingStoreInfoVO> getStoreDisposalCostRanking(@Param("date") LocalDate date);

    List<DailyDisposalTotalVO> getDailyDisposalTotalsForPeriod(@Param("today") LocalDate today,
                                                                @Param("day") int day);


    // 특정 직영점 식재료별 발주 수량 대비 폐기 수량 비율
    List<StoreIngredientDisposalQuantityRateVO> getStoreIngredientDisposalQuantityRates(@Param("storeId") Long storeId);

    // 전체 식재료별 발주 수량 대비 폐기 수량 비율
    List<IngredientDisposalQuantityRateVO> getIngredientDisposalQuantityRates();

    // 특정 직영점 폐기 유형별 비율
    List<StoreDisposalTypeRateVO> getDisposalTypeRatesByStore(@Param("storeId") Long storeId);

    // 전체 폐기 유형별 비율
    List<DisposalTypeRateVO> getDisposalTypeRates();
}
