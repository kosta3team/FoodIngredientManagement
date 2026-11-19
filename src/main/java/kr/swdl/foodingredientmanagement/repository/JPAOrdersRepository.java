package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPAOrdersRepository extends JpaRepository<Orders, Long> {
    //발주 목록 조회
    List<Orders> findAllByStoreId(Long storeId);
}
