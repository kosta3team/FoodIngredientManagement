package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.InventoryListResponseDTO;
import kr.swdl.foodingredientmanagement.dto.InventoryOrderResponseDTO;
import kr.swdl.foodingredientmanagement.dto.InventoryUsageListResponseDTO;
import kr.swdl.foodingredientmanagement.repository.vo.OrderDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MBInventoryRepository {
  List<InventoryListResponseDTO> findInventoryList();

  List<InventoryListResponseDTO> findInventoryList(@Param("ingredientId") Long ingredientId,
                                               @Param("storeId") Long storeId,
                                               @Param("storeStartDate") LocalDate storeStartDate,
                                               @Param("storeEndDate") LocalDate storeEndDate,
                                               @Param("manageStartDate") LocalDate manageStartDate,
                                               @Param("manageEndDate") LocalDate manageEndDate);

  InventoryListResponseDTO findInventoryDetail(@Param("storeId") Long storeId,
                                               @Param("ingredientId") Long ingredientId);

  InventoryOrderResponseDTO findInventoryOrder(@Param("storeId") Long storeId,
                                               @Param("ingredientId") Long ingredientId);

  List <InventoryUsageListResponseDTO> findInventoryUsageList(@Param("storeId") Long storeId,
                                                              @Param("ingredientId") Long ingredientId);

  List<InventoryListResponseDTO> findStoreInventoryList(@Param("storeId") Long storeId,
                                                        @Param("subCategoryName") String subCategoryName,
                                                        @Param("ingredientId") Long ingredientId);

  InventoryListResponseDTO findInventoryByOrderDetailId(@Param("orderDetailId") Long orderDetailId);


  

    OrderDetailVO findById(@Param("orderDetailId")Long orderDetailId);





  InventoryOrderResponseDTO findInventoryOrder(@Param("orderDetailId") Long orderDetailId);



}



