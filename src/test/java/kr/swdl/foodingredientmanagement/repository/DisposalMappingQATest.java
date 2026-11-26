package kr.swdl.foodingredientmanagement.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalAppInfos;
import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalCategories;
import kr.swdl.foodingredientmanagement.repository.entitiy.Disposals;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.MethodOrderer; // 실행 순서를 결정
import org.junit.jupiter.api.TestMethodOrder; // 테스트 메서드의 실행 순서 결정 방식 지정
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.sql.init.mode=never"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DisposalMappingQATest {

    // mybatis 설정 및 SQL 세션 생성 관리
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    // JPA 검증
    @PersistenceContext
    private EntityManager entityManager;

    // 1. DisposalAppInfosRepository
    // JPA
    @Test
    @Order(1)
    @Transactional(readOnly = true)
    void disposalAppInfo_Entity_DB_mapping_success(){
        validateJpaMapping(DisposalAppInfos.class);
    }

    // 2. DisposalCategoriesRepository
    // JPA
    @Test
    @Order(2)
    @Transactional(readOnly = true)
    void disposalCategories_Entity_DB_mapping_success(){
        validateJpaMapping(DisposalCategories.class);
    }

    // 3. DisposalsRepository
    // mybatis
    @Test
    @Order(3) // 테스트 실행 순서 3번째
    void disposal_Repository_XML_mapping_success(){
        validateMapping(DisposalsRepository.class);
    }

    // 4. DisposalStatisticsRepository
    // mybatis
    @Test
    @Order(4)
    void disposalStatistics_Repository_XML_mapping_success(){
        validateMapping(DisposalStatisticsRepository.class);
    }

    // 5. Disposals Entity
    // JPA
    @Test
    @Order(5)
    @Transactional(readOnly = true)
    void disposals_Entity_DB_mapping_success(){
        validateJpaMapping(Disposals.class);
    }

    // mybatis 검증로직
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

    // JPA 검증로직
    private void validateJpaMapping(Class<?> entity){

        // Given
        String entityName = entityManager
                .getMetamodel()
                .entity(entity)
                .getName();

        String jpSql =
                "SELECT e FROM "
                + entityName
                + " e WHERE 1 = 0";

        // When
        assertDoesNotThrow(() -> {
            entityManager
                    .createQuery(jpSql, entity)
                    .getResultList();
        },
        // Then
        "JPA mapping failed: " + entityName);

        System.out.println("[PASS] " + entityName);
    }

}
