package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void 담당직영점_직원_조회(){
        int storeId=4;
        Users userId = userRepository.findByStoreId(storeId);
        assertNotNull(userId);
        log.info(userId.getUserName());
        // 조회된 모든 직원이 4번 매장 소속인지 검증
    }

}
