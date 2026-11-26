package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.vo.InventoryListVO;
import kr.swdl.foodingredientmanagement.repository.vo.InventoryOrderVO;
import kr.swdl.foodingredientmanagement.repository.vo.InventoryUsageListVO;
import kr.swdl.foodingredientmanagement.repository.vo.OrderDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InventoryRepository {
  List<InventoryListVO> getInventoryList();

  List<InventoryListVO> getInventoryList(@Param("ingredientName") String ingredientName,
                                         @Param("storeName") String storeName,
                                         @Param("storeStartDate") LocalDate storeStartDate,
                                         @Param("storeEndDate") LocalDate storeEndDate,
                                         @Param("manageStartDate") LocalDate manageStartDate,
                                         @Param("manageEndDate") LocalDate manageEndDate);

  InventoryListVO getInventoryDetail(@Param("storeId") Long storeId,
                                     @Param("ingredientId") Long ingredientId);

  InventoryOrderVO getInventoryOrder(@Param("storeId") Long storeId,
                                     @Param("ingredientId") Long ingredientId);

  List <InventoryUsageListVO> getInventoryUsageList(@Param("storeId") Long storeId,
                                                    @Param("ingredientId") Long ingredientId);

  List<InventoryListVO> getStoreInventoryList(@Param("storeId") Long storeId,
                                              @Param("subCategoryName") String subCategoryName,
                                              @Param("ingredientName") String ingredientName);

  InventoryListVO getInventoryByOrderDetailId(@Param("orderDetailId") Long orderDetailId);


  

    OrderDetailVO findById(@Param("orderDetailId")Long orderDetailId);


  int updateCurrentQuantityById(@Param("orderDetailId") Long orderDetailId,
                                @Param("newQuantity") int newQuantity);


  InventoryOrderVO getInventoryOrder(@Param("orderDetailId") Long orderDetailId);

  List<String> getSubCategoryList();



}



