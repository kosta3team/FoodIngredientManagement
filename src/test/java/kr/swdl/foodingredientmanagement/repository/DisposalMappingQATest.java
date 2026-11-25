package kr.swdl.foodingredientmanagement.repository;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.MethodOrderer; // 실행 순서를 결정
import org.junit.jupiter.api.TestMethodOrder; // 테스트 메서드의 실행 순서 결정 방식 지정

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DisposalMappingQATest {

    // mybatis 설정 및 SQL 세션 생성 관리
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    // 1. DisposalAppInfosRepository
    // JPA

    // 2. DisposalCategoriesRepository
    // JPA

    // 3. DisposalsRepository
    @Test
    @Order(3) // 테스트 실행 순서 3번째
    void disposal_Repository_XML_mapping_success(){
        validateMapping(DisposalsRepository.class);
    }

    // 4. DisposalStatisticsRepository
    @Test
    @Order(4)
    void disposalStatistics_Repository_XML_mapping_success(){
        validateMapping(DisposalStatisticsRepository.class);
    }

    // 검증로직
    private void validateMapping(Class<?> repository){

        // Given
        List<String> missingMappings = new ArrayList<>();

        // When
        for (Method method : repository.getDeclaredMethods()){

            if (!Modifier.isAbstract(method.getModifiers())){
                continue;
            }

            String statementId =
                    repository.getName()
                    + "."
                    + method.getName();

            boolean exists = sqlSessionFactory
                    .getConfiguration()
                    .hasStatement(statementId);

            if (exists){
                System.out.println("[PASS] "+ statementId);
            } else {
                System.out.println("[FAIL] "+ statementId);
                missingMappings.add(statementId);
            }
        }

        // Then
        assertTrue(
                missingMappings.isEmpty(),
                "MyBatis mapping missed: " + missingMappings
        );
    }

}
