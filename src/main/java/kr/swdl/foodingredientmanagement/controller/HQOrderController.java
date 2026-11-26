package kr.swdl.foodingredientmanagement.controller;

import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderSearchDTO;
import kr.swdl.foodingredientmanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HQOrderController {
    private final OrderService orderService;

    @GetMapping("/hqOrderList")
    public String hqOrderList(@ModelAttribute OrderSearchDTO search, Model model) {
        model.addAttribute("orderStatusCount", orderService.countOrderStatus());
        return "order/hqOrderList";
    }
}

