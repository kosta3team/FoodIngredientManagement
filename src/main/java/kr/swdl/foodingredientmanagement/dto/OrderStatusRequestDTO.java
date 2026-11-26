package kr.swdl.foodingredientmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderStatusRequestDTO {

    private List<OrderStatusDTO> items;
}
