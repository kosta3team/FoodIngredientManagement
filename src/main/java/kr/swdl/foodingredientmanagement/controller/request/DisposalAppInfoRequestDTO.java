package kr.swdl.foodingredientmanagement.controller.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DisposalAppInfoRequestDTO {
    private MultipartFile disposalImg;
}
