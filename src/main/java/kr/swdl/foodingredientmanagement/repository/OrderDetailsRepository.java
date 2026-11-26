package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.OrderDetailRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderStatusCountDTO;
import kr.swdl.foodingredientmanagement.repository.vo.OrderDetailVO;
import kr.swdl.foodingredientmanagement.repository.vo.OrderInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailsRepository {
    int insertOrderDetail(OrderDetailRequestDTO detail);

    OrderStatusCountDTO countOrderStatus();

    List<OrderDetailVO> selectOrderDetailList(
            @Param("orderId") Long orderId
    );

    int updateOrderTotalPrice(@Param("orderId") Long orderId);

    int countOrderDetails(@Param("orderId") Long orderId);

    int approveOrderDetail(
            @Param("orderDetailId") Long orderDetailId);

    int rejectOrderDetail(
            @Param("orderDetailId") Long orderDetailId,
            @Param("rejectReason") String rejectReason
    );

    int updateOrderDetailStatus(
            @Param("orderId") Long orderId,
            @Param("orderDetailId") Long orderDetailId,
            @Param("orderStatus") String orderStatus,
            @Param("rejectReason") String rejectReason
    );

    int updateOrderDetailsDates(
            @Param("orderDetailId") Long orderDetailId,
            @Param("manufactureAt") String manufactureAt,
            @Param("storeAt") String storeAt
    );

    OrderInfoVO selectOrderInfoByOrderId(
            @Param("orderId") Long orderId
    );

    List<String> getOrderDetailStatuses(
            @Param("orderId") Long orderId
    );
}
