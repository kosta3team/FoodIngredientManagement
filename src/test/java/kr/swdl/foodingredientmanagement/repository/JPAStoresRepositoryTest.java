package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.Stores;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class JPAStoresRepositoryTest {
    @Autowired
    private JPAStoresRepository jpaStoresRepository;
    @Test
    void 담당매장_조회(){
        Stores store = jpaStoresRepository.findByUserId(4).orElseThrow(
                () -> new RuntimeException("매장 없음"));
        log.info("담당 매장 : {}", store);
    }
}
