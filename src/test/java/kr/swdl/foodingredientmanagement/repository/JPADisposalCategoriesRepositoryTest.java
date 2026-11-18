package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalCategories;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
@Slf4j
public class JPADisposalCategoriesRepositoryTest {
    @Autowired
    private JPADisposalCategoriesRepository jpaDisposalCategoriesRepository;

    @Test
    public void 전체_항목_조회() {
        Assertions.assertTrue(jpaDisposalCategoriesRepository.findAll().size() > 0);
    }

    // JAP_CustomQuery
    @Test
    public void 폐기유형_대분류_가져오기_커스텀쿼리() {
        // TODO: 서비스 계층
        List<String> list = jpaDisposalCategoriesRepository.findDisposalType();
        for (String disposalType: list) {
            log.info("{}", disposalType);
        }
        Assertions.assertTrue(list.size() > 0);
    }

    @Test
    public void 특정_폐기유형의_폐기사유_가져오기() {
        Assertions.assertTrue(jpaDisposalCategoriesRepository.findAllByDisposalType("식재료 규칙").size() > 0);
    }

    // JAP_CustomQuery
    @Test
    public void 특정_폐기유형의_폐기사유_가져오기_커스텀쿼리() {
        List<String> list = jpaDisposalCategoriesRepository.findDisposalReasonByDisposalType("식재료 규칙");
        for (String disposalName : list) {
            log.info("{}", disposalName);
        }
        Assertions.assertTrue(list.size() > 0);
    }
}
