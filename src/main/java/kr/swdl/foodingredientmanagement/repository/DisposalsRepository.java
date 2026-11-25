package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.dto.*;
import kr.swdl.foodingredientmanagement.repository.vo.DisposalVO;
import kr.swdl.foodingredientmanagement.repository.vo.HQDisposalListVO;
import kr.swdl.foodingredientmanagement.repository.vo.StoreDisposalListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DisposalsRepository {

    List<DisposalVO> getDisposals();

    DisposalVO getDisposalById(@Param("disposalId")Long disposalId);

    // select insert update delete
    int addDisposal(DisposalVO disposalsVO);

    int updateDisposal(DisposalVO disposalsVO);

    int approveDisposal(DisposalVO disposalsVO);

    public void deleteDisposalById(@Param("disposalId") Long disposalId);

    public List<HQDisposalListVO> getHqDisposals(HqDisposalSerachFilterDTO hqDisposalSerachFilterDTO);

    public List<StoreDisposalListVO> getStoreDisposals(StoreDisposalSerachFilterDTO storeDisposalSerachFilterDTO);

    public int getCountByTodayDisposed();

    public int getSumByTodayDisposedPrice();

    public int getCountNeedToConfirmByStoreId(Long storeId);

    public List<Long> getStoreIdsByHQId(Long userId);
}
