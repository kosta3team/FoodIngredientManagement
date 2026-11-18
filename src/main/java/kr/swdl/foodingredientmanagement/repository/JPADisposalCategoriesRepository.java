package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPADisposalCategoriesRepository extends JpaRepository<DisposalCategories, Long> {
    List<DisposalCategories> findAllByDisposalType(String disposalType);
    @Query("SELECT DISTINCT disposalType  FROM DisposalCategories")
    List<String> findDisposalType();
    @Query("SELECT disposalReason  FROM DisposalCategories WHERE disposalType = :disposalType")
    List<String> findDisposalReasonByDisposalType(@Param("disposalType") String disposalType);
}
