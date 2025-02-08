package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageCourseDTO {
    private String imageName;
    private Long courseId;
    private Long profileId;
    private String userName;
    private MultipartFile file;

}