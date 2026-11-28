package kr.swdl.foodingredientmanagement.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class DisposalAppInfoDTO {
    private MultipartFile disposalImg;
}
