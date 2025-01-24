package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.ImageService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.LogService;

import java.util.Map;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private LogService logService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@ModelAttribute ImageDTO imageModel) {
        try {
            logService.info("Received request to upload an image.");
            return imageService.uploadImage(imageModel);
        } catch (Exception e) {
            logService.error("Error occurred during image upload", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error. Please try again later."));
        }
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable String imageId) {
        try {
            logService.info("Fetching image with ID: " + imageId);
            String imageUrl = imageService.getImageUrl(imageId);
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (Exception e) {
            logService.error("Error occurred while fetching the image with ID: " + imageId, e);
            return ResponseEntity.status(404).body(Map.of("error", "Image not found."));
        }
    }

    @GetMapping("/details/{imageId}")
    public ResponseEntity<?> getImageDetails(@PathVariable String imageId) {
        try {
            logService.info("Fetching image details with ID: " + imageId);
            return imageService.getImageDetails(imageId);
        } catch (Exception e) {
            logService.error("Error occurred while fetching image details with ID: " + imageId, e);
            return ResponseEntity.status(404).body(Map.of("error", "Image details not found."));
        }
    }
}

