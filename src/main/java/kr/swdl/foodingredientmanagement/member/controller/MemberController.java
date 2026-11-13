package kr.swdl.foodingredientmanagement.member.controller;

import kr.swdl.foodingredientmanagement.member.dto.request.MemberJoinRequest;
import kr.swdl.foodingredientmanagement.member.serivce.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/join")
    public String joinForm() {
        return "member/join";
    }

    @PostMapping("/join")
    public String join(MemberJoinRequest request) {

        try {
            memberService.join(request);
            return "redirect:/loginView?joinSuccess=true";
        } catch (IllegalArgumentException e) {
            return "redirect:/join?error=true";
        }
    }
}

