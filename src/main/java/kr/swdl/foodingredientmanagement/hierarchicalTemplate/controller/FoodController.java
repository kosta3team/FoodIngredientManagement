package kr.swdl.foodingredientmanagement.hierarchicalTemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FoodController {
    @GetMapping("/dashboard")
    public String dashboard() {

        return "dashboard";
    }
}
