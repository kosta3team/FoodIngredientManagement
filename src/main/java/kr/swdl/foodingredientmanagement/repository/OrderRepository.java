package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.OrderRequestDTO;
import kr.swdl.foodingredientmanagement.dto.OrderStoreSearchDTO;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderRepository {
    int insertOrder(OrderRequestDTO orderRequestDTO);

    List<OrderStatusVO> getOrderStatusList();

    List<OrderRequestDTO> getOrderList(
            @Param("userId") Long userId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("orderStatus") String orderStatus,
            @Param("storeName") String storeName,
            @Param("orderId") Long orderId,
            @Param("dateType") String dateType
    );

    List<OrderListVO> getOrderDetail(
            @Param("userId") Long userId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("dateType") String dateType
    );

    List<OrderInfoVO> selectOrderInfo();


    Long selectOrderTotalPrice(@Param("orderId") Long orderId);

    List<OrderProgressVO> getOrderProgress(@Param("storeId") Long storeId);

    //직영점 조회
    List<OrderRequestDTO> getStoreOrderList(OrderStoreSearchDTO search);

    // 발주 식재료 목록 조회
    List<OrderIngredientVO> getOrderIngredientList();

    Integer selectCurrentQuantity(
            @Param("ingredientId") Long ingredientId
    );
}
