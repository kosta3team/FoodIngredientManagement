package kr.swdl.foodingredientmanagement.controller;

import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.vo.OrderDetailListVO;
import kr.swdl.foodingredientmanagement.repository.vo.OrderInfoVO;
import kr.swdl.foodingredientmanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HQOrderRestController {
    private final OrderService orderService;

    @GetMapping("/api/v1/hq/orders/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderDetail(@PathVariable("orderId") Long orderId){
        log.info("orderId : {}",orderId);
        OrderInfoVO orderInfo = orderService.getOrderInfoByOrderId(orderId);
        log.info("orderInfo: {}",orderInfo);
        List<OrderDetailListVO> orderDetails = orderService.getOrderDetail(orderId);
        log.info("orderDetail : {}",orderDetails);
        Map<String, Object> result = new HashMap<>();
        result.put("info",orderInfo);
        result.put("items",orderDetails);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/v1/hq/orders")
    public ResponseEntity<PageInfo<OrderRequestDTO>> getOrderList(
            @ModelAttribute OrderSearchDTO search) {

        String storeName = null;
        Long orderId = null;

        String searchType = search.getSearchType();
        String searchKeyword = search.getSearchKeyword();

        // 검색어가 입력된 경우
        if (searchKeyword != null && !searchKeyword.isBlank()) {

            searchKeyword = searchKeyword.trim();

            // 발주번호 검색
            if ("orderId".equals(searchType)) {

                try {
                    orderId = Long.valueOf(searchKeyword);

                } catch (NumberFormatException e) {
                    log.warn("잘못된 발주번호 검색: {}", searchKeyword);
                    return ResponseEntity.badRequest().build();
                }

                // 지점명 검색
            } else if ("storeName".equals(searchType)) {

                storeName = searchKeyword;

                // 전체 검색
            } else if ("all".equals(searchType)) {

                storeName = searchKeyword;

                // 숫자로만 구성된 경우 발주번호 검색도 함께 수행
                if (searchKeyword.matches("\\d+")) {
                    try {
                        orderId = Long.valueOf(searchKeyword);

                    } catch (NumberFormatException e) {
                        log.warn("발주번호 범위 초과: {}", searchKeyword);
                    }
                }
            }
        }

        // 검색 조건 로그
        log.info("===== 본사 발주 목록 조회 =====");
        log.info("searchType : {}", searchType);
        log.info("searchKeyword : {}", searchKeyword);
        log.info("storeName : {}", storeName);
        log.info("orderId : {}", orderId);
        log.info("page : {}", search.getPage());
        log.info("size : {}", search.getSize());
        log.info("startDate : {}", search.getStartDate());
        log.info("endDate : {}", search.getEndDate());
        log.info("dateType : {}", search.getDateType());
        log.info("orderStatus : {}", search.getOrderStatus());

        // 발주 목록 조회
        PageInfo<OrderRequestDTO> result = orderService.getOrderList(
                search.getPage(),
                search.getSize(),
                search.getStartDate(),
                search.getEndDate(),
                search.getOrderStatus(),
                storeName,
                orderId,
                search.getDateType()
        );

        return ResponseEntity.ok(result);
    }
}

