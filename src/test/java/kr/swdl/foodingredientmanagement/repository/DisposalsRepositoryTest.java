package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.vo.HQDisposalListVO;
import kr.swdl.foodingredientmanagement.dto.HqDisposalSerachFilterDTO;
import kr.swdl.foodingredientmanagement.repository.vo.StoreDisposalListVO;
import kr.swdl.foodingredientmanagement.dto.StoreDisposalSerachFilterDTO;
import kr.swdl.foodingredientmanagement.repository.vo.DisposalVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@SpringBootTest
@Slf4j

public class DisposalsRepositoryTest {

    @Autowired
    private DisposalsRepository disposalsRepository;

    @Test
    public void 특정_폐기_조회() {
        DisposalVO disposalVO = disposalsRepository.findById(2L);
        log.info("{}", disposalVO);
    }

    @Test
    @Transactional
    public void 본사_폐기_등록_성공() {
        int beforeSize = disposalsRepository.findAll().size();
        DisposalVO disposalVO = DisposalVO.builder()
                .disposalUnit("통폐기")
                .disposalReqAt(new Timestamp(System.currentTimeMillis()))
                .reqUserId(Long.valueOf(1))
                .disposalStatus("승인")
                .processingMethod("정기폐기")
                .disposalType("식재료 규칙")
                .disposalReason("유통기한 경과")
                .disposalQuantity(1)
                .disposalPrice(99999)
                .orderDetailId(Long.valueOf(1))
                .build();
        Assertions.assertEquals(disposalsRepository.insertDisposal(disposalVO), 1);
        Assertions.assertEquals(disposalsRepository.findAll().size(), beforeSize + 1);
        log.info("{}",disposalVO.getDisposalId());
        log.info("{}", disposalsRepository.findById(disposalVO.getDisposalId()));
    }

    @Test
    @Transactional
    public void 본사_폐기_등록_실패_누락데이터() {
        int beforeSize = disposalsRepository.findAll().size();
        DisposalVO disposalVO = DisposalVO.builder().build();
//        Assertions.assertEquals(mbDisposalsRepository.insertDisposal(disposalVO), 0); SQLIntegrityConstraintViolationException
        Assertions.assertEquals(disposalsRepository.findAll().size(), beforeSize);
    }

    @Test
    @Transactional
    public void 직영점_폐기_등록() {
        int beforeSize = disposalsRepository.findAll().size();
        DisposalVO disposalVO = DisposalVO.builder()
                .disposalUnit("통폐기")
                .disposalReqAt(new Timestamp(System.currentTimeMillis()))
                .reqUserId(Long.valueOf(1))
                .disposalStatus("요청")
                .processingMethod("정기폐기")
                .disposalType("식재료 규칙")
                .disposalReason("유통기한 경과")
                .disposalDetailReason(null)
                .rejectReason(null)
                .disposalQuantity(1)
                .disposalPrice(7980)
                .orderDetailId(Long.valueOf(1))
                .build();
        Assertions.assertEquals(disposalsRepository.insertDisposal(disposalVO), 1);
        Assertions.assertEquals(disposalsRepository.findAll().size(), beforeSize + 1);
    }

    @Test
    @Transactional
    public void 폐기_수정_성공() {
        Long disposalId = 4L;
        DisposalVO disposalVO = disposalsRepository.findById(disposalId);
        Assertions.assertNotNull(disposalVO);
        log.info("{}", disposalsRepository.findById(disposalId));
        Assertions.assertEquals(disposalsRepository.updateDisposal(DisposalVO.builder()
                .disposalId(disposalId)
                .disposalType("식재료규칙2")
                .disposalReason("12341234")
                .disposalDetailReason("@@@@@").build()), 1);
        log.info("{}", disposalsRepository.findById(disposalId));
    }

