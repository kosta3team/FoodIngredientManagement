package kr.swdl.foodingredientmanagement.service;

import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.*;
import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalAppInfos;
import kr.swdl.foodingredientmanagement.repository.entitiy.Users;
import kr.swdl.foodingredientmanagement.repository.vo.DisposalVO;
import kr.swdl.foodingredientmanagement.repository.vo.OrderDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.autoconfigure.DispatcherServletPath;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class DisposalServiceTest {

    @Autowired
    private DisposalService disposalService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private MBIngredientRepository mBIngredientRepository;
    @Autowired
    private DisposalsRepository disposalsRepository;
    @Autowired
    private DisposalAppInfosRepository disposalAppInfosRepository;
    @Autowired
    private DispatcherServletPath dispatcherServletPath;


    @Test
    @Transactional
    public void 본사_폐기_등록_로직_검증() {
//        Long reqUserId, Long orderDetailId, DisposalsDTO disposalsDTO
        Long reqUserId = 2L;
        Long orderDetailId = 10L;

        DisposalDTO dto = DisposalDTO.builder()
                .reqUserId(reqUserId)
                .disposalUnit("통폐기")
                .disposalReqAt(new Timestamp(System.currentTimeMillis()))
                .reqUserId(2L)
                .disposalStatus("승인")
                .processingMethod("정기폐기")
                .disposalType("식재료 규칙")
                .disposalReason("유통기한 경과")
                .disposalQuantity(1)
                .orderDetailId(orderDetailId)
                .build();

        Users reqUser = userRepository.findById(dto.getReqUserId()).orElseThrow();
        String status = "승인";
        if (!reqUser.getRole().equals("HQ")) {
            status = "대기";
        }
        // 특정 재고 아이디
        OrderDetailVO orderDetailVO = inventoryRepository.findById(orderDetailId);
        if (null == orderDetailVO) Assertions.fail();
        log.info("Disposal inventory dump : {}", orderDetailVO);

        IngredientDetailDTO ingredientDTO = mBIngredientRepository.findIngredientDetail(orderDetailVO.getIngredientId());
        if (null == ingredientDTO) Assertions.fail();

        DisposalVO disposalVO = DisposalVO.builder()
                .disposalUnit(dto.getDisposalUnit())
                .disposalReqAt(new Timestamp(System.currentTimeMillis()))
                .reqUserId(dto.getReqUserId())
                .disposalStatus(status)
                .processingMethod(dto.getProcessingMethod())
                .disposalType(dto.getDisposalType())
                .disposalReason(dto.getDisposalReason())
                .disposalDetailReason(dto.getDisposalDetailReason())
                .disposalQuantity(orderDetailVO.getCurrentQuantity())
                .disposalPrice(ingredientDTO.getSupplyPrice() * dto.getDisposalQuantity())
                .orderDetailId(orderDetailId)
                .build();
        disposalsRepository.addDisposal(disposalVO);

        inventoryRepository.updateCurrentQuantityById(orderDetailId, 0);
        orderDetailVO = inventoryRepository.findById(orderDetailId);
        log.info("Disposal inventory dump : {}", orderDetailVO);

        log.info("Created disposal dump : {}", disposalsRepository.getDisposalById(disposalVO.getDisposalId()));

        if (status.equals("승인") && null != disposalVO.getDisposalId()) {
            DisposalAppInfos createdDAI = disposalAppInfosRepository.save(DisposalAppInfos.builder()
                    .appUserId(disposalVO.getReqUserId())
                    .disposalId(disposalVO.getDisposalId()).build());
            Assertions.assertNotNull(createdDAI);
            log.info("Created disposalAppInfo dump : {}", createdDAI);
        }
    }

//    @Test
//    @Transactional
//    public void 본사_폐기_등록_실패() {
//        Long orderDetailId = 999L;
//        disposalService.addDisposal(orderDetailId, DisposalDTO.builder().reqUserId(9999L).build());
//    }
//
//    @Test
//    @Transactional
//    public void 직영점_폐기_등록_성공() {
//
//        disposalService.updateDisposal()
//
//    }
//
//    @Test
//    @Transactional
//    public void 직영점_폐기_등록_실패() {
//        disposalService.addDisposal();
//    }

    @Test
    @Transactional
    public void 폐기_수정_로직() {
//        Long disposalId, DisposalDTO disposalDTO

        Long disposalId = 4L; // 요청


        DisposalVO disposalVO = disposalsRepository.getDisposalById(disposalId);
        Assertions.assertNotNull(disposalVO);
        log.info("before dump : {}", disposalVO);

        DisposalDTO disposalDTO = DisposalDTO.builder()
                .disposalType("정기페기")
                .disposalReason("식재료 규칙")
                .disposalDetailReason("폐기하도록!!!").build();

        Assertions.assertEquals(disposalsRepository.updateDisposal(DisposalVO.builder()
                .disposalId(disposalId)
                .disposalType(disposalDTO.getDisposalType())
                .disposalReason(disposalDTO.getDisposalReason())
                .disposalDetailReason(disposalDTO.getDisposalDetailReason()).build()), 1);

        disposalVO = disposalsRepository.getDisposalById(disposalId);
        log.info("after dump : {}", disposalVO);
    }

    @Test
    @Transactional
    public void 폐기_수정_실패_없는폐기() {
//        Long disposalId, DisposalDTO disposalDTO
        Long disposalId = 999L; // 요청
        DisposalDTO disposalDTO = DisposalDTO.builder()
                .disposalType("정기페기")
                .disposalReason("식재료 규칙")
                .disposalDetailReason("폐기하도록!!!").build();
       Assertions.assertFalse(disposalService.updateDisposal(disposalId, disposalDTO));
    }

    @Test
    @Transactional
    public void 폐기_수정_실패_승인된폐기() {
//        Long disposalId, DisposalDTO disposalDTO
        Long disposalId = 3L; // 승인
        DisposalDTO disposalDTO = DisposalDTO.builder()
                .disposalType("정기페기")
                .disposalReason("식재료 규칙")
                .disposalDetailReason("폐기하도록!!!").build();
        Assertions.assertFalse(disposalService.updateDisposal(disposalId, disposalDTO));
    }

    @Test
    @Transactional
    public void 폐기_승인거절_로직(){

        Long appUserId = 2L;
        Long disposalId = 4L;

        // 승인 전
        DisposalVO before = disposalsRepository.getDisposalById(disposalId);

        Assertions.assertNotNull(before);
        Assertions.assertEquals("요청", before.getDisposalStatus());

        log.info("before : {}", before);

        // 승인 요청
        DisposalDTO disposalDTO = DisposalDTO.builder()
                .disposalStatus("승인")
                .rejectReason(null)
                .build();

        // 서비스 실행
        disposalService.approveDisposal(appUserId, disposalId, disposalDTO);

        // 승인 후
        DisposalVO after = disposalsRepository.getDisposalById(disposalId);

        Assertions.assertNotNull(after);
        Assertions.assertEquals("승인", after.getDisposalStatus());
        Assertions.assertNotNull(after.getDisposalAppAt());

        log.info("after : {}", after);

        // 승인 정보 생성 확인
        Optional<DisposalAppInfos> optionalDisposalAppInfos = disposalAppInfosRepository.getDisposalAppInfoByDisposalId(disposalId);
        DisposalAppInfos created = optionalDisposalAppInfos.get();
        Assertions.assertNotNull(created);
        Assertions.assertEquals(appUserId, created.getAppUserId());
        Assertions.assertEquals(disposalId, created.getDisposalId());

        log.info("created dump : {}", created);
    }

    @Test
    @Transactional
    public void 폐기_거절_로직() {

        Long appUserId = 2L;
        Long disposalId = 4L;

        // 거절 전
        DisposalVO before = disposalsRepository.getDisposalById(disposalId);

        Assertions.assertNotNull(before);
        Assertions.assertEquals("요청", before.getDisposalStatus());

        log.info("before : {}", before);

        // 거절 요청
        DisposalDTO disposalDTO = DisposalDTO.builder()
                .disposalStatus("거절")
                .rejectReason("폐기 사유 부적절")
                .build();

        // 서비스 실행
        disposalService.approveDisposal(appUserId, disposalId, disposalDTO);

        // 거절 후
        DisposalVO after = disposalsRepository.getDisposalById(disposalId);

        Assertions.assertNotNull(after);
        Assertions.assertEquals("거절", after.getDisposalStatus());
        Assertions.assertEquals("폐기 사유 부적절", after.getRejectReason());
        Assertions.assertNotNull(after.getDisposalAppAt());

        log.info("after : {}", after);

        // 거절 시 승인 정보가 생성되지 않아야 함
        Optional<DisposalAppInfos> optionalDisposalAppInfos =
                disposalAppInfosRepository.getDisposalAppInfoByDisposalId(disposalId);

        Assertions.assertTrue(optionalDisposalAppInfos.isEmpty());

        log.info("created dump : {}", optionalDisposalAppInfos);
    }

    @Test
    @Transactional
    public void 폐기_중복승인_실패() {

        Long appUserId = 2L;
        Long disposalId = 4L;

        // 승인 전
        DisposalVO before = disposalsRepository.getDisposalById(disposalId);

        Assertions.assertNotNull(before);
        Assertions.assertEquals("요청", before.getDisposalStatus());

        log.info("before : {}", before);

        // 승인 요청
        DisposalDTO disposalDTO = DisposalDTO.builder()
                .disposalStatus("승인")
                .rejectReason(null)
                .build();

        // 최초 승인
        disposalService.approveDisposal(appUserId, disposalId, disposalDTO);

        // 최초 승인 후
        DisposalVO approved = disposalsRepository.getDisposalById(disposalId);

        Assertions.assertNotNull(approved);
        Assertions.assertEquals("승인", approved.getDisposalStatus());
        Assertions.assertNotNull(approved.getDisposalAppAt());

        log.info("approved : {}", approved);

        // 최초 승인 정보 생성 확인
        Optional<DisposalAppInfos> optionalDisposalAppInfos =
                disposalAppInfosRepository.getDisposalAppInfoByDisposalId(disposalId);

        Assertions.assertTrue(optionalDisposalAppInfos.isPresent());

        DisposalAppInfos created = optionalDisposalAppInfos.get();

        Assertions.assertEquals(appUserId, created.getAppUserId());
        Assertions.assertEquals(disposalId, created.getDisposalId());

        log.info("created dump : {}", created);

        // 중복 승인 시도 및 예외 검증
        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> disposalService.approveDisposal(
                        appUserId, disposalId, disposalDTO)
        );

        // 발생한 예외 메시지 검증
        Assertions.assertEquals(
                "이미 처리되었거나 존재하지 않는 폐기 요청입니다.",
                exception.getMessage()
        );

        // 예외 출력
        log.info("중복 승인 예외 : {}", exception.getMessage());

        // 중복 승인 시도 후
        DisposalVO after = disposalsRepository.getDisposalById(disposalId);

        Assertions.assertNotNull(after);
        Assertions.assertEquals("승인", after.getDisposalStatus());
        Assertions.assertEquals(
                approved.getDisposalAppAt(),
                after.getDisposalAppAt()
        );

        log.info("after : {}", after);

        // 기존 승인 정보 유지 확인
        Optional<DisposalAppInfos> afterOptional =
                disposalAppInfosRepository.getDisposalAppInfoByDisposalId(disposalId);

        Assertions.assertTrue(afterOptional.isPresent());

        DisposalAppInfos afterCreated = afterOptional.get();

        Assertions.assertEquals(
                created.getDisposalAppInfoId(),
                afterCreated.getDisposalAppInfoId()
        );
        Assertions.assertEquals(
                created.getDisposalId(),
                afterCreated.getDisposalId()
        );
        Assertions.assertEquals(
                created.getAppUserId(),
                afterCreated.getAppUserId()
        );

        log.info("after created dump : {}", afterCreated);
    }

    @Test
    @Transactional
    public void 폐기_거절() {
    }

    @Test
    @Transactional
    public void 폐기_삭제_성공() {
        Long disposalId = 4L; // 요청상태 폐기
        Assertions.assertNotNull(disposalsRepository.getDisposalById(disposalId));
        disposalService.deleteDisposal(disposalId);
        Assertions.assertNull(disposalsRepository.getDisposalById(disposalId));
    }

    @Test
    @Transactional
    public void 폐기_삭제_실패_승인된폐기() {
        Long disposalId = 3L; // 승인된 폐기
        Assertions.assertNotNull(disposalsRepository.getDisposalById(disposalId));
        disposalService.deleteDisposal(disposalId);
        Assertions.assertNotNull(disposalsRepository.getDisposalById(disposalId));
    }


