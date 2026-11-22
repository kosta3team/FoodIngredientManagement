package kr.swdl.foodingredientmanagement.repository;

import kr.swdl.foodingredientmanagement.repository.entitiy.DisposalAppInfos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@SpringBootTest
public class JPADisposalAppInfoTest {

    @Autowired
    private DisposalAppInfosRepository disposalAppInfosRepository;

    @Test
    @Transactional
    public void 폐기승인정보_생성() {
        DisposalAppInfos disposalAppInfo = DisposalAppInfos.builder()
                .appUserId(Long.valueOf(1))
                .disposalId(Long.valueOf(1)).build();

        DisposalAppInfos created = disposalAppInfosRepository.save(disposalAppInfo);
        Assertions.assertEquals(disposalAppInfo, created);
    }

    @Test
    @Transactional
    public void 폐기승인정보_업데이트() {
        Optional<DisposalAppInfos> optionalDisposalAppInfo = disposalAppInfosRepository.findById(Long.valueOf(1));
        Assertions.assertFalse(optionalDisposalAppInfo.isEmpty());
        DisposalAppInfos disposalAppInfo = optionalDisposalAppInfo.get();
        disposalAppInfo.setCheckAt(new Timestamp(System.currentTimeMillis()));
//        disposalAppInfo.setDisposalImg();
        disposalAppInfo.setCheckUserId(Long.valueOf(1));

        DisposalAppInfos updated = disposalAppInfosRepository.save(disposalAppInfo);
        Assertions.assertEquals(disposalAppInfo, updated);
    }
}
