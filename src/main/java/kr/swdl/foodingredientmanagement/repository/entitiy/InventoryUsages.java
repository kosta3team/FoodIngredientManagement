package kr.swdl.foodingredientmanagement.repository.entitiy;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InventoryUsages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryUsageId;
    private int usageQuantity;
    private Timestamp managementStartAt;
    private Timestamp consumptionAt;
    private Long orderDetailId;
}