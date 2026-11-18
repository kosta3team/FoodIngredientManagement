package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.controller.responseDTO.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class MBInventoryRepositoryTest {

    @Autowired
    MBInventoryRepository mbInventoryRepository;

    @Test
    void 본사_전체_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList();
        list.forEach(System.out::println);
    }

    @Test
    void 본사_식재료명_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(1L, null, null, null, null, null);
        list.forEach(System.out::println);
    }

    @Test
    void 본사_직영점명_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(null, 9L, null, null, null, null);
        list.forEach(System.out::println);
    }

    @Test
    void 본사_입고일시_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(null, null,  LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 16), null, null);
        list.forEach(System.out::println);
    }

    @Test
    void 본사_사용기한_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(null, null, null, null,  LocalDate.of(2026, 10, 3), LocalDate.of(2026, 11, 16));
        list.forEach(System.out::println);
    }

    @Test
    void 본사_식재료명_직영점명_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(14L, 5L, null, null, null, null);
        list.forEach(System.out::println);
    }

    @Test
    void 본사_식재료명_입고일시_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(14L, null, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 8), null, null);
        list.forEach(System.out::println);
    }

    @Test
    void 본사_식재료명_사용기한_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(14L, null, null, null, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 8));
        list.forEach(System.out::println);
    }

    @Test
    void 본사_직영점명_입고일시_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(null, 6L, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30),null, null);
        list.forEach(System.out::println);
    }

    @Test
    void 본사_직영점명_사용기한_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(null, 5L, null, null,LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
        list.forEach(System.out::println);
    }

    @Test
    void 본사_식재료명_직영점명_입고일시_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(14L, 5L, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30) ,null, null);
        list.forEach(System.out::println);
    }

    @Test
    void 본사_식재료명_직영점명_사용기한_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findInventoryList(14L, 5L, null, null ,LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
        list.forEach(System.out::println);
    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회(){
        InventoryListResponse inventoryListResponse = mbInventoryRepository.findInventoryDetail(1L, 1L);
        System.out.println(inventoryListResponse);
    }



    @Test
    void 본사_특정_직영점_특정_재고_상세_조회_입고정보(){
        InventoryOrderResponse inventoryOrderResponse = mbInventoryRepository.findInventoryOrder(6L, 13L);
        System.out.println(inventoryOrderResponse);
    }

    @Test
    void 본사_특정_직영점_특정_재고_상세_조회_사용이력(){
       List <InventoryUsageListResponse> list = mbInventoryRepository.findInventoryUsageList(1L, 12L);
        list.forEach(System.out::println);
    }

    @Test
    void 직영점_전체_재고_목록_조회(){

        List<InventoryListResponse> list = mbInventoryRepository.findStoreInventoryList(1L, null, null);
        list.forEach(System.out::println);
    }

    @Test
    void 직영점_분류_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findStoreInventoryList(1L, "소스류", null);
        list.forEach(System.out::println);
    }

    @Test
    void 직영점_식재료명_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findStoreInventoryList(1L, null, 12L);
        list.forEach(System.out::println);
    }

    @Test
    void 직영점_분류_식재료명_재고_목록_조회(){
        List<InventoryListResponse> list = mbInventoryRepository.findStoreInventoryList(5L, "육가공류", 14L);
        list.forEach(System.out::println);

    }

    @Test
    void 직영점_재고_상세_조회_잔여수량_식재료(){
        InventoryListResponse inventoryListResponse = mbInventoryRepository.findInventoryDetail(5L, 14L);
        System.out.println(inventoryListResponse);

    }

    @Test
    void 직영점_재고_상세_조회_사용이력(){
        List <InventoryUsageListResponse> list  = mbInventoryRepository.findInventoryUsageList(5L, 14L);
        list.forEach(System.out::println);

    }





}
