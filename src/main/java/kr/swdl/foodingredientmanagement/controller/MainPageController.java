package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.service.DisposalStaticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final DisposalStaticsService disposalStaticsService;

    @GetMapping
    public String mainPage(Model model) {
        LocalDate today = LocalDate.now();

        // 1. 전체 직영점의 전체 발주 대비 통폐기율
        double orderDisposalRate =
                disposalStaticsService.getOrderDisposalRateUntilNow(null, null);

        // 2. 직영점별 발주 대비 통폐기율
        List<StoreOrderDisposalRateDTO> storeOrderDisposalRates =
                disposalStaticsService.getOrderDisposalRatesByStore(null, null);

        // 6. 금일 직영점별 폐기 비용 순위
        List<DisposalCostRankingStoreInfoDTO> storeDisposalCostRanking =
                disposalStaticsService.getStoreDisposalCostRanking(today);

        // 기본 직영점 지정
        Long storeId = storeDisposalCostRanking.stream()
                .map(DisposalCostRankingStoreInfoDTO::getStoreId)
                .min(Long::compareTo)
                .orElse(null);

        // 3. 특정 직영점 식재료별 발주 대비 통폐기율
        List<IngredientDisposalRateToOrderByStoreDTO> storeIngredientDisposalRates =
                storeId == null ? List.of() :
                        disposalStaticsService.getStoreIngredientDisposalRates(
                                storeId, null, null
                        );

        // 4. 전체 직영점 식재료별 폐기 비용
        List<DisposalCostByIngredientDTO> disposalCostsByIngredient =
                disposalStaticsService.getDisposalCostsByIngredient(null, null);

        // 5. 특정 직영점 식재료별 폐기 비용
        List<DisposalCostByIngredientDTO> disposalCostsByIngredientForStore =
                storeId == null ? List.of() :
                        disposalStaticsService.getDisposalCostsByIngredientForStore(
                                storeId, null, null
                        );

        // 7. 최근 7일간 일자별 폐기 비용 및 수량
        List<DailyDisposalTotalDTO> dailyDisposalTotals =
                disposalStaticsService.getDailyDisposalTotalsForPeriod(today, 7);

        // 8. 특정 직영점 식재료별 수량 폐기율
        List<StoreIngredientDisposalQuantityRateDTO> storeIngredientDisposalQuantityRates =
                storeId == null ? List.of() :
                        disposalStaticsService.getStoreIngredientDisposalQuantityRates(storeId);

        // 9. 전체 직영점 식재료별 수량 폐기율
        List<IngredientDisposalQuantityRateDTO> ingredientDisposalQuantityRates =
                disposalStaticsService.getIngredientDisposalQuantityRates();

        // 10. 특정 직영점 폐기 유형별 비율
        List<StoreDisposalTypeRateDTO> disposalTypeRatesByStore =
                storeId == null ? List.of() :
                        disposalStaticsService.getDisposalTypeRatesByStore(storeId);

        // 11. 전체 직영점 폐기 유형별 비율
        List<DisposalTypeRateDTO> disposalTypeRates =
                disposalStaticsService.getDisposalTypeRates();


        // ==================== 상단 카드 데이터 ====================

        long totalDisposalPriceForWeek = dailyDisposalTotals.stream()
                .mapToLong(DailyDisposalTotalDTO::getTotalDisposalPrice)
                .sum();

        long totalDisposalCountForWeek = dailyDisposalTotals.stream()
                .mapToLong(DailyDisposalTotalDTO::getDisposalCount)
                .sum();

        long totalDisposalPriceForToday = storeDisposalCostRanking.stream()
                .mapToLong(DisposalCostRankingStoreInfoDTO::getTotalDisposalPrice)
                .sum();

        long totalOrderCount = storeOrderDisposalRates.stream()
                .mapToLong(StoreOrderDisposalRateDTO::getTotalOrderCount)
                .sum();

        long totalDisposalCount = storeOrderDisposalRates.stream()
                .mapToLong(StoreOrderDisposalRateDTO::getTotalDisposalCount)
                .sum();


        // ==================== 최근 7일 차트 데이터 ====================

        List<LocalDate> dailyDates = new ArrayList<>();
        List<Long> dailyPrices = new ArrayList<>();
        List<Long> dailyQuantities = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {

            LocalDate date = today.minusDays(i);

            // [수정] Timestamp를 LocalDate로 변환하여 비교
            DailyDisposalTotalDTO daily = dailyDisposalTotals.stream()
                    .filter(dto -> date.equals(
                            dto.getDisposalDate().toLocalDateTime().toLocalDate()
                    ))
                    .findFirst()
                    .orElse(null);

            dailyDates.add(date);

            dailyPrices.add(
                    daily == null ? 0L : (long) daily.getTotalDisposalPrice()
            );

            dailyQuantities.add(
                    daily == null ? 0L : (long) daily.getTotalDisposalQuantity()
            );
        }

        long maxDailyPrice = Math.max(
                1L,
                dailyPrices.stream().mapToLong(Long::longValue).max().orElse(0L)
        );

        // [추가] 폐기 수량 그래프 최댓값
        long maxDailyQuantity = Math.max(
                1L,
                dailyQuantities.stream().mapToLong(Long::longValue).max().orElse(0L)
        );


        // ==================== Model ====================

        model.addAttribute("today", today);
        model.addAttribute("selectedStoreId", storeId);
        model.addAttribute("storeList", storeDisposalCostRanking);

        // 통계 데이터 11개
        model.addAttribute("orderDisposalRate", orderDisposalRate);
        model.addAttribute("storeOrderDisposalRates", storeOrderDisposalRates);
        model.addAttribute("storeIngredientDisposalRates", storeIngredientDisposalRates);
        model.addAttribute("disposalCostsByIngredient", disposalCostsByIngredient);
        model.addAttribute("disposalCostsByIngredientForStore", disposalCostsByIngredientForStore);
        model.addAttribute("storeDisposalCostRanking", storeDisposalCostRanking);
        model.addAttribute("dailyDisposalTotals", dailyDisposalTotals);
        model.addAttribute("storeIngredientDisposalQuantityRates", storeIngredientDisposalQuantityRates);
        model.addAttribute("ingredientDisposalQuantityRates", ingredientDisposalQuantityRates);
        model.addAttribute("disposalTypeRatesByStore", disposalTypeRatesByStore);
        model.addAttribute("disposalTypeRates", disposalTypeRates);

        // 상단 카드
        model.addAttribute("totalDisposalPriceForWeek", totalDisposalPriceForWeek);
        model.addAttribute("totalDisposalCountForWeek", totalDisposalCountForWeek);
        model.addAttribute("totalDisposalPriceForToday", totalDisposalPriceForToday);
        model.addAttribute("totalOrderCount", totalOrderCount);
        model.addAttribute("totalDisposalCount", totalDisposalCount);

        // 차트
        model.addAttribute("dailyDates", dailyDates);
        model.addAttribute("dailyPrices", dailyPrices);
        model.addAttribute("dailyQuantities", dailyQuantities);
        model.addAttribute("maxDailyPrice", maxDailyPrice);

        // [추가] 폐기 수량 그래프 최댓값 전달
        model.addAttribute("maxDailyQuantity", maxDailyQuantity);

        // 직영점 이름 목록
        model.addAttribute("storeNameList", disposalStaticsService.getStores());

        return "index";
    }
}