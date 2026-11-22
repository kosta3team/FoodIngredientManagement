package kr.swdl.foodingredientmanagement.service;

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
                .disposalQuantity(disposalDTO.getDisposalQuantity())
                .disposalPrice(ingredientDTO.getSupplyPrice() * disposalDTO.getDisposalQuantity())
                .orderDetailId(orderDetailId)
                .build();
        disposalsRepository.insertDisposal(disposalVO);

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
    public void approveDisposal(Long appUserId, DisposalDTO disposalDTO) {
        disposalsRepository.approveDisposal(DisposalVO.builder()
                .disposalAppAt(new Timestamp(System.currentTimeMillis()))
                .disposalStatus(disposalDTO.getDisposalStatus())
                .rejectReason(disposalDTO.getRejectReason()).build());
        if (disposalDTO.getDisposalStatus().equals("승인")) {
            disposalAppInfosRepository.save(DisposalAppInfos.builder()
                    .appUserId(appUserId)
                    .disposalId(disposalDTO.getDisposalId()).build());
        }
    }

    // 폐기 삭제
    public void deleteDisposal(Long disposalId) {
        disposalsRepository.deleteDisposalById(disposalId);
    }

    public List<HQDisposalListDTO> getHqDisposals(HqDisposalSerachFilterDTO filterDTO) {
        return disposalsRepository.getHqDisposals(filterDTO).stream()
                .map(vo -> toHqDisposalListDTO(vo))
                .toList();
    }

    public List<StoreDisposalListDTO> getStoreDisposals(StoreDisposalSerachFilterDTO filterDTO) {
        return disposalsRepository.getStoreDisposals(filterDTO).stream()
                .map(vo -> toStoreDisposalListDTO(vo))
                .toList();
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
