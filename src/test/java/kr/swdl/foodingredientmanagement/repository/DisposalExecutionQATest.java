package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalAppInfos;
import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalCategories;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.sql.init.mode=never"
})
public class DisposalExecutionQATest {

    // JPA
    @Autowired
    private DisposalAppInfosRepository disposalAppInfosRepository;

    // JPA
    @Autowired
    private DisposalCategoryRepository disposalCategoryRepository;

    // mybatis
    @Autowired
    private DisposalsRepository disposalsRepository;

    // mybatis
    @Autowired
    private DisposalStatisticsRepository disposalStatisticsRepository;


    // DisposalAppInfoTest
    // JPA
    @Test
    @Order(1)
    void diposalAppInfos_execution_success(){
        // given
        // 첫번째 페이지에서 최대 1개 데이터 조회
        PageRequest pageRequest = PageRequest.of(0,1);

        // when
        Page<DisposalAppInfos> result = assertDoesNotThrow(() ->
                disposalAppInfosRepository.findAll(pageRequest));
        // then
        assertNotNull(result);

        System.out.println("[PASS] DisposalAppInfosRepository.findAll");
        System.out.println("조회 건수: " + result.getNumberOfElements());
    }

    // DisposalCategoryRepository
    // JPA
    @Test
    @Order(2)
    void disposalCategory_execution_success(){
        // given
        // 첫번째 페이지에서 최대 1개 데이터 조회
        PageRequest pageRequest = PageRequest.of(0,1);

        // when
        Page<DisposalCategories> result = assertDoesNotThrow(() ->
                    disposalCategoryRepository.findAll(pageRequest));

        // then
        assertNotNull(result);

        System.out.println("[PASS] DisposalCategoryRepository.findAll");
        System.out.println("조회 건수: " + result.getNumberOfElements());

    }

    // DisposalsRepository
    // mybatis

    // DisposalStaticsRepository
    // mybatis

}
