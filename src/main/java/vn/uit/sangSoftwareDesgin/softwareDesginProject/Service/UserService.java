package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import org.springframework.data.domain.Page;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.UserDTO;

import java.util.List;

public interface UserService {
    /**
     * Retrieve all users as DTOs with pagination
     *
     * @return List of UserDTO
     */
    /**
     * Retrieve all users as DTOs with pagination.
     *
     * @param page the page number (zero-based).
     * @param size the number of records per page.
     * @return A paginated list of UserDTO.
     */
    Page<UserDTO> getAllUsers(int page, int size);

    /**
     * Retrieve a user as DTOs.
     * @param userId id of user
     * @return UserDTO (the data transfer object containing user details)
     */
    UserDTO getUserById(Long userId);

    /**
     * Retrieve a user as DTOs.
     * @param username username of user
     * @return UserDTO (the data transfer object containing user details)
     */
    UserDTO getUserByUsername(String username);

    /**
     * Retrieve all users as DTOs with limit and pagination
     *
     * @return List of UserDTO
     */
    List<UserDTO> getSomeUsers();

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
