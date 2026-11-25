package kr.swdl.foodingredientmanagement.repository;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MyBatisMappingQATest {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    void disposal_Repository_XML_mapping_success(){

        // given
        Class<?> repository = DisposalsRepository.class;

        // when
        for (Method method : repository.getDeclaredMethods()){
            if (method.isDefault()){
                continue;
            }

            String statementId =
                    repository.getName()
                    + "."
                    + method.getName();

            boolean exists = sqlSessionFactory
                    .getConfiguration()
                    .hasStatement(statementId);

            assertTrue(
                    exists,
                    "MyBatis mapping missed: " + statementId
            );
        }
    }
}
