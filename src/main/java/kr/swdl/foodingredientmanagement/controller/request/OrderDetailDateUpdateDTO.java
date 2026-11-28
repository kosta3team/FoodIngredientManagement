package kr.swdl.foodingredientmanagement.controller.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderDetailDateUpdateDTO {
    private Long orderDetailId;
    private Timestamp manufactureAt;
    private Timestamp storeAt;
}