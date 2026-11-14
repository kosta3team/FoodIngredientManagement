package kr.swdl.foodingredientmanagement.repository.entitiy;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class DisposalCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long disposalCategoryId;
    private String disposalType;
    private String disposalReason;
}