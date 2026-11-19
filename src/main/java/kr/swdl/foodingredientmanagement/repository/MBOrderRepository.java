package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MBOrderRepository {
    int insertOrder(OrderRequestDTO orderRequestDTO);

    List<OrderStatusVO> findOrderStatusList();

    List<OrderRequestDTO> findOrderList(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("orderStatus") String orderStatus,
            @Param("storeName") String storeName,
            @Param("orderId") Long orderId,
            String dateType);

    List<OrderListVO> findOrderDetail(
            @Param("userId") Long userId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("dateType") String dateType
    );

    List<OrderInfoVO> selectOrderInfo();

    List<OrderDetailVO> selectOrderDetailList(
            @Param("orderId") Long orderId
    );


    Long selectOrderTotalPrice(@Param("orderId") Long orderId);


    List<OrderProgressVO> findOrderProgress(@Param("storeId") Long storeId);

}
