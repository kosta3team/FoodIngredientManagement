package kr.swdl.foodingredientmanagement.order.service;

import kr.swdl.foodingredientmanagement.dto.OrderDetailRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderStatusCountDTO;
import kr.swdl.foodingredientmanagement.repository.MBOrderDetailsRepository;
import kr.swdl.foodingredientmanagement.repository.MBOrderRepository;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import kr.swdl.foodingredientmanagement.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MBOrderRepository mbOrderRepository;

    @Autowired
    private MBOrderDetailsRepository mbOrderDetailsRepository;

    // 1. 발주 등록
    @Test
    void 발주_등록_테스트() {

        OrderRequestDTO order = OrderRequestDTO.builder()
                .orderAt(Timestamp.valueOf(LocalDateTime.now()))
                .appAt(null)
                .totalPrice(0)
                .userId(null)
                .storeId(1L)
                .build();
        List<OrderDetailRequestDTO> detailList = new ArrayList<>();

        OrderDetailRequestDTO detail1 = new OrderDetailRequestDTO().builder()
                .ingredientId(1L)
                .orderQuantity(10)
                .supplyPrice(2000)
                .orderPrice(20000)
                .build();

        OrderDetailRequestDTO detail2 = new OrderDetailRequestDTO().builder()
                .ingredientId(2L)
                .orderQuantity(5)
                .supplyPrice(3000)
                .orderPrice(15000)
                .build();
        detailList.add(detail1);
        detailList.add(detail2);

        Long orderId = orderService.registerOrder(order, detailList);

        int count = mbOrderDetailsRepository.countOrderDetails(orderId);
        Long totalPrice = mbOrderRepository.selectOrderTotalPrice(orderId);

        assertNotNull(orderId);
        assertEquals(2, count);
        assertEquals(35000L, totalPrice);

        log.info("발주 번호: {}", orderId);
        log.info("상세 품목 수: {}", count);
        log.info("발주 총금액: {}", totalPrice);
    }

    //2. 발주 조회
