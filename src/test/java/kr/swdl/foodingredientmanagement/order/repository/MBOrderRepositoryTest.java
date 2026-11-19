package kr.swdl.foodingredientmanagement.order.repository;

import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderStatusCountDTO;
import kr.swdl.foodingredientmanagement.repository.MBOrderDetailsRepository;
import kr.swdl.foodingredientmanagement.repository.MBOrderRepository;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class MBOrderRepositoryTest {

    @Autowired
    private MBOrderRepository mbOrderRepository;

    @Autowired
    private MBOrderDetailsRepository mbOrderDetailsRepository;

    @Test
    void 발주_등록(){
        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder()
                .orderAt(Timestamp.valueOf(LocalDateTime.now()))
                .storeName("강남점")
                .firstIngredientName("양파")
                .ingredientCount(10L)
                .totalPrice(0)
                .storeAt(Timestamp.valueOf(LocalDateTime.now()))
                .storeId(1L)
                .build();
        int result = mbOrderRepository.insertOrder(orderRequestDTO);
        log.info("발주 등록 결과 : {} 건",result);
        assertEquals(1, result);
    }

    @Test
    void 발주_상태_전체_조회(){
        OrderStatusCountDTO result = mbOrderDetailsRepository.countOrderStatus();

        log.info("대기 : {}", result.getWaitingCount());
        log.info("승인 : {}", result.getApprovedCount());
        log.info("거절 : {}", result.getRejectedCount());

        assertThat(result).isNotNull();
    }

//    @Test
//    void 발주_목록_조회() {
//
//        List<OrderRequestDTO> result =
////                mbOrderRepository.findOrderList("2026-11-01","2026-11-11","order","",2L, dateType);
//        for (OrderRequestDTO order : result) {
//            log.info("발주번호: {}", order.getOrderId());
//            log.info("매장명: {}", order.getStoreName());
//            log.info("대표 식자재: {}", order.getFirstIngredientName());
//            log.info("식자재 종류 수: {}", order.getIngredientCount());
//            log.info("총 발주 금액: {}", order.getTotalPrice());
//            log.info("입고일: {}", order.getStoreAt());
//        }
//        assertThat(result).isNotEmpty();
//    }

    @Test
    void 발주_상태_조회() {

        List<OrderStatusVO> result =
                mbOrderRepository.findOrderStatusList();

        log.info("조회 결과 개수: {}", result.size());

        for (int i = 0; i < result.size(); i++) {

            OrderStatusVO status = result.get(i);

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

//    @Test
//    void 발주_전체_조회() {
//
//        List<OrderRequestDTO> result =
//                mbOrderRepository.findOrderList(
//                        null, null, null, null, null,
//                        dateType);
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//    }

    @Test
    void 직영점_발주_요청_조회() {
        List<OrderListVO> result =
                mbOrderRepository.findOrderDetail(
                        1L,
                        "2026-11-01 00:00:00",
                        "2026-11-11 00:00:00",
                        "ORDER"
                );

        log.info("조회 결과: {}", result);

        assertNotNull(result);

        for (OrderListVO dto : result) {
            assertNotNull(dto.getOrderId());
            assertNotNull(dto.getOrderAt());

            log.info("발주번호: {}", dto.getOrderId());
            log.info("발주일: {}", dto.getOrderAt());
            log.info("식재료: {}", dto.getIngredientName());
            log.info("식재료 종류 수: {}", dto.getIngredientCount());
            log.info("총 가격: {}", dto.getTotalPrice());
            log.info("발주 상태: {}", dto.getOrderStatus());
            log.info("입고일: {}", dto.getStoreAt());
        }
    }

//    @Test
//    void 발주일_기준_날짜_검색() {
//
//        List<OrderRequestDTO> result =
//                mbOrderRepository.findOrderList(
//                        "2026-11-01",
//                        "2026-11-12",
//                        null,
//                        null,
//                        null,
//                        dateType);
//        for (int i = 0; i < result.size(); i++) {
//
//            OrderRequestDTO order = result.get(i);
//            Timestamp orderAt = order.getOrderAt();
//
//            log.info("발주번호: {}, 발주일: {}",
//                    order.getOrderId(), orderAt);
//
//            Timestamp startDate =
//                    Timestamp.valueOf("2026-11-01 00:00:00");
//
//            Timestamp endDate =
//                    Timestamp.valueOf("2026-11-13 00:00:00");
//        }
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//
//    }
//    @Test
//    void 대기_발주_목록_조회(){
//        List<OrderRequestDTO> result = mbOrderRepository.findOrderList(
//                null,       // startDate
//                null,       // endDate
//                "대기",     // orderStatus
//                null,       // storeName
//                null,        // orderId
//                dateType);
//
//        log.info("대기 발주 조회 결과: {}", result);
//
//        assertNotNull(result);
//
//        for (OrderRequestDTO dto : result) {
//            assertNotNull(dto.getOrderId());
//            assertNotNull(dto.getOrderAt());
//            assertNotNull(dto.getStoreName());
//
//            log.info("발주번호: {}", dto.getOrderId());
//            log.info("매장명: {}", dto.getStoreName());
//            log.info("식재료 요약: {}", dto.getFirstIngredientName());
//            log.info("식재료 종류: {}", dto.getIngredientCount());
//            log.info("총금액: {}", dto.getTotalPrice());
//            log.info("입고일: {}", dto.getStoreAt());
//        }
//    }
//
//
//    @Test
//    void 발주_상태_검색() {
//
//        List<OrderRequestDTO> result =
//                mbOrderRepository.findOrderList(
//                        null, null, "대기", null, null,
//                        dateType);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    void 발주_직영점명_검색() {
//
//        String keyword = "대";
//
//        List<OrderRequestDTO> result =
//                mbOrderRepository.findOrderList(
//                        null, null, null, keyword, null,
//                        dateType);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    void 발주_번호_검색() {
//
//        Long orderId = 1L;
//
//        List<OrderRequestDTO> result =
//                mbOrderRepository.findOrderList(
//                        null, null, null, null, orderId,
//                        dateType);
//
//        assertEquals(1, result.size());
//        assertEquals(orderId, result.get(0).getOrderId());
//    }
//
//    @Test
//        void 발주_여러_조건_동시_검색() {
//
//        List<OrderRequestDTO> result =
//                mbOrderRepository.findOrderList(
//                        "2026-11-01",
//                        "2026-11-12",
//                        "대기",
//                        "대",
//                        null,
//                        dateType);
//
//        log.info("여러 조건 동시 검색 결과 :{}",result);
//        assertNotNull(result);
//    }

    @Test
    void 발주_기본_정보_조회(){

        List<OrderInfoVO> result =
                mbOrderRepository.selectOrderInfo();

        for (OrderInfoVO detail : result){
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

//    @Test
//    void 발주_상세_품목_조회() {
//
//        Long orderId = 3L;
//
//        List<OrderDetailVO> result =
//                mbOrderRepository.selectOrderDetailList(orderId);
//        for (OrderDetailVO detail : result) {
//            log.info("발주번호 "+ orderId+ "의 정보 ");
//            log.info("식자재명: {}", detail.getIngredientName());
//            log.info("발주 수량: {}", detail.getOrderQuantity());
//            log.info("발주 단위: {}", detail.getOrderUnit());
//            log.info("공급 가격: {}", detail.getSupplyPrice());
//            log.info("발주 금액: {}", detail.getOrderPrice());
//            log.info("발주 상태: {}", detail.getOrderStatus());
//            log.info("거절 사유: {}", detail.getRejectReason());
//            log.info("-----------------------------------------");
//        }
//    }

    @Test
    void 직영점_식자재_목록_조회() {


    }
    @Test
    void 발주_상세_승인(){
        int result = mbOrderDetailsRepository.approveOrderDetail(34L);
        log.info("승인 처리 결과 : {}",result);
        assertEquals(1, result);
    }

    @Test
    void 발주_상세_거절_(){
        int result = mbOrderDetailsRepository.rejectOrderDetail(
                34L,"박스가 비에 젖어 상태불량"
        );
        log.info("거절 처리 결과 : {}", result);
        assertEquals(1, result);
    }

    @Test
    void 직영점_입고_제조_변경(){
        int result = mbOrderDetailsRepository.updateOrderDetailsDates(
                34L,"2026-11-17","2026-11-19"
        );
        log.info("입고, 제조일 변경 결과 : {}", result);
        assertEquals(1,result);
    }

    @Test
    void 본사직원_담당매장_발주목록_조회() {

        Long userId = 2L;

        List<OrderListVO> result =
                mbOrderRepository.findOrderDetail(
                        userId, null, null, null
                );

        result.forEach(order ->
                log.info("발주번호: {}, 발주일: {}, 식재료: {}, 종류: {}, 금액: {}, 상태: {}",
                        order.getOrderId(),
                        order.getOrderAt(),
                        order.getIngredientName(),
                        order.getIngredientCount(),
                        order.getTotalPrice(),
                        order.getOrderStatus())
        );

        assertNotNull(result);
    }

//    @Test
//    void 본사_매장기준_발주목록_조회() {
//
//        List<OrderRequestDTO> result = mbOrderRepository.findOrderList(
//                null,
//                null,
//                null,
//                null,
//                null,
//                dateType);
//
//        log.info("조회된 발주 건수: {}", result.size());
//
//        assertNotNull(result);
//
//        for (OrderRequestDTO order : result) {
//            log.info("발주번호: {}", order.getOrderId());
//            log.info("매장번호: {}", order.getStoreId());
//            log.info("매장명: {}", order.getStoreName());
//
//            assertNotNull(order.getOrderId());
//            assertNotNull(order.getStoreId());
//        }
//    }
}
