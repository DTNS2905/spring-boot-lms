package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.UserDTO;

import java.util.List;

public interface UserService {
    /**
     * Retrieve all users as DTOs.
     *
     * @return List of UserDTO
     */
    List<UserDTO> getAllUsers();

    /**
     * Create a new user from a UserDTO.
     *
     * @param userDTO the data transfer object containing user details
     * @return the created UserDTO
     */
    UserDTO createUser(UserDTO userDTO);

    /**
     * Assign courses to a user.
     *
     * @param userId    the ID of the user
     * @param courseIds the list of course IDs to assign
     * @return the updated UserDTO with assigned courses
     */
    UserDTO assignCoursesToUser(Long userId, List<Long> courseIds);




}
