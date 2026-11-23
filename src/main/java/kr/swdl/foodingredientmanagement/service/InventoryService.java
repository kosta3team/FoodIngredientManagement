package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.dto.InventoryListDTO;
import kr.swdl.foodingredientmanagement.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public List<InventoryListDTO> findInventoryList(){
        return inventoryRepository.findInventoryList(null, null, null, null, null, null);
    }

    public List<InventoryListDTO> findInventoryList(Long ingredientId,
                                                    Long storeId,
                                                    LocalDate storeStartDate,
                                                    LocalDate storeEndDate,
                                                    LocalDate manageStartDate,
                                                    LocalDate manageEndDate){
        return inventoryRepository.findInventoryList( ingredientId, storeId, storeStartDate, storeEndDate,  manageStartDate, manageEndDate );
    }



}
