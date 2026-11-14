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
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;
    private int orderQuantity;
    private int supplyPrice;
    private Timestamp manufactureAt;
    private Timestamp storeAt;
    private Timestamp appAt;
    private String orderStatus;
    private int orderPrice;
    private String rejectReason;
    private int currentQuantity;
    private Long orderId;
    private Long ingredientId;



}