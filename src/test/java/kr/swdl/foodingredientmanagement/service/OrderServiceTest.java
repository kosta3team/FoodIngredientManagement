package kr.swdl.foodingredientmanagement.service;

import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.controller.request.OrderDetailDateUpdateDTO;
import kr.swdl.foodingredientmanagement.dto.OrderDetailRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderStatusCountDTO;
import kr.swdl.foodingredientmanagement.dto.OrderStoreSearchDTO;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
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
                .orderQuantity(51)
                .supplyPrice(3000)
                .orderPrice(15000)
                .build();
        detailList.add(detail1);
        detailList.add(detail2);

        Long orderId = orderService.registerOrder(order, detailList);

        int count = orderService.countOrderDetails(orderId);
        Long totalPrice = orderService.selectOrderTotalPrice(orderId);

        assertNotNull(orderId);
        assertEquals(2, count);
        assertEquals(35000L, totalPrice);

        log.info("발주 번호: {}", orderId);
        log.info("상세 품목 수: {}", count);
        log.info("발주 총금액: {}", totalPrice);
    }



    @Test
    void 재고_초과_발주_등록_실패() {

        // given
        OrderRequestDTO order = OrderRequestDTO.builder()
                .orderAt(Timestamp.valueOf(LocalDateTime.now()))
                .totalPrice(0)
                .storeId(1L)
                .build();

        List<OrderDetailRequestDTO> detailList = new ArrayList<>();

        OrderDetailRequestDTO detail = OrderDetailRequestDTO.builder()
                .ingredientId(1L)
                .orderQuantity(999999)
                .supplyPrice(2000)
                .orderPrice(1999998000)
                .build();

        detailList.add(detail);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.registerOrder(order, detailList);
        });

        log.info("재고 초과 발주 등록 차단 테스트 통과");
    }

    //2. 발주 조회

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
    void 특정직영점_발주_조회() {

        OrderStoreSearchDTO search = new OrderStoreSearchDTO();
        search.setStoreId(1L);
        search.setPage(1);
        search.setSize(8);

        // when
        PageInfo<OrderRequestDTO> result =
                orderService.getStoreOrderList(search);

        // then
        log.info("===== 특정 직영점 발주 목록 조회 테스트 =====");
        log.info("테스트 직영점 ID: {}", search.getStoreId());
        log.info("전체 데이터: {}", result.getTotal());
        log.info("현재 페이지: {}", result.getPageNum());

        assertNotNull(result);
        assertNotNull(result.getList());
        assertTrue(result.getTotal() > 0);

        for (OrderRequestDTO order : result.getList()) {

            log.info("발주번호: {}", order.getOrderId());
            log.info("직영점 ID: {}", order.getStoreId());
            log.info("발주상태: {}", order.getOrderStatus());

            assertEquals(2L, order.getStoreId());
        }
    }

    @Test
    void 매장별_발주_정보_조회() {

        // given
        Long storeId = 2L;

        // when
        List<OrderProgressVO> result =
                orderService.getOrderProgress(storeId);

        // then
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
    void 발주번호별_품목내역_조회() {

        Long orderId = 9L;
        Long storeId = 2L;

        log.info("===== 발주 품목 상세 Service 테스트 =====");

        List<Map<String, Object>> result =
                orderService.getOrderDetailItems(orderId, storeId);

        assertNotNull(result, "Service 조회 결과는 null이면 안 됩니다.");
        assertTrue(result.size() > 0, "품목 내역이 존재해야 합니다.");

        log.info("Service 반환 품목 수: {}", result.size());

        for (Map<String, Object> item : result) {
            log.info("Service 반환 데이터: {}", item);
        }

        log.info("===== Service 테스트 종료 =====");
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

        OrderDetailDateUpdateDTO dto = new OrderDetailDateUpdateDTO();

        dto.setOrderDetailId(35L);
        dto.setManufactureAt(Timestamp.valueOf("2026-11-17 00:00:00"));
        dto.setStoreAt(Timestamp.valueOf("2026-11-19 00:00:00"));

        int result = orderService.updateOrderDate(dto);

        log.info("직영점 입고 처리 {}", result);

        assertEquals(1, result, "입고 날짜가 수정되지 않았습니다.");
    }





    // 5. 입고 처리 -> 제조일자 + 식재료 기간


    @Test
    void 발주목록_페이지네이션_조회() {

        int page = 2;
        int size = 10;

        PageInfo<OrderRequestDTO> result =
                orderService.getOrderList(
                        2L,
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

