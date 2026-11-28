package kr.swdl.foodingredientmanagement.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.exception.ErrorDomain;
import kr.swdl.foodingredientmanagement.exception.NotFoundException;
import kr.swdl.foodingredientmanagement.exception.UpdateFailedException;
import kr.swdl.foodingredientmanagement.repository.*;
import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalAppInfos;
import kr.swdl.foodingredientmanagement.repository.entitiy.Users;
import kr.swdl.foodingredientmanagement.repository.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisposalService {
    private final UserRepository jPAUserRepository;
    private final DisposalAppInfosRepository disposalAppInfosRepository;
    private final DisposalsRepository disposalsRepository;
    private final InventoryRepository inventoryRepository;
    private final MBIngredientRepository mbIngredientRepository;
    private final ImageHandler imageHandler;

    // 폐기 등록
    @Transactional
    public boolean addDisposal(Long orderDetailId, DisposalDTO disposalDTO) {
        Users reqUser = jPAUserRepository
                .findById(disposalDTO.getReqUserId()).orElseThrow();
        String status = "승인";
        if (!reqUser.getRole().equals("HQ")) {
            status = "대기";
        }
        // 특정 재고 아이디
        OrderDetailVO orderDetailVO = inventoryRepository.findById(orderDetailId);
        if (null == orderDetailVO) {
            throw new NotFoundException(ErrorDomain.ORDER_DETAIL);
        }
        IngredientVO ingredientVO = mbIngredientRepository.findIngredientById(orderDetailVO.getIngredientId());
        if (null == ingredientVO) {
            throw new NotFoundException(ErrorDomain.INGREDIENT);
        }
        DisposalVO disposalVO = DisposalVO.builder()
                .disposalUnit(disposalDTO.getDisposalUnit())
                .disposalReqAt(new Timestamp(System.currentTimeMillis()))
                .reqUserId(disposalDTO.getReqUserId())
                .disposalStatus(status)
                .processingMethod(disposalDTO.getProcessingMethod())
                .disposalType(disposalDTO.getDisposalType())
                .disposalReason(disposalDTO.getDisposalReason())
                .disposalDetailReason(disposalDTO.getDisposalDetailReason())
                .disposalQuantity(orderDetailVO.getCurrentQuantity())
                .disposalPrice(ingredientVO.getSupplyPrice() * disposalDTO.getDisposalQuantity())
                .orderDetailId(orderDetailId)
                .build();

        disposalsRepository.addDisposal(disposalVO);
        inventoryRepository.updateCurrentQuantityById(orderDetailId, 0);

        if (status.equals("승인") && null != disposalVO.getDisposalId()) {
            disposalAppInfosRepository.save(DisposalAppInfos.builder()
                    .appUserId(disposalVO.getReqUserId())
                    .disposalId(disposalVO.getDisposalId()).build());
        }
        return true;
    }

    // 폐기 수정
    public boolean updateDisposal(Long disposalId, DisposalDTO disposalDTO) {
        return 1 == disposalsRepository.updateDisposal(DisposalVO.builder()
                .disposalId(disposalId)
                .disposalType(disposalDTO.getDisposalType())
                .disposalReason(disposalDTO.getDisposalReason())
                .disposalDetailReason(disposalDTO.getDisposalDetailReason()).build());
    }

    // 폐기 승인/거절
    @Transactional
    public void approveDisposal(Long appUserId, Long disposalId, DisposalDTO disposalDTO) {
        // TODO : controller에서 appUserId의 롤 체크 이후, 관리자가 아니면 접근을 막는게 추가되어야 한다.
        int updated = disposalsRepository.approveDisposal(DisposalVO.builder()
                .disposalId(disposalId)
                .disposalAppAt(new Timestamp(System.currentTimeMillis()))
                .disposalStatus(disposalDTO.getDisposalStatus())
                .rejectReason(disposalDTO.getRejectReason())
                .build());

        // 승인/거절 처리 실패
        if (updated != 1) {
            throw new IllegalStateException("이미 처리되었거나 존재하지 않는 폐기 요청입니다.");
        }

        // 승인 정보 생성
        if ("승인".equals(disposalDTO.getDisposalStatus())) {
            disposalAppInfosRepository.save(DisposalAppInfos.builder()
                    .appUserId(appUserId)
                    .disposalId(disposalId)
                    .build());
        }
    }

    // 폐기 삭제
    public void deleteDisposal(Long disposalId) {
        disposalsRepository.deleteDisposalById(disposalId);
    }

    public PageInfo<HQDisposalListDTO> getHqDisposals(HqDisposalSerachFilterDTO filterDTO, Long userId, int page, int size) {
        filterDTO.setStoreIds(disposalsRepository.getStoreIdsByHQId(userId));
        PageHelper.startPage(page, size);
        return new PageInfo<>(disposalsRepository.getHqDisposals(filterDTO)).convert(this::toHqDisposalListDTO);
    }

    public PageInfo<StoreDisposalListDTO> getStoreDisposals(StoreDisposalSerachFilterDTO filterDTO, int page, int size) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(disposalsRepository.getStoreDisposals(filterDTO)).convert(this::toStoreDisposalListDTO);
    }

    public int countByTodayDisposed() {
        return disposalsRepository.getCountByTodayDisposed();
    }

    public int sumByTodayDisposedPrice() {
        return disposalsRepository.getSumByTodayDisposedPrice();
    }

    public int countNeedToConfirmByStoreId(Long storeId) {
        return disposalsRepository.getCountNeedToConfirmByStoreId(storeId);
    }

    public DisposalDetailDTO getDisposalDetailByDisposalId(Long disposalId) {
        DisposalDetailVO vo =
                disposalsRepository.getDisposalDetailByDisposalId(disposalId);
        if (vo == null) {
            throw new NotFoundException(ErrorDomain.DISPOSAL);
        }
        return toDisposalDetailDTO(vo);
    }

    @Transactional
    public void updateDisposalAppInfo(Long checkUserId, Long disposalId, DisposalAppInfoDTO disposalAppInfoDTO) {
        DisposalAppInfos disposalAppInfo = disposalAppInfosRepository.findByDisposalId(disposalId).orElseThrow(() ->
                new NotFoundException(ErrorDomain.DISPOSAL_APP_INFO)
        );

        // 영속성 1차 캐시로 여기서 저장하면 반영되는걸로 아는데 진짜 되나?
        disposalAppInfo.setCheckUserId(checkUserId);
        disposalAppInfo.setCheckAt(new Timestamp(System.currentTimeMillis()));
        disposalAppInfo.setDisposalImg(imageHandler.saveImage(disposalAppInfoDTO.getDisposalImg()));

        // 완료 상태변경
        if (1 != disposalsRepository.updateDisposalStatusToCompleted(disposalId)) {
            throw new UpdateFailedException(ErrorDomain.DISPOSAL);
        }

        // 안되는 경우
//        if (null == disposalAppInfosRepository.save(disposalAppInfo)) {
//            throw new UpdateFailedException(ErrorDomain.DISPOSAL_APP_INFO);
//        }
    }

    private DisposalVO getDisposalById(Long disposalId) {
        return disposalsRepository.getDisposalById(disposalId);// -> 예외 발생할수 있음
    }

    private HQDisposalListDTO toHqDisposalListDTO(HQDisposalListVO vo) {
        return HQDisposalListDTO.builder()
                .disposalId(vo.getDisposalId())
                .disposalReqAt(vo.getDisposalReqAt())
                .storeName(vo.getStoreName())
                .ingredientName(vo.getIngredientName())
                .disposalQuantity(vo.getDisposalQuantity())
                .disposalPrice(vo.getDisposalPrice())
                .disposalType(vo.getDisposalType())
                .processingMethod(vo.getProcessingMethod())
                .disposalStatus(vo.getDisposalStatus())
                .checkAt(vo.getCheckAt())
                .build();
    }



    private StoreDisposalListDTO toStoreDisposalListDTO(StoreDisposalListVO vo) {
        return StoreDisposalListDTO.builder()
                .disposalId(vo.getDisposalId())
                .disposalReqAt(vo.getDisposalReqAt())
                .ingredientName(vo.getIngredientName())
                .disposalQuantity(vo.getDisposalQuantity())
                .disposalPrice(vo.getDisposalPrice())
                .disposalType(vo.getDisposalType())
                .processingMethod(vo.getProcessingMethod())
                .disposalStatus(vo.getDisposalStatus())
                .checkAt(vo.getCheckAt())
                .build();
    }

    private DisposalDetailDTO toDisposalDetailDTO(DisposalDetailVO vo) {

        return DisposalDetailDTO.builder()

                // 기본 정보
                .disposalId(vo.getDisposalId())
                .orderDetailId(vo.getOrderDetailId())
                .disposalUnit(vo.getDisposalUnit())
                .disposalStatus(vo.getDisposalStatus())

                // 폐기 정보
                .storeName(vo.getStoreName())
                .ingredientName(vo.getIngredientName())
                .processingMethod(vo.getProcessingMethod())
                .disposalQuantity(vo.getDisposalQuantity())
                .orderUnit(vo.getOrderUnit())

                // 폐기 요청 정보
                .disposalReqAt(vo.getDisposalReqAt())
                .reqUserId(vo.getReqUserId())
                .reqUserName(vo.getReqUserName())
                .reqUserRole(vo.getReqUserRole())

                // 폐기 승인 정보
                .disposalAppAt(vo.getDisposalAppAt())
                .appUserId(vo.getAppUserId())
                .appUserName(vo.getAppUserName())
                .appUserRole(vo.getAppUserRole())

                // 폐기 유형 및 사유
                .disposalType(vo.getDisposalType())
                .disposalReason(vo.getDisposalReason())
                .disposalDetailReason(vo.getDisposalDetailReason())
                .rejectReason(vo.getRejectReason())

                // 금액 정보
                .supplyPrice(vo.getSupplyPrice())
                .disposalPrice(vo.getDisposalPrice())

                // 폐기 확인 정보
                .disposalAppInfoId(vo.getDisposalAppInfoId())
                .checkAt(vo.getCheckAt())
                .checkUserId(vo.getCheckUserId())
                .checkUserName(vo.getCheckUserName())
                .checkUserRole(vo.getCheckUserRole())
                .disposalImg(imageHandler.getImage(vo.getDisposalImg()) )

                .build();
    }


}
