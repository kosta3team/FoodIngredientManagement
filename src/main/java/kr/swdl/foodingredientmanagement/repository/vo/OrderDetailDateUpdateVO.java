package kr.swdl.foodingredientmanagement.repository.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderDetailDateUpdateVO {

    private Long orderDetailId;

    private Timestamp manufactureAt;

    private Timestamp storeAt;
}