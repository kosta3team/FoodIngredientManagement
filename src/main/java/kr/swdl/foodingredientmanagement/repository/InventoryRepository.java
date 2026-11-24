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
  List<InventoryListDTO> getInventoryList();

  List<InventoryListDTO> getInventoryList(@Param("ingredientId") Long ingredientId,
                                           @Param("storeId") Long storeId,
                                           @Param("storeStartDate") LocalDate storeStartDate,
                                           @Param("storeEndDate") LocalDate storeEndDate,
                                           @Param("manageStartDate") LocalDate manageStartDate,
                                           @Param("manageEndDate") LocalDate manageEndDate);

  InventoryListDTO getInventoryDetail(@Param("storeId") Long storeId,
                                       @Param("ingredientId") Long ingredientId);

  InventoryOrderDTO getInventoryOrder(@Param("storeId") Long storeId,
                                       @Param("ingredientId") Long ingredientId);

  List <InventoryUsageListDTO> getInventoryUsageList(@Param("storeId") Long storeId,
                                                      @Param("ingredientId") Long ingredientId);

  List<InventoryListDTO> getStoreInventoryList(@Param("storeId") Long storeId,
                                                @Param("subCategoryName") String subCategoryName,
                                                @Param("ingredientId") Long ingredientId);

  InventoryListDTO getInventoryByOrderDetailId(@Param("orderDetailId") Long orderDetailId);


  

    OrderDetailVO findById(@Param("orderDetailId")Long orderDetailId);





  InventoryOrderDTO getInventoryOrder(@Param("orderDetailId") Long orderDetailId);



}



