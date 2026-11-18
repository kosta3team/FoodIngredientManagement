package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.controller.responseDTO.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MBInventoryRepository {
  List<InventoryListResponse> findInventoryList();

  List<InventoryListResponse> findInventoryList(@Param("ingredientId") Long ingredientId,
                                               @Param("storeId") Long storeId,
                                               @Param("storeStartDate") LocalDate storeStartDate,
                                               @Param("storeEndDate") LocalDate storeEndDate,
                                               @Param("manageStartDate") LocalDate manageStartDate,
                                               @Param("manageEndDate") LocalDate manageEndDate);




    InventoryListResponse findInventoryDetail(@Param("storeId") Long storeId,
                                              @Param("ingredientId") Long ingredientId);




    InventoryOrderResponse findInventoryOrder(@Param("storeId") Long storeId,
                                                  @Param("ingredientId") Long ingredientId);



    List <InventoryUsageListResponse> findInventoryUsageList(@Param("storeId") Long storeId,
                                                             @Param("ingredientId") Long ingredientId);



    List<InventoryListResponse> findStoreInventoryList(@Param("storeId") Long storeId,
                                                            @Param("subCategoryName") String subCategoryName,
                                                            @Param("ingredientId") Long ingredientId);
}



