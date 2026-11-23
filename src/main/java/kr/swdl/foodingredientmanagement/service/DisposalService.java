package kr.swdl.foodingredientmanagement.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.*;
import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalAppInfos;
import kr.swdl.foodingredientmanagement.repository.entitiy.Users;
import kr.swdl.foodingredientmanagement.repository.vo.DisposalVO;
import kr.swdl.foodingredientmanagement.repository.vo.HQDisposalListVO;
import kr.swdl.foodingredientmanagement.repository.vo.OrderDetailVO;
import kr.swdl.foodingredientmanagement.repository.vo.StoreDisposalListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisposalService {
    private final JPAUsersRepository jPAUsersRepository;
    private final DisposalAppInfosRepository disposalAppInfosRepository;
    private final DisposalsRepository disposalsRepository;
    private final MBInventoryRepository mbInventoryRepository;
    private final MBIngredientRepository mbIngredientRepository;

    // 폐기 등록
    @Transactional
    public boolean addDisposal(Long orderDetailId, DisposalDTO disposalDTO) {
        Users reqUser = jPAUsersRepository.findById(disposalDTO.getReqUserId()).orElseThrow();
        String status = "승인";
        if (!reqUser.getRole().equals("HQ")) {
            status = "대기";
        }
        // 특정 재고 아이디
        OrderDetailVO orderDetailVO = mbInventoryRepository.findById(orderDetailId);
        if (null == orderDetailVO) {
            return false;
        }
        IngredientDetailDTO ingredientDTO = mbIngredientRepository.findIngredientDetail(orderDetailVO.getIngredientId());
        if (null == ingredientDTO) {
            return false;
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
                .disposalPrice(ingredientDTO.getSupplyPrice() * disposalDTO.getDisposalQuantity())
                .orderDetailId(orderDetailId)
                .build();
        disposalsRepository.insertDisposal(disposalVO);
        mbInventoryRepository.updateCurrentQuantityById(orderDetailId, 0);

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
        return disposalsRepository.countByTodayDisposed();
    }

    public int sumByTodayDisposedPrice() {
        return disposalsRepository.sumByTodayDisposedPrice();
    }

    public int countNeedToConfirmByStoreId(Long storeId) {
        return disposalsRepository.countNeedToConfirmByStoreId(storeId);
    }

    private DisposalVO getDisposalById(Long disposalId) {
        return disposalsRepository.findById(disposalId);// -> 예외 발생할수 있음
    }

    private HQDisposalListDTO toHqDisposalListDTO(HQDisposalListVO vo) {
        return HQDisposalListDTO.builder()
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
}
