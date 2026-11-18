package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.dto.OrderDetailRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MBOrderRepository {
    OrderStatusCountDTO countOrderStatus();

    List<OrderDTO> findOrderList();

    List<OrderStatusDTO> findOrderStatusList();

    List<OrderDTO> findOrderList(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("orderStatus") String orderStatus,
            @Param("storeName") String storeName,
            @Param("orderId") Long orderId
    );

    List<OrderInfoDTO> selectOrderInfo();

    List<OrderDetailDTO> selectOrderDetailList(
            @Param("orderId") Long orderId
    );

    List<IngredientListDTO> findIngredients();

    int insertOrderDetail(OrderDetailRequestDTO detail);

    int updateOrderTotalPrice(@Param("orderId") Long orderId);

    int countOrderDetails(@Param("orderId") Long orderId);

    Long selectOrderTotalPrice(@Param("orderId") Long orderId);

    List<OrderListDTO> findOrderDetail(
            @Param("storeId") Long storeId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("dateType") String dateType
    );

    List<OrderProgressDTO> findOrderProgress(@Param("storeId") Long storeId);

    int approveOrderDetail(
            @Param("orderDetailId") Long orderDetailId);

    int rejectOrderDetail(
            @Param("orderDetailId") Long orderDetailId,
            @Param("rejectReason") String rejectReaseon
    );

    int updateOrderDetailsDates(
            @Param("orderDetailId") Long orderDetailId,
            @Param("manufactureAt") String manufactureAt,
            @Param("storeAt") String storeAt
    );
}
