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

    List<DisposalVO> findAll();

    DisposalVO findById(@Param("disposalId")Long disposalId);

    // select insert update delete
    int insertDisposal(DisposalVO disposalsVO);

    int updateDisposal(DisposalVO disposalsVO);

    int approveDisposal(DisposalVO disposalsVO);

    public void deleteDisposalById(@Param("disposalId") Long disposalId);

    public List<HQDisposalListVO> getHqDisposals(HqDisposalSerachFilterDTO hqDisposalSerachFilterDTO);

    public List<StoreDisposalListVO> getStoreDisposals(StoreDisposalSerachFilterDTO storeDisposalSerachFilterDTO);

    public int countByTodayDisposed();

    public int sumByTodayDisposedPrice();

    public int countNeedToConfirmByStoreId(Long storeId);
}
