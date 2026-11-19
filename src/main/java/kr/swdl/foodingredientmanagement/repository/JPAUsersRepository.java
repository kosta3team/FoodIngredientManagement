package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JPAUsersRepository extends JpaRepository<Users,Long> {
    Users findByStoreId(Integer storeId);
}