//    내일할거
//    공통 예외 적용 - 어드바이스
//    페이지헬퍼 적용
//    아래테스트 진행하면서
//
    @Test
    public void 본사_폐기_목록_조회() {
//        HqDisposalSerachFilterDTO filterDTO, Long userId, int page, int size
        Long userId = 2L;
        int page = 2;
        int size = 5;

        PageInfo<HQDisposalListDTO> hqDisposals = disposalService.getHqDisposals(HqDisposalSerachFilterDTO.builder().build(), userId, page, size);
        List<HQDisposalListDTO> list = hqDisposals.getList();
        for (HQDisposalListDTO dto : list) {
            log.info("{}", dto);
        }
        log.info("전체 데이터 수: {}", hqDisposals.getTotal());
        log.info("전체 페이지 수: {}", hqDisposals.getPages());
        log.info("현재 페이지: {}", hqDisposals.getPageNum());
        log.info("페이지 크기: {}", hqDisposals.getPageSize());
        log.info("조회된 데이터 수: {}", hqDisposals.getList().size());
    }

    @Test
    public void 직영점_폐기_목록_조회() {
        int page = 1;
        int size = 100;
        PageInfo<StoreDisposalListDTO> storeDisposals = disposalService.getStoreDisposals(StoreDisposalSerachFilterDTO.builder().storeId(1L).build(), page, size);
        List<StoreDisposalListDTO> list = storeDisposals.getList();
        for (StoreDisposalListDTO dto : list) {
            log.info("{}", dto);
        }
        log.info("전체 데이터 수: {}", storeDisposals.getTotal());
        log.info("전체 페이지 수: {}", storeDisposals.getPages());
        log.info("현재 페이지: {}", storeDisposals.getPageNum());
        log.info("페이지 크기: {}", storeDisposals.getPageSize());
        log.info("조회된 데이터 수: {}", storeDisposals.getList().size());
    }

    @Test
    public void 금일_폐기_개수() {
        disposalService.countByTodayDisposed();
    }

    @Test
    public void 금일_폐기_금액() {
        disposalService.sumByTodayDisposedPrice();
    }

    @Test
    public void 특정_직영점_확인필요_폐기_개수() {
        Assertions.assertEquals(disposalService.countNeedToConfirmByStoreId(1L), 6);
    }
}
