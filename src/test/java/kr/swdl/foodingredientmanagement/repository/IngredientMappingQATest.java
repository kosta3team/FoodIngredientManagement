package kr.swdl.foodingredientmanagement.repository;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IngredientMappingQATest {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    // 1. IngredientCategoriesRepository
    // JPA

    // 2. MBIngredientRepository
    @Test
    @Order(2)
    void mbIngredient_Repository_XML_mapping_success(){
        validateMapping(MBIngredientRepository.class);
    }

    // 검증로직
    private void validateMapping(Class<?> repository){

        // Given
        List<String> missingMappings = new ArrayList<>();

        int totalCount = 0;

        // When
        for (Method method : repository.getDeclaredMethods()){

            if (!Modifier.isAbstract(method.getModifiers())){
                continue;
            }

            totalCount++;

            String statementId =
                    repository.getName()
                    +"."
                    + method.getName();

            boolean exists = sqlSessionFactory
                    .getConfiguration()
                    .hasStatement(statementId);

            if (exists){
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
        System.out.println("FAIL: "+ missingMappings.size());

        assertTrue(
                missingMappings.isEmpty(),
                "Mybatis mapping missed: " + missingMappings
        );

    }
}