//    @Test
//    void 본사_발주_목록_조회() {
//
//        List<OrderRequestDTO> orderList = ordersService.findOrderList();
//
//        assertNotNull(orderList, "발주 목록이 null입니다.");
//
//        for (int i = 0; i < orderList.size(); i++) {
//            OrderRequestDTO order = orderList.get(i);
//
//            if (order == null) {
//                log.error("null 데이터 위치: {}", i);
//            } else {
//                log.info("발주번호: {}", order.getOrderId());
//            }
//        }
//    }

    @Test
    void 발주_상세_품목_조회() {

        // given
        Long orderId = 1L;

        // when
        List<OrderDetailVO> result =
                orderService.findOrderDetail(orderId);

        // then
        log.info("발주 상세 품목 조회 결과: {}", result);

        assertNotNull(result);
        log.info("조회된 상세 품목 수: {}", result.size());
    }

    @Test
    void 직영점_발주_요청_조회() {
        List<OrderListVO> result =
                mbOrderRepository.findOrderDetail(
                        4L,
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

    @Test
    void 매장별_발주_정보_조회() {
        Long storeId = 2L;

        List<OrderProgressVO> result =
                mbOrderRepository.findOrderProgress(storeId);

        assertNotNull(result);

        for (OrderProgressVO dto : result) {

            assertNotNull(dto.getOrderId());
            assertNotNull(dto.getOrderAt());
            assertNotNull(dto.getTotalPrice());
            assertNotNull(dto.getOrderStatus());

            log.info("발주번호: {}", dto.getOrderId());
            log.info("발주일: {}", dto.getOrderAt());
            log.info("총 가격: {}", dto.getTotalPrice());
            log.info("발주 상태: {}", dto.getOrderStatus());
            log.info("승인일: {}", dto.getAppAt());
            log.info("입고일: {}", dto.getStoreAt());
        }
    }

    @Test
    void 발주_상세_상태_조회() {

        List<OrderStatusVO> result = mbOrderRepository.findOrderStatusList();

        assertNotNull(result, "조회 결과 리스트가 null입니다.");
        assertFalse(result.isEmpty(), "조회 결과가 없습니다.");

        log.info("조회 건수: {}", result.size());

        for (OrderStatusVO dto : result) {

            assertNotNull(dto, "DTO 객체가 null입니다.");

            log.info("발주 번호: {}", dto.getOrderId());
            log.info("발주 상태: {}", dto.getOrderStatus());

            assertNotNull(dto.getOrderId(), "발주 번호가 null입니다.");
            assertNotNull(dto.getOrderStatus(), "발주 상태가 null입니다.");
        }
    }

    @Test
    void 전체_상태_조회() {
        OrderStatusCountDTO result = orderService.countOrderStatus();
        log.info("승인 전체 상태 조회 : {}", result.getApprovedCount());
        log.info("거절 전체 상태 조회 : {}", result.getRejectedCount());
        log.info("대기 전체 상태 조회 : {}", result.getWaitingCount());
    }

    // 3. 본사 승인
    @Test
    void 본사_발주_승인() {
        int result = orderService.approveOrder(22L);
        log.info("본사 발주 승인 : {}", result);
        assertEquals(1, result, "발주 승인 미처리");
    }

    // 4, 본사 거절
    @Test
    void 본사_발주_거절() {
        int result = orderService.rejectOrder(23L, "곰팡이");

        log.info("본사 거절 결과 : {}", result);

        assertEquals(1, result, "발주 거절 미처리 ");
    }

    // 5. 입고 처리(입고, 제조일자 변경)
    @Test
    void 직영점_입고_처리() {
        int result = orderService.updateOrderDate(35L, "2026-11-17", "2026-11-19");
        log.info("직영점 입고 처리 {}", result);

        assertEquals(1, result, "입고 날짜가 수정되지 않았습니다.");
    }

    // 1. 존재하지 않는 발주 상세 ID 승인
    @Test
    void 존재하지_않는_발주_상세_승인() {

        Long orderDetailId = 1239L;

        int result = orderService.approveOrder(orderDetailId);

        assertEquals(0, result,
                "존재하지 않는 발주 상세입니다. 수정할 수 없습니다.");

        log.info("승인 실패 테스트 - 상세 ID: {}", orderDetailId);
        log.info("승인 처리 결과: {}", result);
    }


    // 2. 존재하지 않는 발주 상세 거절
    @Test
    void 존재하지_않는_발주_상세_거절() {

        Long orderDetailId = 1239L;
        String rejectReason = "재고 부족";

        int result = orderService.rejectOrder(
                orderDetailId,
                rejectReason
        );

        assertEquals(0, result,
                "존재하지 않는 발주 상세입니다. 거절할 수 없습니다.");

        log.info("거절 실패 테스트 - 상세 ID: {}", orderDetailId);
        log.info("거절 처리 결과: {}", result);
    }


    // 3. 존재하지 않는 발주 상세 입고일자 수정
    @Test
    void 존재하지_않는_발주_상세_입고_처리() {

        Long orderDetailId = 9999799L;
        String manufactureAt = "2026-11-17";
        String storeAt = "2026-11-19";

        int result = orderService.updateOrderDate(
                orderDetailId,
                manufactureAt,
                storeAt
        );

        assertEquals(0, result,
                "존재하지 않는 발주 상세입니다. 입고될 수 없습니다.");

        log.info("입고 처리 실패 테스트 - 상세 ID: {}", orderDetailId);
        log.info("입고 처리 결과: {}", result);
    }

    // 4. 본사 거절
    @Test
    void 본사_발주_거절_() {

        Long orderDetailId = 2L; //
        String rejectReason = "곰팡이";

        int result = orderService.rejectOrder(orderDetailId, rejectReason);

        log.info("발주 거절 - 상세 ID: {}", orderDetailId);
        log.info("반려 사유: {}", rejectReason);
        log.info("반려 처리 결과: {}", result);

    }

    // 5. 입고 처리 -> 제조일자 + 식재료 기간

    @Test
    void 발주_입고_날짜_수정_성공() {

        Long orderDetailId = 1L;
        String manufactureAt = "2026-11-15";
        String storeAt = "2026-11-18";

        int result = orderService.updateOrderDate(
                orderDetailId,
                manufactureAt,
                storeAt
        );

        log.info("입고 날짜 수정 테스트");
        log.info("발주 상세 ID: {}", orderDetailId);
        log.info("제조일자: {}", manufactureAt);
        log.info("입고일자: {}", storeAt);
        log.info("수정 결과: {}", result);

    }
}
