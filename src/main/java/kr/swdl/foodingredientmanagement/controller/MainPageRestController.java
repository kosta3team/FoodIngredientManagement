package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.dto.DisposalCostByIngredientDTO;
import kr.swdl.foodingredientmanagement.dto.IngredientDisposalRateToOrderByStoreDTO;
import kr.swdl.foodingredientmanagement.dto.StoreDisposalTypeRateDTO;
import kr.swdl.foodingredientmanagement.dto.StoreIngredientDisposalQuantityRateDTO;
import kr.swdl.foodingredientmanagement.service.DisposalStaticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/disposal-statistics")
public class MainPageRestController {

    private final DisposalStaticsService disposalStaticsService;


    // 특정 직영점 식재료별 폐기 비용
    @GetMapping("/stores/{storeId}/ingredient-costs")
    public List<DisposalCostByIngredientDTO>
    getDisposalCostsByIngredientForStore(
            @PathVariable("storeId") Long storeId
    ) {

        return disposalStaticsService
                .getDisposalCostsByIngredientForStore(
                        storeId,
                        null,
                        null
                );
    }


    // 특정 직영점 폐기 유형별 비율
    @GetMapping("/stores/{storeId}/type-rates")
    public List<StoreDisposalTypeRateDTO>
    getDisposalTypeRatesByStore(
            @PathVariable("storeId") Long storeId
    ) {

        return disposalStaticsService
                .getDisposalTypeRatesByStore(storeId);
    }


    // 특정 직영점 식재료별 수량 폐기율
    @GetMapping("/stores/{storeId}/ingredient-quantity-rates")
    public List<StoreIngredientDisposalQuantityRateDTO>
    getStoreIngredientDisposalQuantityRates(
            @PathVariable("storeId") Long storeId
    ) {

        return disposalStaticsService
                .getStoreIngredientDisposalQuantityRates(storeId);
    }


    // 특정 직영점 식재료별 건수 폐기율
    @GetMapping("/stores/{storeId}/ingredient-count-rates")
    public List<IngredientDisposalRateToOrderByStoreDTO>
    getStoreIngredientDisposalRates(
            @PathVariable("storeId") Long storeId
    ) {

        return disposalStaticsService
                .getStoreIngredientDisposalRates(
                        storeId,
                        null,
                        null
                );
    }
}
