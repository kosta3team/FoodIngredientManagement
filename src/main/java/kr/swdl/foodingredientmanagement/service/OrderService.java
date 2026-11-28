package kr.swdl.foodingredientmanagement.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.controller.request.OrderDetailDateUpdateDTO;
import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.OrderDetailsRepository;
import kr.swdl.foodingredientmanagement.repository.OrderRepository;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderDetailsRepository orderDetailsRepository;

    @Transactional
    public Long registerOrder(OrderRequestDTO orderDTO, List<OrderDetailRequestDTO> detailList) {

        log.info("===== 발주 등록 시작 =====");

        // 1. 발주 품목 및 수량 검증
        if (detailList == null || detailList.isEmpty()) {
            throw new IllegalArgumentException("발주 품목이 존재하지 않습니다.");
        }

        for (OrderDetailRequestDTO detail : detailList) {

            Long ingredientId = detail.getIngredientId();
            Integer orderQuantity = detail.getOrderQuantity();
            Integer supplyQuantity = detail.getSupplyQuantity();
            Integer currentQuantity =
                    orderRepository.selectCurrentQuantity(ingredientId);

            log.info("식재료 ID: {}", ingredientId);
            log.info("잔여 재고: {}", supplyQuantity);
            log.info("현재 재고: {}", currentQuantity);
            log.info("주문 수량: {}", orderQuantity);

            if (currentQuantity == null) {
                throw new IllegalArgumentException(
                        "존재하지 않는 식재료입니다. ID: " + ingredientId
                );
            }

            if (orderQuantity == null || orderQuantity <= 0) {
                throw new IllegalArgumentException(
                        "주문 수량은 1개 이상이어야 합니다."
                );
            }

            if (orderQuantity > currentQuantity) {
                throw new IllegalArgumentException(
                        "현재 재고보다 많은 수량은 주문할 수 없습니다. 식재료: "
                                + ingredientId
                );
            }
        }

        // 2. 발주 마스터 등록
        int result = orderRepository.insertOrder(orderDTO);

        if (result != 1) {
            throw new IllegalStateException("발주 등록 실패");
        }

        Long orderId = orderDTO.getOrderId();

        log.info("발주 번호 생성: {}", orderId);

        // 3. 발주 상세 등록
        for (OrderDetailRequestDTO detail : detailList) {

            detail.setOrderId(orderId);

            int detailResult =
                    orderDetailsRepository.insertOrderDetail(detail);

            if (detailResult != 1) {
                throw new IllegalStateException("발주 상세 등록 실패");
            }
        }

        // 4. 발주 총금액 갱신
        int priceResult =
                orderDetailsRepository.updateOrderTotalPrice(orderId);

        if (priceResult != 1) {
            throw new IllegalStateException("총금액 갱신 실패");
        }

        log.info("발주 등록 완료: {}", orderId);

        return orderId;
    }

    // 발주 상세 조회
    public List<OrderDetailListVO> getOrderDetail(Long orderId) {
        return orderDetailsRepository.selectOrderDetailList(orderId);
    }

    public int approveOrder(Long orderDetailId) {
        int result = orderDetailsRepository.approveOrderDetail(orderDetailId);
        return result;
    }

    public int rejectOrder(Long orderDetailId, String rejectReason) {
        int result = orderDetailsRepository.rejectOrderDetail(orderDetailId, rejectReason);
        return result;
    }

    @Transactional
    public int updateOrderDate(OrderDetailDateUpdateDTO dto) {

        log.info("===== 발주 품목 제조일자 및 입고일자 수정 =====");
        log.info("발주 상세 번호: {}", dto.getOrderDetailId());
        log.info("제조일자: {}", dto.getManufactureAt());
        log.info("입고일자: {}", dto.getStoreAt());

        // DTO → VO 변환
        OrderDetailDateUpdateVO vo = new OrderDetailDateUpdateVO();

        vo.setOrderDetailId(dto.getOrderDetailId());
        vo.setManufactureAt(dto.getManufactureAt());
        vo.setStoreAt(dto.getStoreAt());

        // DB 수정
        int result = orderDetailsRepository.updateOrderDetailsDates(
                vo.getOrderDetailId(),
                vo.getManufactureAt(),
                vo.getStoreAt()
        );

        if (result != 1) {
            throw new IllegalStateException("발주 품목 날짜 수정에 실패했습니다.");
        }

        log.info("날짜 수정 완료: {}", result);

        return result;
    }

    public OrderStatusCountDTO countOrderStatus() {
        return orderDetailsRepository.countOrderStatus();
    }

    public List<OrderStatusVO> getOrderStatusList() {
        return orderRepository.getOrderStatusList();
    }

    public PageInfo<OrderRequestDTO> getOrderList(
            Long userId,
            Integer page,
            Integer size,
            String startDate,
            String endDate,
            String orderStatus,
            String storeName,
            Long orderId,
            String dateType
    ) {
        // 1. 페이지네이션 설정
        PageHelper.startPage(page, size);

        // 2. 기존 Repository를 통해 DB 조회
        List<OrderRequestDTO> orderList =
                orderRepository.getOrderList(
                        userId,
                        startDate,
                        endDate,
                        orderStatus,
                        storeName,
                        orderId,
                        dateType
                );

        // 3. 조회 결과에 페이지 정보 추가
        return new PageInfo<>(orderList);
    }

    public OrderInfoVO getOrderInfoByOrderId(Long orderId) {
        return orderDetailsRepository.selectOrderInfoByOrderId(orderId);
    }

    @Transactional
    public String updateOrderDetailStatus(
            Long orderId,
            List<OrderStatusDTO> items
    ) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("처리할 발주 품목이 없습니다.");
        }

        // 1. 발주 상세 품목 조회
        List<OrderDetailListVO> details =
                orderDetailsRepository.selectOrderDetailList(orderId);

        for (OrderStatusDTO item : items) {

            String status = item.getOrderStatus();

            if (!"승인".equals(status) && !"거절".equals(status)) {
                throw new IllegalArgumentException("잘못된 발주 처리 상태입니다.");
            }

            if ("거절".equals(status)
                    && (item.getRejectReason() == null
                    || item.getRejectReason().isBlank())) {
                throw new IllegalArgumentException("거절 사유를 입력해주세요.");
            }

            // 2. 처리할 발주 품목 찾기
            OrderDetailListVO detail = details.stream()
                    .filter(d -> d.getOrderDetailId()
                            .equals(item.getOrderDetailId()))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException("발주 품목을 찾을 수 없습니다.")
                    );

            // 3. 발주 상태 변경
            int result = orderDetailsRepository.updateOrderDetailStatus(
                    orderId,
                    item.getOrderDetailId(),
                    status,
                    item.getRejectReason()
            );

            if (result == 0) {
                throw new IllegalArgumentException(
                        "이미 처리되었거나 존재하지 않는 발주 품목입니다."
                );
            }

            // 4. 승인인 경우에만 재고 차감
            if ("승인".equals(status)) {

                int stockResult = orderDetailsRepository.updateIngredientStock(
                        detail.getIngredientId(),
                        detail.getOrderQuantity()
                );

                if (stockResult == 0) {
                    throw new IllegalArgumentException(
                            "재고가 부족하여 승인할 수 없습니다."
                    );
                }
            }

            log.info("[본사] 품목번호: {}, 처리 상태: {}, 처리 결과: {}",
                    item.getOrderDetailId(), status, result);
        }

        // 5. 발주 전체 상태 계산
        List<String> statuses =
                orderDetailsRepository.getOrderDetailStatuses(orderId);

        String orderStatus = calculateOrderStatus(statuses);

        log.info("[본사] 발주번호: {}, 최종 발주 상태: {}",
                orderId, orderStatus);

        return orderStatus;
    }

    public String calculateOrderStatus(List<String> statuses) {

        if (statuses == null || statuses.isEmpty()) {
            throw new IllegalArgumentException("발주 품목이 없습니다.");
        }

        // 대기 품목이 하나라도 있으면 전체 대기
        if (statuses.contains("대기")) {
            return "대기";
        }

        // 모든 품목이 승인
        if (statuses.stream().allMatch("승인"::equals)) {
            return "승인";
        }

        // 모든 품목이 거절
        if (statuses.stream().allMatch("거절"::equals)) {
            return "거절";
        }

        // 승인과 거절이 섞여 있는 경우
        return "부분";
    }

    public PageInfo<OrderRequestDTO> getStoreOrderList(OrderStoreSearchDTO search) {

        int page = search.getPage() == null || search.getPage() < 1
                ? 1 : search.getPage();

        int size = search.getSize() == null || search.getSize() < 1
                ? 8 : search.getSize();

        search.setPage(page);
        search.setSize(size);

        // 페이지네이션 시작
        PageHelper.startPage(page, size);

        // 발주 목록 조회
        List<OrderRequestDTO> list =
                orderRepository.getStoreOrderList(search);

        // PageInfo 생성
        PageInfo<OrderRequestDTO> pageInfo = new PageInfo<>(list);

        log.info("조회 직영점 ID: {}", search.getStoreId());
        log.info("현재 페이지: {}", pageInfo.getPageNum());
        log.info("페이지당 조회 건수: {}", pageInfo.getPageSize());
        log.info("전체 데이터: {}", pageInfo.getTotal());
        log.info("전체 페이지: {}", pageInfo.getPages());

        return pageInfo;
    }

    public List<OrderProgressVO> getOrderProgress(Long storeId) {

        log.info("===== 매장별 발주 진행 정보 조회 =====");
        log.info("조회 매장 ID: {}", storeId);

        List<OrderProgressVO> result =
                orderRepository.getOrderProgress(storeId);

        log.info("조회된 발주 건수: {}", result.size());

        return result;
    }

    public List<Map<String, Object>> getOrderDetailItems(Long orderId, Long storeId) {

        log.info("===== 발주 품목 상세 조회 Service =====");
        log.info("요청 발주번호: {}", orderId);
        log.info("요청 매장 ID: {}", storeId);

        List<Map<String, Object>> result =
                orderDetailsRepository.getOrderDetailItems(orderId, storeId);

        log.info("Service 조회 결과 품목 수: {}", result.size());

        return result;
    }

    public List<OrderIngredientVO> getOrderIngredientList() {
        return orderRepository.getOrderIngredientList();
    }

    public int countOrderDetails(Long orderId) {

        log.info("===== 발주 상세 품목 개수 조회 =====");
        log.info("조회 발주 번호: {}", orderId);

        int count = orderDetailsRepository.countOrderDetails(orderId);

        log.info("조회된 발주 품목 개수: {}", count);

        return count;
    }

    public Long selectOrderTotalPrice(Long orderId) {

        log.info("===== 발주 총 금액 조회 =====");
        log.info("조회 발주 번호: {}", orderId);

        Long totalPrice = orderRepository.selectOrderTotalPrice(orderId);

        log.info("조회된 발주 총 금액: {}", totalPrice);

        return totalPrice;
    }


}
