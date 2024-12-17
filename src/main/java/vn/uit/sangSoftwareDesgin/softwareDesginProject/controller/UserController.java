package vn.uit.sangSoftwareDesgin.softwareDesginProject.controller;


import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PostMapping("/{userId}/assign-courses")
    public UserDTO assignCoursesToUser(@PathVariable Long userId, @RequestBody List<Long> courseIds) {
        return userService.assignCoursesToUser(userId, courseIds);
    }
}
