package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageDTO {
    private String name;
    private MultipartFile file;
}