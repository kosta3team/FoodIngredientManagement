package kr.swdl.foodingredientmanagement.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.dto.OrderDetailRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderStatusCountDTO;
import kr.swdl.foodingredientmanagement.dto.OrderStatusDTO;
import kr.swdl.foodingredientmanagement.repository.OrderDetailsRepository;
import kr.swdl.foodingredientmanagement.repository.OrderRepository;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderDetailsRepository orderDetailsRepository;

    public Long registerOrder(OrderRequestDTO orderDTO, List<OrderDetailRequestDTO> detailList) {

        int result = orderRepository.insertOrder(orderDTO);

        if (result != 1) {
            throw new IllegalStateException("발주 등록 실패");
        }

        Long orderId = orderDTO.getOrderId();

        for (OrderDetailRequestDTO detail : detailList) {
            detail.setOrderId(orderId);
            int detailResult = orderDetailsRepository.insertOrderDetail(detail);
            if(detailResult !=1){
                throw new IllegalStateException("발주 상세 등록 실패");
            }
        }

        int priceResult = orderDetailsRepository.updateOrderTotalPrice(orderId);
        if (priceResult != 1) {
            throw new IllegalStateException("총금액 갱신 실패");
        }

        return orderId;
    }

    // 발주 상세 조회
    public List<OrderDetailListVO> getOrderDetail(Long orderId) {
        return orderRepository.selectOrderDetailList(orderId);
    }

    public int approveOrder(Long orderDetailId) {
        int result = orderDetailsRepository.approveOrderDetail(orderDetailId);
        return result;
    }

    public int rejectOrder(Long orderDetailId, String rejectReason) {
        int result = orderDetailsRepository.rejectOrderDetail(orderDetailId, rejectReason);
        return result;
    }

    public int updateOrderDate(Long orderDetailId, String manufactureAt, String storeAt) {
        int result = orderDetailsRepository.updateOrderDetailsDates(orderDetailId, manufactureAt, storeAt);
        return result;
    }

    public OrderStatusCountDTO countOrderStatus() {
        return orderDetailsRepository.countOrderStatus();
    }

    public List<OrderStatusVO> getOrderStatusList() {
        return orderRepository.getOrderStatusList();
    }

    public PageInfo<OrderRequestDTO> getOrderList(
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

        for (OrderStatusDTO item : items) {

            log.info("[본사] 품목번호: {}, 상태: [{}]",
                    item.getOrderDetailId(),
                    item.getOrderStatus());

            if (!"승인".equals(item.getOrderStatus())
                    && !"거절".equals(item.getOrderStatus())) {
                throw new IllegalArgumentException("잘못된 발주 처리 상태입니다.");
            }

            if ("거절".equals(item.getOrderStatus())
                    && (item.getRejectReason() == null
                    || item.getRejectReason().isBlank())) {
                throw new IllegalArgumentException("거절 사유를 입력해주세요.");
            }

            int result = orderDetailsRepository.updateOrderDetailStatus(
                    orderId,
                    item.getOrderDetailId(),
                    item.getOrderStatus(),
                    item.getRejectReason()
            );

            log.info("[본사] 품목번호 {} UPDATE 결과: {}건",
                    item.getOrderDetailId(), result);

            if (result == 0) {
                throw new IllegalArgumentException(
                        "변경할 발주 품목이 없거나 이미 처리된 품목입니다."
                );
            }
        }

        // 품목별 처리 완료 후 해당 발주의 전체 품목 상태 조회
        List<String> statuses =
                orderDetailsRepository.getOrderDetailStatuses(orderId);

        // Service에서 발주 전체 상태 계산
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
}
