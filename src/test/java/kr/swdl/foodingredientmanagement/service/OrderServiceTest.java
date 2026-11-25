package kr.swdl.foodingredientmanagement.service;

import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.dto.OrderDetailRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderStatusCountDTO;
import kr.swdl.foodingredientmanagement.repository.OrderDetailsRepository;
import kr.swdl.foodingredientmanagement.repository.OrderRepository;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

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

        int count = orderDetailsRepository.countOrderDetails(orderId);
        Long totalPrice = orderRepository.selectOrderTotalPrice(orderId);

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
    void 발주상세조회_전체상태확인() {

        Long orderId = 13L;

        OrderInfoVO result =
                orderService.getOrderInfoByOrderId(orderId);

        log.info("발주번호: {}", result.getOrderId());
        log.info("발주 전체 상태: {}", result.getOrderStatus());

        assertNotNull(result);
        assertEquals("부분", result.getOrderStatus());
    }
    @Test
    void 발주_상세_품목_조회() {

        // given
        Long orderId = 1L;

        // when
        List<OrderDetailListVO> result =
                orderService.getOrderDetail(orderId);

        // then
        log.info("발주 상세 품목 조회 결과: {}", result);

        assertNotNull(result);
        log.info("조회된 상세 품목 수: {}", result.size());
    }

    @Test
    void 직영점_발주_요청_조회() {
        List<OrderListVO> result =
                orderRepository.getOrderDetail(
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
                orderRepository.getOrderProgress(storeId);

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

        List<OrderStatusVO> result = orderRepository.getOrderStatusList();

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
    @Test
    void 승인_대기_거절_상태_결과값_테스트() {

        // 1. 대기 품목이 하나라도 있으면 대기
        String result1 = orderService.calculateOrderStatus(
                List.of("승인", "대기", "거절")
        );

        // 2. 전부 승인
        String result2 = orderService.calculateOrderStatus(
                List.of("승인", "승인")
        );

        // 3. 전부 거절
        String result3 = orderService.calculateOrderStatus(
                List.of("거절", "거절")
        );

        // 4. 승인과 거절이 섞이면 부분
        String result4 = orderService.calculateOrderStatus(
                List.of("승인", "거절")
        );

        log.info("대기 포함 결과: {}", result1);
        log.info("전체 승인 결과: {}", result2);
        log.info("전체 거절 결과: {}", result3);
        log.info("승인 + 거절 결과: {}", result4);

        assertEquals("대기", result1);
        assertEquals("승인", result2);
        assertEquals("거절", result3);
        assertEquals("부분", result4);
    }



    // 5. 입고 처리(입고, 제조일자 변경)
    @Test
    void 직영점_입고_처리() {
        int result = orderService.updateOrderDate(35L, "2026-11-17", "2026-11-19");
        log.info("직영점 입고 처리 {}", result);

        assertEquals(1, result, "입고 날짜가 수정되지 않았습니다.");
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
    @Test
    void 발주목록_페이지네이션_조회() {

        int page = 2;
        int size = 10;

        PageInfo<OrderRequestDTO> result =
                orderService.getOrderList(
                        page,
                        size,
                        null,
                        null,
                        null,
                        null,
                        null,
                        "orderAt"
                );

        log.info("현재 페이지 : {}", result.getPageNum());
        log.info("페이지당 조회 건수 : {}", result.getPageSize());
        log.info("전체 데이터 : {}", result.getTotal());
        log.info("전체 페이지 : {}", result.getPages());
        log.info("현재 페이지 데이터 : {}", result.getList().size());

        assertEquals(page, result.getPageNum());
        assertEquals(size, result.getPageSize());
        assertTrue(result.getList().size() <= size);
    }

    @Test
    void 승인과거절이섞인발주조회() {
        // given
        Long userId = 2L;

        // when
        List<OrderListVO> result =
                orderRepository.getOrderDetail(
                        userId, null, null, "orderAt"
                );

        // then
        OrderListVO order = result.stream()
                .filter(item -> item.getOrderId().equals(13L))
                .findFirst()
                .orElseThrow();

        log.info("발주번호: {}", order.getOrderId());
        log.info("발주 전체 상태: {}", order.getOrderStatus());

        assertEquals("부분", order.getOrderStatus());
    }
}

