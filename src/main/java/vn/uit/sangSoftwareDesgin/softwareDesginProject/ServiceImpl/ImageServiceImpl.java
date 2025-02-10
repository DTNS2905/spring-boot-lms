package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageCourseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageResponseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Pagination.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CloudinaryService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.ImageService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.LogService;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Enums.Role.LECTURER;

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
    @Transactional
    public ImageResponseDTO uploadAndAssociateImage(ImageCourseDTO imageRequest) {
        try {
            // Step 1: Upload image
            String uploadedUrl = cloudinaryService.uploadFile(imageRequest.getFile(), "spring_boot_lms");
            if (uploadedUrl == null) {
                throw new RuntimeException("Failed to upload image to Cloudinary.");
            }

            // Step 2: Save image in the database
            Image image = new Image();
            image.setName(imageRequest.getImageName());
            image.setUrl(uploadedUrl);
            imageRepository.save(image);

            // Step 3: Associate image (Rollback occurs here if it fails)
            associateImage(imageRequest, image.getId());

            logService.info("Image uploaded and associated successfully. URL: " + uploadedUrl);

            // Step 4: Return response DTO
            return modelMapper.map(image, ImageResponseDTO.class);

        } catch (Exception e) {
            logService.error("Transaction failed. Rolling back upload & association.", e);
            throw new RuntimeException("Image upload and association failed: " + e.getMessage(), e);
        }
    }

    @Override
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

            if(!user.getRoles().contains(LECTURER)) {
                throw new RuntimeException("Access denied for user: " + userName);
            }

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
    public <T> List<ImageResponseDTO> getImagesByReferenceId(
            Long referenceId,
            Function<Long, List<T>> fetchImagesFunction,
            String referenceType
    ) {
        try {
            // Fetch images dynamically
            List<T> imageMappings = fetchImagesFunction.apply(referenceId);

            if (imageMappings.isEmpty()) {
                throw new RuntimeException("No images found for " + referenceType + " ID: " + referenceId);
            }

            // Use Reflection to dynamically get the `Image` field
            return imageMappings.stream()
                    .map(mapping -> extractImage(mapping))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve images for " + referenceType + " ID: " + referenceId, e);
        }
    }

    /**
     * Dynamically extracts the `Image` field from any entity type.
     */
    private ImageResponseDTO extractImage(Object mapping) {
        try {
            // Use reflection to get the `getImage` method from any entity type
            Method getImageMethod = mapping.getClass().getMethod("getImage");
            Image image = (Image) getImageMethod.invoke(mapping);

            // Convert Image entity to DTO
            return modelMapper.map(image, ImageResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract image from entity: " + mapping.getClass().getSimpleName(), e);
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

