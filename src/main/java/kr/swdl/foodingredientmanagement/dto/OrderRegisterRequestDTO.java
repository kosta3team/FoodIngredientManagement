package kr.swdl.foodingredientmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OrderRegisterRequestDTO {
    private Long storeId;
    private List<OrderDetailRequestDTO> items;
}