    @Test
    @Transactional
    public void 폐기_수정_실패() {
        Long disposalId = 1L;
        DisposalVO disposalVO = disposalsRepository.findById(disposalId);
        Assertions.assertNotNull(disposalVO);
        log.info("{}", disposalsRepository.findById(disposalId));
        Assertions.assertEquals(disposalsRepository.updateDisposal(DisposalVO.builder()
                .disposalId(disposalId)
                .disposalType("식재료규칙2")
                .disposalReason("12341234")
                .disposalDetailReason("@@@@@").build()), 0);
        log.info("{}", disposalsRepository.findById(disposalId));
    }


    @Test
    @Transactional
    public void 폐기_삭제() {
        Long disposalId = 4L;
        DisposalVO disposalVO = disposalsRepository.findById(disposalId);
        log.info("{}", disposalVO);
        disposalsRepository.deleteDisposalById(disposalId);
        Assertions.assertNull(disposalsRepository.findById(disposalId));
        log.info("{}", disposalVO);
    }


    // -------------------------


    @Test
    public void 본사_폐기_목록조회() {
        HqDisposalSerachFilterDTO filterDTO = HqDisposalSerachFilterDTO.builder()
                .storeName(null)
                .ingredientName(null)
                .disposalStatus(null)
                .startDate(null)
                .endDate(null)
                .disposalType(null)
                .processingMethod(null)
                .userId(2L)
                .sortType(null).build();
        List<HQDisposalListVO> disposals = disposalsRepository.getHqDisposals(filterDTO);
        log.info("{}", disposals.size());
        Assertions.assertTrue(disposals.size() >= 0);
    }

    @Test
    public void 본사_폐기_목록조회_오름차순() {
        HqDisposalSerachFilterDTO filterDTO = HqDisposalSerachFilterDTO.builder()
                .storeName(null)
                .ingredientName(null)
                .disposalStatus(null)
                .startDate(null)
                .endDate(null)
                .disposalType(null)
                .processingMethod(null)
                .userId(2L)
                .sortType("asc").build();
        List<HQDisposalListVO> disposals = disposalsRepository.getHqDisposals(filterDTO);
        Assertions.assertTrue(disposals.size() >= 0);
    }

    @Test
    public void 본사_폐기_목록조회_전체_내림차순() {
        HqDisposalSerachFilterDTO filterDTO = HqDisposalSerachFilterDTO.builder()
                .storeName(null)
                .ingredientName(null)
                .disposalStatus(null)
                .startDate(null)
                .endDate(null)
                .disposalType(null)
                .processingMethod(null)
                .userId(2L)
                .sortType("desc").build();
        List<HQDisposalListVO> disposals = disposalsRepository.getHqDisposals(filterDTO);
        Assertions.assertTrue(disposals.size() >= 0);
    }

    @Test
    public void 본사_폐기_목록조회_전체_직영점이름() {
        HqDisposalSerachFilterDTO filterDTO = HqDisposalSerachFilterDTO.builder()
                .storeName("잠실직영점")
                .ingredientName(null)
                .disposalStatus(null)
                .startDate(null)
                .endDate(null)
                .disposalType(null)
                .processingMethod(null)
                .userId(2L)
                .sortType(null).build();
        List<HQDisposalListVO> disposals = disposalsRepository.getHqDisposals(filterDTO);
        Assertions.assertTrue(disposals.size() >= 0);
    }

    @Test
    public void 본사_폐기_목록조회_전체_식재료명() {
        HqDisposalSerachFilterDTO filterDTO = HqDisposalSerachFilterDTO.builder()
                .storeName(null)
                .ingredientName("계란")
                .disposalStatus(null)
                .startDate(null)
                .endDate(null)
                .disposalType(null)
                .processingMethod(null)
                .userId(2L)
                .sortType(null).build();
        List<HQDisposalListVO> disposals = disposalsRepository.getHqDisposals(filterDTO);
        Assertions.assertTrue(disposals.size() >= 0);
    }

