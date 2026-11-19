package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HQOrderController {
    private final OrderService orderService;

    @GetMapping("/hqOrderList")
    public String hqOrderList(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String dateType,
            @RequestParam(required = false) String orderStatus,
            Model model
    ) {

        String storeName = null;
        Long orderId = null;

        if (searchKeyword != null && !searchKeyword.isBlank()) {

            if ("orderId".equals(searchType)) {
                orderId = Long.valueOf(searchKeyword);

            } else if ("storeName".equals(searchType)) {
                storeName = searchKeyword;
            }
        }

        // 발주 목록 조회
        List<OrderRequestDTO> orderList =
                orderService.findOrderList(
                        startDate,
                        endDate,
                        orderStatus,
                        storeName,
                        orderId,
                        dateType
                );

        model.addAttribute("orderList", orderList);

        // 기존 요약 현황 유지
        model.addAttribute("orderStatusCount",
                orderService.countOrderStatus());

        // 검색 조건을 화면에 유지
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("dateType", dateType);
        model.addAttribute("orderStatus", orderStatus);

        return "order/hqOrderList";
    }
}
