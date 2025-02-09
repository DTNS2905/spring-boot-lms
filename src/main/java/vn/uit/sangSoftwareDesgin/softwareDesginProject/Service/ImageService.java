package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageCourseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ImageResponseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Pagination.ApiResponse;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface ImageService {

    ImageResponseDTO getImageById(UUID imageId);

    ApiResponse<List<ImageResponseDTO>> getAllImages(int page, int size);

    ImageResponseDTO uploadAndAssociateImage(ImageCourseDTO imageRequest);

    void associateImage(ImageCourseDTO imageRequest, UUID imageId);

    void deleteImage(UUID imageId);

    ImageResponseDTO getProfileImage(Long profileId);

    ImageResponseDTO getCourseImage(Long courseId);

    ApiResponse<List<ImageResponseDTO>> getProfileImages(Long profileId, int page, int size);

    ApiResponse<List<ImageResponseDTO>> getCourseImages(Long courseId, int page, int size);

    <T> List<ImageResponseDTO> getImagesByReferenceId(
            Long referenceId,
            Function<Long, List<T>> fetchImagesFunction,
            String referenceType
    );

}
