package kr.swdl.foodingredientmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/loginView")
    public String loginView(){
        return "login";
    }
}
