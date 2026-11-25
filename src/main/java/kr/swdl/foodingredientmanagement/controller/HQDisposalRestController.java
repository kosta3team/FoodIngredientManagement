package kr.swdl.foodingredientmanagement.controller;

import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.dto.HQDisposalListDTO;
import kr.swdl.foodingredientmanagement.dto.HqDisposalSerachFilterDTO;
import kr.swdl.foodingredientmanagement.service.DisposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class HQDisposalRestController {

    private final DisposalService disposalService;

    @GetMapping("/api/hqDisposalList")
    public ResponseEntity<?> getHqDisposalList(@ModelAttribute HqDisposalSerachFilterDTO filter, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        log.info("filter dump : {}", filter);
        log.info("size dump : {}", size);
        return ResponseEntity.ok().body(disposalService.getHqDisposals(filter, 2L, page, size));
    }
}
