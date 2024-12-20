package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.ResponseInstance.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-all-users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        ApiResponse<List<UserDTO>> response = new ApiResponse<>("success", "Users retrieved successfully", users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        ApiResponse<UserDTO> response = new ApiResponse<>("success", "User created successfully", createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> login(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        ApiResponse<UserDTO> response = new ApiResponse<>("success", "User created successfully", createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/assign-courses")
    public ResponseEntity<ApiResponse<UserDTO>> assignCoursesToUser(
            @PathVariable Long userId,
            @RequestBody List<Long> courseIds) {
        UserDTO updatedUser = userService.assignCoursesToUser(userId, courseIds);
        ApiResponse<UserDTO> response = new ApiResponse<>("success", "Courses assigned to user successfully", updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
