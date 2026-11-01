package kr.swdl.foodingredientmanagement.entitiy;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    @Id
    @GeneratedValue(generator = "food_seq", strategy = GenerationType.SEQUENCE)
    private int foodNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodProcessingType processedFood;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String unit;   // 봉 / 판 / 통 / 망..

    @Column(nullable = false)
    private String capacity;   // 개 / 그람 / 밀리리터
}
