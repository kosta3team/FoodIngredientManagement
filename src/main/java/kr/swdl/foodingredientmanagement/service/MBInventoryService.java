package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.controller.responseDTO.InventoryListResponse;
import kr.swdl.foodingredientmanagement.repository.MBInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MBInventoryService {
    @Autowired
    private MBInventoryRepository mbInventoryRepository;

}
