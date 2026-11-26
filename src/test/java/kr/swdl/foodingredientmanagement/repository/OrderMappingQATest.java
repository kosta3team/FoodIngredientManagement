package kr.swdl.foodingredientmanagement.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.swdl.foodingredientmanagement.repository.entitiy.OrderDetails;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderMappingQATest {

    // mybatis 검증
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    // JPA 검증
    @PersistenceContext
    private EntityManager entityManager;

    // 1. OrderDetailsRepository
    // JPA
    @Test
    @Order(1)
    @Transactional(readOnly = true)
    void orderDetails_Entity_DB_mapping_success(){
        validateJpaMapping(OrderDetails.class);
    }


    // 2. OrderRepository
    @Test
    @Order(2)
    void orderRepository_XML_mapping_success() {
        validateMapping(OrderRepository.class);
    }

    // 3. OrderDetailsRespository
    @Test
    @Order(3)
    void OrderDetailsRepository_XML_mapping_success(){
        validateMapping(OrderDetailsRepository.class);
    }

    // 검증로직
    private void validateMapping(Class<?> repository) {

        // Given
        List<String> missingMappings = new ArrayList<>();

        int totalCount = 0;

        // When
        for (Method method : repository.getDeclaredMethods()) {

            if (!Modifier.isAbstract(method.getModifiers())) {
                continue;
            }

            totalCount++;

            String statementId =
                    repository.getName()
                            + "."
                            + method.getName();

            boolean exists = sqlSessionFactory
                    .getConfiguration()
                    .hasStatement(statementId);

            if (exists) {
                System.out.println("[PASS] " + statementId);
            } else {
                System.out.println("[FAIL] " + statementId);
                missingMappings.add(statementId);
            }

        }

        // Then
        System.out.println("Repository: " + repository.getSimpleName());
        System.out.println("Total: " + totalCount);
        System.out.println("PASS: " + (totalCount - missingMappings.size()));
        System.out.println("FAIL: " + missingMappings.size());

        assertTrue(
                missingMappings.isEmpty(),
                "Mybatis mapping missed: " + missingMappings
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
