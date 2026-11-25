package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderRepository {
    int insertOrder(OrderRequestDTO orderRequestDTO);

    List<OrderStatusVO> getOrderStatusList();

    List<OrderRequestDTO> getOrderList(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("orderStatus") String orderStatus,
            @Param("storeName") String storeName,
            @Param("orderId") Long orderId,
            String dateType);

    List<OrderListVO> getOrderDetail(
            @Param("userId") Long userId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("dateType") String dateType
    );

    List<OrderInfoVO> selectOrderInfo();

    List<OrderDetailListVO> selectOrderDetailList(
            @Param("orderId") Long orderId
    );

    Long selectOrderTotalPrice(@Param("orderId") Long orderId);

    List<OrderProgressVO> getOrderProgress(@Param("storeId") Long storeId);

}
