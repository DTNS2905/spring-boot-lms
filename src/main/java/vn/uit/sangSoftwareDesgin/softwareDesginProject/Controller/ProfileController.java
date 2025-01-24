package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ProfileCreateDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ProfileResponseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ProfileUpdateDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Pagination.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.LogService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private LogService logService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{userName}/get-profile")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> getProfileById(@PathVariable String userName
    ) {
        try {
            // Retrieve the profile by ID
            Profile foundProfile = profileService.getProfile(userName);


            if (foundProfile == null) {
                // If no profile is found, return a NOT_FOUND response

                ApiResponse<ProfileResponseDTO>respond = ApiResponse.error("Profile not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respond);
            }

            // Map the found Profile entity to the ProfileResponseDTO DTO
            ProfileResponseDTO profileResponse = modelMapper.map(foundProfile, ProfileResponseDTO.class);

            ApiResponse<ProfileResponseDTO>respond = ApiResponse.success(
                    "Profile retrieved successfully", profileResponse
            );
            // Return the response with a success message
            return ResponseEntity.ok(respond);
        } catch (Exception e) {

            // Handle any unexpected errors
            logService.error("Error retrieving profile by ID", e);  
            String message = e.getMessage();
            ApiResponse<ProfileResponseDTO> respond = ApiResponse.error(message, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respond);
        }
    }

    @PostMapping("/{userName}/create-user-profile")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> createUserProfile(
            @PathVariable String userName,
            @RequestBody ProfileCreateDTO profileCreateDTO) {
        try {
            // Create the profile using the provided DTO
            Profile createdProfile = profileService.createProfile(userName,profileCreateDTO);

            if (createdProfile == null) {

                // If profile creation fails, return a NOT_FOUND response
                ApiResponse<ProfileResponseDTO> respond = ApiResponse.error("error", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respond);
            }

            // Map the created Profile entity to the ProfileResponseDTO DTO
            ProfileResponseDTO profileResponse = modelMapper.map(createdProfile, ProfileResponseDTO.class);

            // Construct the success response
            ApiResponse<ProfileResponseDTO> respond = ApiResponse.success("Profile created successfully", null);
            return ResponseEntity.ok(respond);
        } catch (Exception e) {
            // Handle any unexpected errors
            logService.error("Error occurred during profile creation", e);
            ApiResponse<ProfileResponseDTO> respond = ApiResponse.error("Error occurred during profile creation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respond);
        }
    }

    @PutMapping("/{username}/update-user-profile")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> updateUserProfile(
            @PathVariable String username,
            @RequestBody ProfileUpdateDTO profileUpdateDTO) {
        try {
            // Create the profile using the provided DTO
            Profile createdProfile = profileService.updateProfile(username,profileUpdateDTO);

            if (createdProfile == null) {
                // If profile creation fails, return a NOT_FOUND response
                ApiResponse<ProfileResponseDTO> respond = ApiResponse.error("error");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respond);
            }

            // Map the created Profile entity to the ProfileResponseDTO DTO
            ProfileResponseDTO profileResponse = modelMapper.map(createdProfile, ProfileResponseDTO.class);

            // Construct the success response
            ApiResponse<ProfileResponseDTO> respond = ApiResponse.success(
                    "Profile updated successfully", profileResponse
            );
            return ResponseEntity.ok(respond);
        } catch (Exception e) {
            // Handle any unexpected errors
            logService.error("Error occurred during profile creation", e);

            ApiResponse<ProfileResponseDTO> respond = ApiResponse.error("Error occurred during profile creation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respond);
        }
    }

}
