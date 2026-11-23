package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InventoryServiceTest {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void 본사_전체_재고_목록_조회(){

    }

    @Test
    void 본사_목록_조회_실패(){

    }
}
