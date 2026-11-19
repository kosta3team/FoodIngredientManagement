package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.Stores;
import kr.swdl.foodingredientmanagement.repository.entitiy.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JPAStoresRepository extends JpaRepository<Stores, Long> {

    // 로그인한 사용자의 담당 매장 조회
    Optional<Stores> findByUserId(Integer userId);

}
