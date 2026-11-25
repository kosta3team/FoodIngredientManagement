package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.dto.HqDisposalSerachFilterDTO;
import kr.swdl.foodingredientmanagement.service.DisposalCategoryService;
import kr.swdl.foodingredientmanagement.service.DisposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HQDisposalController {

    private final DisposalService disposalService;
    private final DisposalCategoryService disposalCategoryService;

    @GetMapping("/hqDisposalList")
    public String disposalList(Model model, @ModelAttribute HqDisposalSerachFilterDTO filter, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        // TODO : 페이지가 처음 열리는 경우에는 본인 아이디로만 접근하면 된다. 다른 필터가 필요없다. 메서드 오버로딩할것.
        model.addAttribute("disposalTypeList", disposalCategoryService.getDisposalTypes());
        model.addAttribute("list", disposalService.getHqDisposals(filter, 2L, page, size));
        return "disposal/hqDisposalList";
    }
}
