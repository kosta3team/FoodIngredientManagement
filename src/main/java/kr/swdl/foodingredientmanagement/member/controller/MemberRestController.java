package kr.swdl.foodingredientmanagement.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberRestController {

    @GetMapping("/hq/test")
    public String hqTest() {
        return "본사 권한";
    }

    @GetMapping("/hq/admin/test")
    public String hqAdminTest() {
        return "본사 관리자 권한 ";
    }

    @GetMapping("/store/test")
    public String storeTest() {
        return "직영점 권한";
    }

    @GetMapping("/common/test")
    public String commonTest() {
        return "모두 접근";
    }
}
