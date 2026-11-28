package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.DisposalStatisticsRepository;
import kr.swdl.foodingredientmanagement.repository.StoreRepository;
import kr.swdl.foodingredientmanagement.repository.entitiy.Stores;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisposalStaticsService {

    private final DisposalStatisticsRepository disposalStatisticsRepository;
    private final StoreRepository storeRepository;

    // 1. 전체 직영점의 전체 발주 대비 통폐기율
    public double getOrderDisposalRateUntilNow(
            LocalDate startDate, LocalDate endDate) {

        return disposalStatisticsRepository
                .getOrderDisposalRateUntilNow(startDate, endDate);
    }

    // 2. 직영점별 발주 대비 통폐기율
    public List<StoreOrderDisposalRateDTO> getOrderDisposalRatesByStore(
            LocalDate startDate, LocalDate endDate) {

        return disposalStatisticsRepository
                .getOrderDisposalRatesByStore(startDate, endDate)
                .stream()
                .map(this::toStoreOrderDisposalRateDTO)
                .toList();
    }

    // 3. 특정 직영점 식재료별 발주 대비 통폐기율
    public List<IngredientDisposalRateToOrderByStoreDTO> getStoreIngredientDisposalRates(
            Long storeId, LocalDate startDate, LocalDate endDate) {

        return disposalStatisticsRepository
                .getStoreIngredientDisposalRates(storeId, startDate, endDate)
                .stream()
                .map(this::toIngredientDisposalRateToOrderByStoreDTO)
                .toList();
    }

    // 4. 전체 직영점 식재료별 폐기 비용
    public List<DisposalCostByIngredientDTO> getDisposalCostsByIngredient(
            LocalDate startDate, LocalDate endDate) {

        return disposalStatisticsRepository
                .getDisposalCostsByIngredient(startDate, endDate)
                .stream()
                .map(this::toDisposalCostByIngredientDTO)
                .toList();
    }

    // 5. 특정 직영점 식재료별 폐기 비용
    public List<DisposalCostByIngredientDTO> getDisposalCostsByIngredientForStore(
            Long storeId, LocalDate startDate, LocalDate endDate) {

        return disposalStatisticsRepository
                .getDisposalCostsByIngredientForStore(storeId, startDate, endDate)
                .stream()
                .map(this::toDisposalCostByIngredientDTO)
                .toList();
    }

    // 6. 직영점별 폐기 비용 순위
    public List<DisposalCostRankingStoreInfoDTO> getStoreDisposalCostRanking(
            LocalDate date) {

        return disposalStatisticsRepository
                .getStoreDisposalCostRanking(date)
                .stream()
                .map(this::toDisposalCostRankingStoreInfoDTO)
                .toList();
    }

    // 7. 일자별 폐기 비용 및 수량
    public List<DailyDisposalTotalDTO> getDailyDisposalTotalsForPeriod(
            LocalDate today, int day) {

        return disposalStatisticsRepository
                .getDailyDisposalTotalsForPeriod(today, day)
                .stream()
                .map(this::toDailyDisposalTotalDTO)
                .toList();
    }

    // 8. 특정 직영점 식재료별 발주 수량 대비 폐기 수량 비율
    public List<StoreIngredientDisposalQuantityRateDTO> getStoreIngredientDisposalQuantityRates(
            Long storeId) {

        return disposalStatisticsRepository
                .getStoreIngredientDisposalQuantityRates(storeId)
                .stream()
                .map(this::toStoreIngredientDisposalQuantityRateDTO)
                .toList();
    }

    // 9. 전체 직영점 식재료별 발주 수량 대비 폐기 수량 비율
    public List<IngredientDisposalQuantityRateDTO> getIngredientDisposalQuantityRates() {

        return disposalStatisticsRepository
                .getIngredientDisposalQuantityRates()
                .stream()
                .map(this::toIngredientDisposalQuantityRateDTO)
                .toList();
    }

    // 10. 특정 직영점 폐기 유형별 비율
    public List<StoreDisposalTypeRateDTO> getDisposalTypeRatesByStore(
            Long storeId) {

        return disposalStatisticsRepository
                .getDisposalTypeRatesByStore(storeId)
                .stream()
                .map(this::toStoreDisposalTypeRateDTO)
                .toList();
    }

    // 11. 전체 직영점 폐기 유형별 비율
    public List<DisposalTypeRateDTO> getDisposalTypeRates() {

        return disposalStatisticsRepository
                .getDisposalTypeRates()
                .stream()
                .map(this::toDisposalTypeRateDTO)
                .toList();
    }

    public List<String> getStores() {
        return storeRepository.findAll().stream()
                .map(Stores::getStoreName)
                .toList();
    }

    // ==================== VO -> DTO 변환 ====================


    // 직영점별 발주 대비 통폐기율
    private StoreOrderDisposalRateDTO toStoreOrderDisposalRateDTO(StoreOrderDisposalRateVO vo) {

        return StoreOrderDisposalRateDTO.builder()
                .storeId(vo.getStoreId())
                .storeName(vo.getStoreName())
                .totalOrderCount(vo.getTotalOrderCount())
                .totalDisposalCount(vo.getTotalDisposalCount())
                .disposalRatePct(vo.getDisposalRatePct())
                .build();
    }

    // 특정 직영점 식재료별 발주 대비 통폐기율
    private IngredientDisposalRateToOrderByStoreDTO toIngredientDisposalRateToOrderByStoreDTO(
            IngredientDisposalRateToOrderByStoreVO vo) {

        return IngredientDisposalRateToOrderByStoreDTO.builder()
                .storeId(vo.getStoreId())
                .storeName(vo.getStoreName())
                .ingredientId(vo.getIngredientId())
                .ingredientName(vo.getIngredientName())
                .totalOrderCount(vo.getTotalOrderCount())
                .totalDisposalCount(vo.getTotalDisposalCount())
                .disposalRatePct(vo.getDisposalRatePct())
                .build();
    }

    // 전체 및 특정 직영점 식재료별 폐기 비용
    private DisposalCostByIngredientDTO toDisposalCostByIngredientDTO(
            DisposalCostByIngredientVO vo) {

        return DisposalCostByIngredientDTO.builder()
                .ingredientId(vo.getIngredientId())
                .ingredientName(vo.getIngredientName())
                .totalDisposalPrice(vo.getTotalDisposalPrice())
                .totalDisposalQuantity(vo.getTotalDisposalQuantity())
                .build();
    }

    // 직영점별 폐기 비용 순위
    private DisposalCostRankingStoreInfoDTO toDisposalCostRankingStoreInfoDTO(
            DisposalCostRankingStoreInfoVO vo) {

        return DisposalCostRankingStoreInfoDTO.builder()
                .storeId(vo.getStoreId())
                .storeName(vo.getStoreName())
                .totalDisposalPrice(vo.getTotalDisposalPrice())
                .totalDisposalQuantity(vo.getTotalDisposalQuantity())
                .build();
    }

    // 일자별 폐기 비용 및 수량
    private DailyDisposalTotalDTO toDailyDisposalTotalDTO(
            DailyDisposalTotalVO vo) {

        return DailyDisposalTotalDTO.builder()
                .disposalDate(vo.getDisposalDate())
                .disposalCount(vo.getDisposalCount())
                .totalDisposalQuantity(vo.getTotalDisposalQuantity())
                .totalDisposalPrice(vo.getTotalDisposalPrice())
                .build();
    }

    // 특정 직영점 식재료별 발주 수량 대비 폐기 수량 비율
    private StoreIngredientDisposalQuantityRateDTO toStoreIngredientDisposalQuantityRateDTO(
            StoreIngredientDisposalQuantityRateVO vo) {

        return StoreIngredientDisposalQuantityRateDTO.builder()
                .storeId(vo.getStoreId())
                .storeName(vo.getStoreName())
                .ingredientId(vo.getIngredientId())
                .ingredientName(vo.getIngredientName())
                .orderUnit(vo.getOrderUnit())
                .totalOrderQuantity(vo.getTotalOrderQuantity())
                .totalDisposalQuantity(vo.getTotalDisposalQuantity())
                .disposalRatePct(vo.getDisposalRatePct())
                .build();
    }

    // 전체 직영점 식재료별 발주 수량 대비 폐기 수량 비율
    private IngredientDisposalQuantityRateDTO toIngredientDisposalQuantityRateDTO(
            IngredientDisposalQuantityRateVO vo) {

        return IngredientDisposalQuantityRateDTO.builder()
                .ingredientId(vo.getIngredientId())
                .ingredientName(vo.getIngredientName())
                .orderUnit(vo.getOrderUnit())
                .totalOrderQuantity(vo.getTotalOrderQuantity())
                .totalDisposalQuantity(vo.getTotalDisposalQuantity())
                .disposalRatePct(vo.getDisposalRatePct())
                .build();
    }

    // 특정 직영점 폐기 유형별 비율
    private StoreDisposalTypeRateDTO toStoreDisposalTypeRateDTO(
            StoreDisposalTypeRateVO vo) {

        return StoreDisposalTypeRateDTO.builder()
                .storeId(vo.getStoreId())
                .storeName(vo.getStoreName())
                .disposalType(vo.getDisposalType())
                .disposalCount(vo.getDisposalCount())
                .totalDisposalCount(vo.getTotalDisposalCount())
                .disposalRate(vo.getDisposalRate())
                .disposalRatePct(vo.getDisposalRatePct())
                .build();
    }

    // 전체 직영점 폐기 유형별 비율
    private DisposalTypeRateDTO toDisposalTypeRateDTO(
            DisposalTypeRateVO vo) {

        return DisposalTypeRateDTO.builder()
                .disposalType(vo.getDisposalType())
                .disposalCount(vo.getDisposalCount())
                .totalDisposalCount(vo.getTotalDisposalCount())
                .disposalRate(vo.getDisposalRate())
                .disposalRatePct(vo.getDisposalRatePct())
                .build();
    }
}
