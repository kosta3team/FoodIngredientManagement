package kr.swdl.foodingredientmanagement.member.repository;

import kr.swdl.foodingredientmanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JPAMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(String id);
    boolean existsById(String id);
}
