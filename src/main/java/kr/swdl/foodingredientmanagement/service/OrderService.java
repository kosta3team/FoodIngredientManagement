package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.dto.OrderDetailRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderStatusCountDTO;
import kr.swdl.foodingredientmanagement.repository.JPAOrdersRepository;
import kr.swdl.foodingredientmanagement.repository.MBOrderDetailsRepository;
import kr.swdl.foodingredientmanagement.repository.MBOrderRepository;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final JPAOrdersRepository jpaOrderRepository;

    private final MBOrderRepository mbOrderRepository;

    private final MBOrderDetailsRepository mbOrderDetailsRepository;

    public Long registerOrder(OrderRequestDTO orderDTO, List<OrderDetailRequestDTO> detailList) {

        int result = mbOrderRepository.insertOrder(orderDTO);

        if (result != 1) {
            throw new IllegalStateException("발주 등록 실패");
        }

        Long orderId = orderDTO.getOrderId();

        for (OrderDetailRequestDTO detail : detailList) {
            detail.setOrderId(orderId);
            int detailResult = mbOrderDetailsRepository.insertOrderDetail(detail);
            if(detailResult !=1){
                throw new IllegalStateException("발주 상세 등록 실패");
            }
        }

        int priceResult = mbOrderDetailsRepository.updateOrderTotalPrice(orderId);
        if (priceResult != 1) {
            throw new IllegalStateException("총금액 갱신 실패");
        }

        return orderId;
    }

    // 발주 상세 조회
    public List<OrderDetailVO> findOrderDetail(Long orderId) {
        return mbOrderRepository.selectOrderDetailList(orderId);
    }

    public int approveOrder(Long orderDetailId) {
        int result = mbOrderDetailsRepository.approveOrderDetail(orderDetailId);
        return result;
    }

    public int rejectOrder(Long orderDetailId, String rejectReason) {
        int result = mbOrderDetailsRepository.rejectOrderDetail(orderDetailId, rejectReason);
        return result;
    }

    public int updateOrderDate(Long orderDetailId, String manufactureAt, String storeAt) {
        int result = mbOrderDetailsRepository.updateOrderDetailsDates(orderDetailId, manufactureAt, storeAt);
        return result;
    }

    public OrderStatusCountDTO countOrderStatus() {
        return mbOrderDetailsRepository.countOrderStatus();
    }

    public List<OrderStatusVO> findOrderStatusList() {
        return mbOrderRepository.findOrderStatusList();
    }

    public List<OrderRequestDTO> findOrderList(
            String startDate,
            String endDate,
            String orderStatus,
            String storeName,
            Long orderId,
            String dateType
    ) {
        return mbOrderRepository.findOrderList(
                startDate,
                endDate,
                orderStatus,
                storeName,
                orderId,
                dateType
        );
    }
}
