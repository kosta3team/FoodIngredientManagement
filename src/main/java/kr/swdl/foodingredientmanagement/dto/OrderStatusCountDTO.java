package kr.swdl.foodingredientmanagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class OrderStatusCountDTO {
    private int waitingCount;
    private int approvedCount;
    private int rejectedCount;

}
