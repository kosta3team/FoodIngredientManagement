package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.controller.request.DisposalAppInfoRequestDTO;
import kr.swdl.foodingredientmanagement.controller.response.DisposalDetailResponseDTO;
import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.service.DisposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;


@RestController
@RequiredArgsConstructor
@Slf4j
public class DisposalRestController {

    private final DisposalService disposalService;

    @GetMapping("/api/hqDisposalList")
    public ResponseEntity<?> getHqDisposalList(@ModelAttribute HqDisposalSerachFilterDTO filter, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok().body(disposalService.getHqDisposals(filter, 2L, page, size));
    }

    @GetMapping("/api/storeDisposalList")
    public ResponseEntity<?> getStoreDisposalList(@ModelAttribute StoreDisposalSerachFilterDTO filter, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        filter.setStoreId(4L);
        return ResponseEntity.ok().body(disposalService.getStoreDisposals(filter, page, size));
    }

    // 본사 승인/거절
    @PatchMapping("/api/hq/approve/{disposalId}")
    public void approveDisposal(@PathVariable("disposalId") Long disposalId, @RequestBody DisposalApproveRequestDTO disposalApproveRequestDTO) {
        disposalService.approveDisposal(2L, disposalId
                , DisposalDTO.builder()
                        .disposalId(disposalId)
                        .disposalStatus(disposalApproveRequestDTO.getDisposalStatus())
                        .rejectReason(disposalApproveRequestDTO.getRejectReason()).build());
    }

    // 폐기 확인 정보 등록
    @PatchMapping("/api/store/disposalApproveInfo/{disposalId}")
    public void updateDisposalAppInfo(@PathVariable("disposalId") Long disposalId, @ModelAttribute DisposalAppInfoRequestDTO disposalAppInfoRequestDTO) {
        log.info("dump : {}", disposalAppInfoRequestDTO);
        disposalService.updateDisposalAppInfo(4L, disposalId, DisposalAppInfoDTO.builder()
                .disposalImg(disposalAppInfoRequestDTO.getDisposalImg()).build());
    }

    // 삭제
    @DeleteMapping("/api/store/disposal/{disposalId}")
    public void deleteDisposal(@PathVariable("disposalId") Long disposalId) {
        disposalService.deleteDisposal(disposalId);
    }


    @GetMapping("/api/disposal/{disposalId}")
    public ResponseEntity<?> getDisposalDetailByDisposalId(@PathVariable("disposalId") Long disposalId) {
        DisposalDetailDTO dto = disposalService.getDisposalDetailByDisposalId(disposalId);
        return ResponseEntity.ok(DisposalDetailResponseDTO.builder()
                .disposalId(dto.getDisposalId())
                .orderDetailId(dto.getOrderDetailId())
                .disposalUnit(dto.getDisposalUnit())
                .disposalStatus(dto.getDisposalStatus())
                .storeName(dto.getStoreName())
                .ingredientName(dto.getIngredientName())
                .processingMethod(dto.getProcessingMethod())
                .disposalQuantity(dto.getDisposalQuantity())
                .orderUnit(dto.getOrderUnit())
                .disposalReqAt(dto.getDisposalReqAt())
                .reqUserId(dto.getReqUserId())
                .reqUserName(dto.getReqUserName())
                .reqUserRole(dto.getReqUserRole())
                .disposalAppAt(dto.getDisposalAppAt())
                .appUserId(dto.getAppUserId())
                .appUserName(dto.getAppUserName())
                .appUserRole(dto.getAppUserRole())
                .disposalType(dto.getDisposalType())
                .disposalReason(dto.getDisposalReason())
                .disposalDetailReason(dto.getDisposalDetailReason())
                .rejectReason(dto.getRejectReason())
                .supplyPrice(dto.getSupplyPrice())
                .disposalPrice(dto.getDisposalPrice())
                .disposalAppInfoId(dto.getDisposalAppInfoId())
                .checkAt(dto.getCheckAt())
                .checkUserId(dto.getCheckUserId())
                .checkUserName(dto.getCheckUserName())
                .checkUserRole(dto.getCheckUserRole())
                .disposalImg(dto.getDisposalImg())
                .role("STORE")
                .build());
    }
}