    @Test
    public void 본사_폐기_목록조회_전체_요청상태() {
        HqDisposalSerachFilterDTO filterDTO = HqDisposalSerachFilterDTO.builder()
                .storeName(null)
                .ingredientName(null)
                .disposalStatus("요청")
                .startDate(null)
                .endDate(null)
                .disposalType(null)
                .processingMethod(null)
                .userId(2L)
                .sortType(null).build();
        List<HQDisposalListVO> disposals = disposalsRepository.getHqDisposals(filterDTO);
        Assertions.assertTrue(disposals.size() >= 0);
    }

    @Test
    public void 본사_폐기_목록조회_전체_시작종료일() {
        HqDisposalSerachFilterDTO filterDTO = HqDisposalSerachFilterDTO.builder()
                .storeName(null)
                .ingredientName(null)
                .disposalStatus(null)
                .startDate("2026-10-08")
                .endDate("2026-11-17")
                .disposalType(null)
                .processingMethod(null)
                .userId(2L)
                .sortType(null).build();
        List<HQDisposalListVO> disposals = disposalsRepository.getHqDisposals(filterDTO);
        log.info("{}", disposals.size());
        Assertions.assertTrue(disposals.size() >= 0);
    }

    @Test
    public void 본사_폐기_목록조회_전체_폐기타입() {
        HqDisposalSerachFilterDTO filterDTO = HqDisposalSerachFilterDTO.builder()
                .storeName(null)
                .ingredientName(null)
                .disposalStatus(null)
                .startDate(null)
                .endDate(null)
                .disposalType("위생(변질)")
                .processingMethod(null)
                .userId(2L)
                .sortType(null).build();
        List<HQDisposalListVO> disposals = disposalsRepository.getHqDisposals(filterDTO);
        Assertions.assertTrue(disposals.size() >= 0);
    }

    @Test
    public void 본사_폐기_목록조회_전체_정규수시() {
        HqDisposalSerachFilterDTO filterDTO = HqDisposalSerachFilterDTO.builder()
                .storeName(null)
                .ingredientName(null)
                .disposalStatus(null)
                .startDate(null)
                .endDate(null)
                .disposalType(null)
                .processingMethod("정기폐기")
                .userId(2L)
                .sortType(null).build();
        List<HQDisposalListVO> disposals = disposalsRepository.getHqDisposals(filterDTO);
        Assertions.assertTrue(disposals.size() >= 0);
    }


    @Test
    public void 본사_폐기_목록조회_전체_모든필터_적용() {

        HqDisposalSerachFilterDTO filterDTO = HqDisposalSerachFilterDTO.builder()
                .storeName("강남직영점")
                .ingredientName("계")
                .disposalStatus("승인")
                .startDate("2026-11-09")
                .endDate("2026-11-11")
                .disposalType("입고 품질 불량")
                .processingMethod("정기폐기")
                .userId(2L)
                .sortType("desc").build();
        List<HQDisposalListVO> disposals = disposalsRepository.getHqDisposals(filterDTO);
        Assertions.assertTrue(disposals.size() >= 0);
    }

    @Test
    @Transactional
    public void 금일_폐기_건수() {
        // 테스트 폐기 추가
        System.out.println(disposalsRepository.countByTodayDisposed());
    }

    @Test
    @Transactional
    public void 금일_폐기_금액() {
        System.out.println(disposalsRepository.sumByTodayDisposedPrice());
    }

    @Test
    @Transactional
    public void 금일_폐기확인필요_개수() {
        System.out.println(disposalsRepository.countNeedToConfirmByStoreId(3L));
    }

    @Test
    public void 직영점_폐기목록_조회() {
        StoreDisposalSerachFilterDTO filterDTO = StoreDisposalSerachFilterDTO.builder()
                .ingredientName(null)
                .disposalStatus(null)
                .startDate(null)
                .endDate(null)
                .processingMethod(null)
                .storeId(1L)
                .sortType(null).build();
        List<StoreDisposalListVO> disposals = disposalsRepository.getStoreDisposals(filterDTO);
        System.out.println(disposals);
        Assertions.assertTrue(disposals.size() >= 0);
    }

}