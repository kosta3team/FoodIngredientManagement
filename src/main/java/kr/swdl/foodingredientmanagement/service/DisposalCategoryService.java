package kr.swdl.foodingredientmanagement.service;

import kr.swdl.foodingredientmanagement.repository.DisposalCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisposalCategoryService {

    private final DisposalCategoryRepository disposalCategoryRepository;

    public List<String> getDisposalTypes() {
        return disposalCategoryRepository.getDisposalTypes();
    }

    public List<String> getDisposalReasonsByDisposalType(String disposalType) {
        return disposalCategoryRepository.getDisposalReasonsByDisposalType(disposalType);
    }
}
