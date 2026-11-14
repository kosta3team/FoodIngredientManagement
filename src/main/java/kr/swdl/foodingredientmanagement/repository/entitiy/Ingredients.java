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
public class Ingredients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;
    private String ingredientName;
    private String orderUnit;
    private int capacity;
    private String capacityUnit;
    private String storageType;
    private String managementMethod;
    private int expirationPeriod;
    private int consumptionPeriod;
    private int summerConsumptionPeriod;
    private int supplyQuantity;
    private int supplyPrice;
    private String majorCategoryName;
    private String subCategoryName;
    private String usableFlag;
    private Timestamp registAt;
}