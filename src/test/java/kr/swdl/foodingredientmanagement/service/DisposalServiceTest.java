package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.vo.DisposalVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@SpringBootTest
@Slf4j
public class DisposalServiceTest {

    @Autowired
    DisposalService disposalService;

    @Test
    @Transactional
    public void 본사_폐기_등록_성공() {
//        Long reqUserId, Long orderDetailId, DisposalsDTO disposalsDTO
        Long reqUserId = 2L;
        Long orderDetailId = 10L;

        DisposalDTO dto = DisposalDTO.builder()
                .reqUserId(reqUserId)
                .disposalUnit("통폐기")
                .disposalReqAt(new Timestamp(System.currentTimeMillis()))
                .reqUserId(2L)
                .disposalStatus("승인")
                .processingMethod("정기폐기")
                .disposalType("식재료 규칙")
                .disposalReason("유통기한 경과")
                .disposalQuantity(1)
                .orderDetailId(orderDetailId)
                .build();
        Assertions.assertTrue(disposalService.addDisposal(orderDetailId, dto));
    }

//    @Test
//    @Transactional
//    public void 본사_폐기_등록_실패() {
//        disposalService.addDisposal();
//    }
//
//    @Test
//    @Transactional
//    public void 직영점_폐기_등록_성공() {
//
//        disposalService.updateDisposal()
//
//    }
//
//    @Test
//    @Transactional
//    public void 직영점_폐기_등록_실패() {
//        disposalService.addDisposal();
//    }
//
//    @Test
//    @Transactional
//    public void 폐기_승인(){
//        disposalService.approveDisposal();
//    }
//
//    @Test
//    @Transactional
//    public void 폐기_거절() {
//        disposalService.approveDisposal();
//    }
//
//    @Test
//    @Transactional
//    public void 폐기_삭제_성공() {
//        disposalService.approveDisposal();
//    }
//
//
//    @Test
//    @Transactional
//    public void 폐기_삭제_실패() {
//        disposalService.approveDisposal();
//    }
//
//    @Test
//    public void 본사_폐기_목록_조회() {
//        disposalService.getHqDisposals();
//    }
//
//    @Test
//    public void 직영점_폐기_목록_조회() {
//        disposalService.getStoreDisposals();
//    }
//
//    @Test
//    public void 금일_폐기_개수() {
//        disposalService.countByTodayDisposed();
//    }
//
//    @Test
//    public void 금일_폐기_금액() {
//        disposalService.sumByTodayDisposedPrice();
//    }
//
//    @Test
//    public void 특정_직영점_확인필요_폐기_개수() {
//        disposalService.countNeedToConfirmByStoreId();
//    }
}
