package kr.swdl.foodingredientmanagement.order.repository;

import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.MBOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class MBOrderRepositoryTest {

    @Autowired
    private MBOrderRepository mbOrderRepository;

    @Test
    void 발주_상태_전체_조회(){
        OrderStatusCountDTO result = mbOrderRepository.countOrderStatus();

        log.info("대기 : {}", result.getWaitingCount());
        log.info("승인 : {}", result.getApprovedCount());
        log.info("거절 : {}", result.getRejectedCount());

        assertThat(result).isNotNull();
    }

    @Test
    void 발주_목록_조회() {

        List<OrderDTO> result =
                mbOrderRepository.findOrderList();
        for (OrderDTO order : result) {
            log.info("발주번호: {}", order.getOrderId());
            log.info("매장명: {}", order.getStoreName());
            log.info("대표 식자재: {}", order.getFirstIngredientName());
            log.info("식자재 종류 수: {}", order.getIngredientCount());
            log.info("총 발주 금액: {}", order.getTotalPrice());
            log.info("입고일: {}", order.getStoreAt());
        }
        assertThat(result).isNotEmpty();
    }

    @Test
    void 발주_상태_조회() {

        List<OrderStatusDTO> result =
                mbOrderRepository.findOrderStatusList();

        log.info("조회 결과 개수: {}", result.size());

        for (int i = 0; i < result.size(); i++) {

            OrderStatusDTO status = result.get(i);

            if (status == null) {
                log.error("조회 결과 {}번째 객체가 null입니다.", i);
                continue;
            }

            log.info("발주번호: {}, 발주상세번호: {}, 발주상태: {}",
                    status.getOrderId(),
                    status.getOrderDetailId(),
                    status.getOrderStatus());
        }
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void 발주_전체_조회() {

        List<OrderDTO> result =
                mbOrderRepository.findOrderList(
                        null, null, null, null, null
                );

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void 발주일_기준_날짜_검색() {

        List<OrderDTO> result =
                mbOrderRepository.findOrderList(
                        "2026-11-01",
                        "2026-11-12",
                        null,
                        null,
                        null
                );
        for (int i = 0; i < result.size(); i++) {

            OrderDTO order = result.get(i);
            Timestamp orderAt = order.getOrderAt();

            log.info("발주번호: {}, 발주일: {}",
                    order.getOrderId(), orderAt);

            Timestamp startDate =
                    Timestamp.valueOf("2026-11-01 00:00:00");

            Timestamp endDate =
                    Timestamp.valueOf("2026-11-13 00:00:00");
        }

        assertNotNull(result);
        assertFalse(result.isEmpty());

    }

    @Test
    void 발주_상태_검색() {

        List<OrderDTO> result =
                mbOrderRepository.findOrderList(
                        null, null, "대기", null, null
                );

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void 발주_직영점명_검색() {

        String keyword = "대";

        List<OrderDTO> result =
                mbOrderRepository.findOrderList(
                        null, null, null, keyword, null
                );

        assertNotNull(result);
    }

    @Test
    void 발주_번호_검색() {

        Long orderId = 1L;

        List<OrderDTO> result =
                mbOrderRepository.findOrderList(
                        null, null, null, null, orderId
                );

        assertEquals(1, result.size());
        assertEquals(orderId, result.get(0).getOrderId());
    }

    @Test
        void 발주_여러_조건_동시_검색() {

        List<OrderDTO> result =
                mbOrderRepository.findOrderList(
                        "2026-11-01",
                        "2026-11-12",
                        "대기",
                        "대",
                        null
                );

        log.info("여러 조건 동시 검색 결과 :{}",result);
        assertNotNull(result);
    }

    @Test
    void 발주_기본_정보_조회(){

        List<OrderInfoDTO> result =
                mbOrderRepository.selectOrderInfo();

        for (OrderInfoDTO detail : result){
            log.info("발주번호 {}", detail.getOrderId() + "의 정보 ");
            log.info("발주일시 : {}", detail.getOrderAt());
            log.info("직영점명 : {}", detail.getStoreName());
            log.info("상태는 디테일에서 처리");
            log.info("처리일시 : {}", detail.getAppAt());
            log.info("입고일시 : {}", detail.getStoreAt());
            log.info("총발주금액 : {}", detail.getTotalPrice());
            log.info("승인담당자 : {}", detail.getUserName());
            log.info("-----------------------------------------");
        }
        assertNotNull(result);
    }

    @Test
    void 발주_상세_품목_조회() {

        Long orderId = 3L;

        List<OrderDetailDTO> result =
                mbOrderRepository.selectOrderDetailList(orderId);
        for (OrderDetailDTO detail : result) {
            log.info("발주번호 "+ orderId+ "의 정보 ");
            log.info("식자재명: {}", detail.getIngredientName());
            log.info("발주 수량: {}", detail.getOrderQuantity());
            log.info("발주 단위: {}", detail.getOrderUnit());
            log.info("공급 가격: {}", detail.getSupplyPrice());
            log.info("발주 금액: {}", detail.getOrderPrice());
            log.info("발주 상태: {}", detail.getOrderStatus());
            log.info("거절 사유: {}", detail.getRejectReason());
            log.info("-----------------------------------------");
        }
    }

    @Test
    void 직영점_식자재_목록_조회() {

        List<IngredientListDTO> result =
                mbOrderRepository.findIngredients();

        for (IngredientListDTO i : result) {
            log.info("식자재명: {}", i.getIngredientName());
            log.info("공급가격: {}", i.getSupplyPrice());
            log.info("공급수량: {}", i.getSupplyQuantity());

        }

    }
    @Test
    void 발주_상세_승인(){
        int result = mbOrderRepository.approveOrderDetail(34L);
        log.info("승인 처리 결과 : {}",result);
        assertEquals(1, result);
    }

    @Test
    void 발주_상세_거절_(){
        int result = mbOrderRepository.rejectOrderDetail(
                34L,"박스가 비에 젖어 상태불량"
        );
        log.info("거절 처리 결과 : {}", result);
        assertEquals(1, result);
    }

    @Test
    void 직영점_입고_제조_변경(){
        int result = mbOrderRepository.updateOrderDetailsDates(
                34L,"2026-11-17","2026-11-19"
        );
        log.info("입고, 제조일 변경 결과 : {}", result);
        assertEquals(1,result);
    }


}
