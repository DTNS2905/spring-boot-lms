package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import org.springframework.http.ResponseEntity;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageDTO;

import java.util.Map;

public interface ImageService {
    ResponseEntity<Map<String, String>> uploadImage(ImageDTO imageModel);

    String getImageUrl(String imageId);

    ResponseEntity<Map<String, String>> getImageDetails(String imageId);
}
