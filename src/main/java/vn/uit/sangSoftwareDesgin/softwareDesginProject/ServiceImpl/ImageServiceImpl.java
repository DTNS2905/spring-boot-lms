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
import java.util.UUID;

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

    @Override
    public String getImageUrl(String imageId) {
        try {
            // Fetch the image from the database using its UUID
            Image image = imageRepository.findById(UUID.fromString(imageId))
                    .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));

            logService.info("Successfully retrieved image URL for ID: " + imageId);
            return image.getUrl();
        } catch (Exception e) {
            logService.error("Failed to retrieve image with ID: " + imageId, e);
            throw new RuntimeException("Failed to retrieve image. Error: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> getImageDetails(String imageId) {
        Map<String, String> response = new HashMap<>();
        try {
            // Fetch image from the database
            Image image = imageRepository.findById(UUID.fromString(imageId))
                    .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));

            // Prepare response with image details
            response.put("id", image.getId().toString());
            response.put("name", image.getName());
            response.put("url", image.getUrl());
            logService.info("Image details retrieved successfully for ID: " + imageId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logService.error("Failed to retrieve image details for ID: " + imageId, e);
            response.put("error", "Failed to retrieve image details. Error: " + e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }
}

