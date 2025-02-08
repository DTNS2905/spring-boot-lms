package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageCourseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageResponseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Pagination.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CloudinaryService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.ImageService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.LogService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LogService logService;

    @Autowired
    private ProfileImageRepo profileImageRepository;

    @Autowired
    private UserCourseImageRepo userCourseImageRepository;

    @Autowired
    private ImageRepo imageRepository;

    @Autowired
    private CourseRepo courseRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ProfileRepo profileRepository;



    @Override
    public ImageResponseDTO uploadImage(ImageCourseDTO imageModel) {
        try {
            // Validate input
            if (imageModel == null || imageModel.getFile() == null || imageModel.getFile().isEmpty()) {
                logService.warn("Image upload failed: File is empty or not provided.");
                throw new IllegalArgumentException("Image file is required.");
            }
            if (imageModel.getImageName() == null || imageModel.getImageName().isEmpty()) {
                logService.warn("Image upload failed: Name is empty or not provided.");
                throw new IllegalArgumentException("Image name is required.");
            }

            // Upload file to Cloudinary
            String uploadedUrl = cloudinaryService.uploadFile(imageModel.getFile(), "spring_boot_lms");
            if (uploadedUrl == null) {
                logService.error("Image upload to Cloudinary failed: Returned URL is null.");
                throw new RuntimeException("Failed to upload image to Cloudinary.");
            }

            // Save to database
            Image image = new Image();
            image.setName(imageModel.getImageName());
            image.setUrl(uploadedUrl);
            imageRepository.save(image);

            logService.info("Image uploaded successfully. URL: " + uploadedUrl);
            return modelMapper.map(image, ImageResponseDTO.class);

        } catch (Exception e) {
            logService.error("Unexpected error during image upload", e);
            throw new RuntimeException("An unexpected error occurred during image upload.", e);
        }
    }

    @Override
    //Decide where to associate the image
    public void associateImage(ImageCourseDTO imageRequest, UUID imageId) {
        if (imageRequest.getCourseId() != null) {
            associateImageWithCourse(imageRequest.getUserName(), imageRequest.getCourseId(), imageId);
        } else if (imageRequest.getProfileId() != null) {
            associateImageWithProfile(imageRequest.getProfileId(), imageId);
        }
    }

    private void associateImageWithCourse(String userName, Long courseId, UUID imageId) {
        try {
            // Fetch entities
            User user = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new RuntimeException("User not found with name: " + userName));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));

            // Create and save UserCourseImage entity
            UserCourseImage userCourseImage = new UserCourseImage();
            userCourseImage.setUser(user);
            userCourseImage.setCourse(course);
            userCourseImage.setImage(image);
            userCourseImageRepository.save(userCourseImage);

            logService.info("Image associated with course successfully for user name: " + userName);
        } catch (Exception e) {
            logService.error("Failed to associate image with course", e);
            throw new RuntimeException("Error associating image with course.", e);
        }
    }

    private void associateImageWithProfile(Long profileId, UUID imageId) {
        try {
            // Fetch entities
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new RuntimeException("User not found with name: " + profileId));

            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));

            // Create and save UserCourseImage entity
            ProfileImage profileImage = new ProfileImage();
            profileImage.setProfile(profile);
            profileImage.setImage(image);
            profileImageRepository.save(profileImage);

            logService.info("Image associated with course successfully for profile name: " + profileId);
        } catch (Exception e) {
            logService.error("Failed to associate image with course", e);
            throw new RuntimeException("Error associating image with course.", e);
        }
    }


    @Override
    public ImageResponseDTO getProfileImage(Long profileId) {
        try {
            // Fetch the ProfileImage entity using the profileId
            ProfileImage profileImage = profileImageRepository.findByProfileId(profileId)
                    .orElseThrow(() -> new RuntimeException("No profile image found for profile ID: " + profileId));

            // Get the associated image
            Image image = profileImage.getImage();
            if (image == null) {
                throw new RuntimeException("No image associated with profile ID: " + profileId);
            }

            logService.info("Successfully retrieved image URL for profile ID: " + profileId);
            return modelMapper.map(image, ImageResponseDTO.class);
        } catch (Exception e) {
            logService.error("Failed to retrieve image for profile ID: " + profileId, e);
            throw new RuntimeException("Failed to retrieve image. Error: " + e.getMessage(), e);
        }
    }

    @Override
    public ImageResponseDTO getCourseImage(Long courseId) {
        try {
            // Fetch the UserCourseImage entity using the courseId
            UserCourseImage userCourseImage = userCourseImageRepository.findByCourseId(courseId)
                    .orElseThrow(() -> new RuntimeException("No course image found for course ID: " + courseId));

            // Get the associated image
            Image image = userCourseImage.getImage();
            if (image == null) {
                throw new RuntimeException("No image associated with course ID: " + courseId);
            }

            logService.info("Successfully retrieved image URL for course ID: " + courseId);

            return modelMapper.map(image, ImageResponseDTO.class);
        } catch (Exception e) {
            logService.error("Failed to retrieve image for course ID: " + courseId, e);
            throw new RuntimeException("Failed to retrieve image. Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteImage(UUID imageId) {
        // Find image
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));

        // Delete from Cloudinary
        boolean cloudinaryDeleted = cloudinaryService.deleteFile(image.getUrl());
        if (!cloudinaryDeleted) {
            throw new RuntimeException("Failed to delete image from Cloudinary.");
        }

        // Delete from database (automatically removes rows in `UserCourseImage`)
        imageRepository.delete(image);
        logService.info("Image deleted successfully: " + imageId);
    }

    @Override
    public ImageResponseDTO getImageById(UUID imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));

        return new ImageResponseDTO(image.getId(), image.getName(), image.getUrl());
    }

    @Override
    public ApiResponse<List<ImageResponseDTO>> getAllImages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Image> imagePage = imageRepository.findAll(pageable);

        List<ImageResponseDTO> images = imagePage.getContent()
                .stream()
                .map(img -> new ImageResponseDTO(img.getId(), img.getName(), img.getUrl()))
                .collect(Collectors.toList());

        ApiResponse.Pagination pagination = ApiResponse.Pagination.of(
                imagePage.getNumber(), imagePage.getSize(), imagePage.getTotalElements()
        );

        return ApiResponse.success("Images retrieved successfully", images, pagination);
    }

    @Override
    public ApiResponse<List<ImageResponseDTO>> getCourseImages(Long courseId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<UserCourseImage> courseImages = userCourseImageRepository.findByCourseId(courseId, pageable);

        List<ImageResponseDTO> images = courseImages.getContent()
                .stream()
                .map(uci -> new ImageResponseDTO(uci.getImage().getId(), uci.getImage().getName(), uci.getImage().getUrl()))
                .collect(Collectors.toList());

        ApiResponse.Pagination pagination = ApiResponse.Pagination.of(
                courseImages.getNumber(), courseImages.getSize(), courseImages.getTotalElements()
        );

        return ApiResponse.success("Course images retrieved successfully", images, pagination);
    }

    @Override
    public ApiResponse<List<ImageResponseDTO>> getProfileImages(Long profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ProfileImage> profileImages = profileImageRepository.findByProfileId(profileId, pageable);

        List<ImageResponseDTO> images = profileImages.getContent()
                .stream()
                .map(pi -> new ImageResponseDTO(pi.getImage().getId(), pi.getImage().getName(), pi.getImage().getUrl()))
                .collect(Collectors.toList());

        ApiResponse.Pagination pagination = ApiResponse.Pagination.of(
                profileImages.getNumber(), profileImages.getSize(), profileImages.getTotalElements()
        );

        return ApiResponse.success("Profile images retrieved successfully", images, pagination);
    }
}

