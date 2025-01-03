package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Image;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.ImageRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CloudinaryService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.ImageService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.LogService;

import java.util.HashMap;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ImageRepo imageRepository;

    @Autowired
    private LogService logService;

    @Override
    public ResponseEntity<Map<String, String>> uploadImage(ImageDTO imageModel) {
        Map<String, String> response = new HashMap<>();
        try {
            // Validate input
            if (imageModel == null || imageModel.getFile() == null || imageModel.getFile().isEmpty()) {
                logService.warn("Image upload failed: File is empty or not provided.");
                response.put("error", "Image file is required.");
                return ResponseEntity.badRequest().body(response);
            }
            if (imageModel.getName() == null || imageModel.getName().isEmpty()) {
                logService.warn("Image upload failed: Name is empty or not provided.");
                response.put("error", "Image name is required.");
                return ResponseEntity.badRequest().body(response);
            }

            // Upload file to Cloudinary
            String uploadedUrl = cloudinaryService.uploadFile(imageModel.getFile(), "spring_boot_lms");
            if (uploadedUrl == null) {
                logService.error("Image upload to Cloudinary failed: Returned URL is null.", null);
                response.put("error", "Failed to upload image to Cloudinary.");
                return ResponseEntity.badRequest().body(response);
            }

            // Save to database
            Image image = new Image();
            image.setName(imageModel.getName());
            image.setUrl(uploadedUrl);
            imageRepository.save(image);

            // Return success response
            response.put("url", uploadedUrl);
            logService.info("Image uploaded successfully. URL: " + uploadedUrl);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logService.error("Unexpected error during image upload", e);
            response.put("error", "An unexpected error occurred. Please try again.");
            return ResponseEntity.status(500).body(response);
        }
    }
}
