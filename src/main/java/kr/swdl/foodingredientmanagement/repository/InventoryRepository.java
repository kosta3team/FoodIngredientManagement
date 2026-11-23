package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.InventoryListDTO;
import kr.swdl.foodingredientmanagement.dto.InventoryOrderDTO;
import kr.swdl.foodingredientmanagement.dto.InventoryUsageListDTO;
import kr.swdl.foodingredientmanagement.repository.vo.OrderDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InventoryRepository {
  List<InventoryListDTO> findInventoryList();

  List<InventoryListDTO> findInventoryList(@Param("ingredientId") Long ingredientId,
                                           @Param("storeId") Long storeId,
                                           @Param("storeStartDate") LocalDate storeStartDate,
                                           @Param("storeEndDate") LocalDate storeEndDate,
                                           @Param("manageStartDate") LocalDate manageStartDate,
                                           @Param("manageEndDate") LocalDate manageEndDate);

  InventoryListDTO findInventoryDetail(@Param("storeId") Long storeId,
                                       @Param("ingredientId") Long ingredientId);

  InventoryOrderDTO findInventoryOrder(@Param("storeId") Long storeId,
                                       @Param("ingredientId") Long ingredientId);

  List <InventoryUsageListDTO> findInventoryUsageList(@Param("storeId") Long storeId,
                                                      @Param("ingredientId") Long ingredientId);

  List<InventoryListDTO> findStoreInventoryList(@Param("storeId") Long storeId,
                                                @Param("subCategoryName") String subCategoryName,
                                                @Param("ingredientId") Long ingredientId);

  InventoryListDTO findInventoryByOrderDetailId(@Param("orderDetailId") Long orderDetailId);


  

    OrderDetailVO findById(@Param("orderDetailId")Long orderDetailId);





  InventoryOrderDTO findInventoryOrder(@Param("orderDetailId") Long orderDetailId);



}



