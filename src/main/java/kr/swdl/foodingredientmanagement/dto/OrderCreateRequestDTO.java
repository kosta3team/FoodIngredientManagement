package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class OrderCreateRequestDTO {
    private Long storeId;
    private Long userId;
    private Timestamp orderAt;
    private List<OrderDetailRequestDTO> detailList;
}
