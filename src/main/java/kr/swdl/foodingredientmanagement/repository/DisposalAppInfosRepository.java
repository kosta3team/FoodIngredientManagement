package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalAppInfos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisposalAppInfosRepository extends JpaRepository<DisposalAppInfos, Long> {
    Optional<DisposalAppInfos> findByDisposalId(Long disposalId);
}
