package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.ResponseInstance.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.LogService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;


    @GetMapping("/get-all-users")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
    try {
        Page<UserDTO> userPage = userService.getAllUsers(page, size);

        ApiResponse.Pagination pagination = new ApiResponse.Pagination(
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );

        ApiResponse<Page<UserDTO>> response = ApiResponse.success(
                "Users retrieved successfully",
                userPage,
                pagination
        );

        return ResponseEntity.ok(response);
    }
        catch (Exception e) {
            logService.error("Error occurred during image upload", e);
            ApiResponse<Page<UserDTO>> response = ApiResponse.error(
                    "Error during fetching users"
                    );
            return ResponseEntity.status(500).body(response);
        }

    }

    @PostMapping("/{userId}/assign-courses")
    public ResponseEntity<ApiResponse<UserDTO>> assignCoursesToUser(
            @PathVariable Long userId,
            @RequestBody List<Long> courseIds) {
        UserDTO updatedUser = userService.assignCoursesToUser(userId, courseIds);
        ApiResponse<UserDTO> response = ApiResponse.success("Courses assigned to user successfully", updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
