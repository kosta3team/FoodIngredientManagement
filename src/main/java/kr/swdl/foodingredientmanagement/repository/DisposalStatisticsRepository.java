package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DisposalStatisticsRepository {

    double getOrderDisposalRateUntilNow(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

    List<StoreOrderDisposalRateDTO> getOrderDisposalRatesByStore(@Param("startDate") LocalDate startDate,
                                                                 @Param("endDate") LocalDate endDate);

    List<IngredientDisposalRateToOrderByStoreDTO> getStoreIngredientDisposalRates(@Param("storeId")Long storeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<DisposalCostByIngredientDTO> getDisposalCostsByIngredient(@Param("startDate") LocalDate startDate,
                                                                   @Param("endDate") LocalDate endDate);

    List<DisposalCostByIngredientDTO> getDisposalCostsByIngredientForStore(@Param("storeId") Long storeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<DisposalCostRankingStoreInfoDTO> getStoreDisposalCostRanking(@Param("date") LocalDate date);

    List<DailyDisposalTotalDTO> getDailyDisposalTotalsForPeriod(@Param("today") LocalDate today,
                                                                @Param("day") int day);
}
