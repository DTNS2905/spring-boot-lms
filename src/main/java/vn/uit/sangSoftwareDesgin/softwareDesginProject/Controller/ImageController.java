package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageCourseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageResponseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Image;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Pagination.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.ImageService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.LogService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private LogService logService;

    @PostMapping("/upload-image")
    public ResponseEntity<ApiResponse<ImageResponseDTO>> uploadImage(
            @ModelAttribute ImageCourseDTO imageRequest
    ) {
        try {
            logService.info("Received request to upload an image.");

            // Upload image and get response DTO
            ImageResponseDTO imageResponse = imageService.uploadAndAssociateImage(imageRequest);

            // Success response
            ApiResponse<ImageResponseDTO> response = ApiResponse.success(
                    "Image uploaded successfully",
                    imageResponse
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logService.error("Error occurred during image upload", e);
            ApiResponse<ImageResponseDTO> response = ApiResponse.error(
                    "Image upload failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("profile/{profileId}/get-one")
    public ResponseEntity<ApiResponse<ImageResponseDTO>> getProfileImage(@PathVariable Long profileId) {
        try {
            logService.info("Fetching image with ID: " + profileId);
            ImageResponseDTO imageResponse = imageService.getProfileImage(profileId);

            ApiResponse<ImageResponseDTO> successReponse = ApiResponse.success(
                    "Image get successfully",
                    imageResponse
            );
            return ResponseEntity.status(HttpStatus.OK).body(successReponse);
        } catch (Exception e) {
            logService.error("Error occurred while fetching the profile with ID: " + profileId, e);
            ApiResponse<ImageResponseDTO> errorResponse = ApiResponse.error(
                    "Profile image can not get"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("course/{courseId}/get-one")
    public ResponseEntity<ApiResponse<ImageResponseDTO>> getCourseImage(@PathVariable Long courseId) {
        try {
            logService.info("Fetching image with ID: " + courseId);
            ImageResponseDTO imageResponse = imageService.getCourseImage(courseId);

            ApiResponse<ImageResponseDTO> successReponse = ApiResponse.success(
                    "Image get successfully",
                    imageResponse
            );
            return ResponseEntity.status(HttpStatus.OK).body(successReponse);
        } catch (Exception e) {
            logService.error("Error occurred while fetching the profile with ID: " + courseId, e);
            ApiResponse<ImageResponseDTO> errorResponse = ApiResponse.error(
                    "Profile image can not get"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/delete-image/{imageId}")
    public ResponseEntity<ApiResponse<String>> deleteImage(@PathVariable UUID imageId) {
        try {
            imageService.deleteImage(imageId);
            ApiResponse<String> response = ApiResponse.success("Image deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logService.error("Error occurred while deleting image", e);
            ApiResponse<String> response = ApiResponse.error("Failed to delete image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}/get-one")
    public ResponseEntity<ApiResponse<ImageResponseDTO>> getImageById(@PathVariable UUID id) {
        ImageResponseDTO image = imageService.getImageById(id);
        return ResponseEntity.ok(ApiResponse.success("Image retrieved successfully", image));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<ImageResponseDTO>>> getAllImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse<List<ImageResponseDTO>> response = imageService.getAllImages(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}/get-all")
    public ResponseEntity<ApiResponse<List<ImageResponseDTO>>> getCourseImages(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse<List<ImageResponseDTO>> response = imageService.getCourseImages(courseId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{profileId}/get-all")
    public ResponseEntity<ApiResponse<List<ImageResponseDTO>>> getProfileImages(
            @PathVariable Long profileId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse<List<ImageResponseDTO>> response = imageService.getProfileImages(profileId, page, size);
        return ResponseEntity.ok(response);
    }
}

