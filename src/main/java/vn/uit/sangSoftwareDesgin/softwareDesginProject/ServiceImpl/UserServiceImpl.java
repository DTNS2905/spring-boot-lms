package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.springframework.data.domain.PageRequest;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.UserDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Enrollment;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Exception.CourseNotFoundException;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Exception.UserNotFoundException;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.CourseRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.UserRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private CourseRepo courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<UserDTO> getAllUsers(int page, int size) {
        // Fetch paginated list of users
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));

        // Map entities to DTOs
        return userPage.map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public UserDTO getUserById(Long userId) {
        Optional<User> userInfo = userRepository.findById(userId);
        return modelMapper.map(userInfo, UserDTO.class);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        Optional<User> userInfo = userRepository.findByUsername(username);
        return modelMapper.map(userInfo, UserDTO.class);
    }


    @Override
    public List<UserDTO> getSomeUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO assignCoursesToUser(Long userId, List<Long> courseIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        List<Course> courses = courseRepository.findAllById(courseIds);
        if (courses.size() != courseIds.size()) {
            throw new CourseNotFoundException("Some courses were not found for the provided IDs.");
        }

        Set<Enrollment> enrollments = courses.stream()
                .map(course -> {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setUser(user);
                    enrollment.setCourse(course);
                    return enrollment;
                })
                .collect(Collectors.toSet());

        user.getEnrollments().clear(); // Remove previous enrollments if needed
        user.getEnrollments().addAll(enrollments);

        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDTO.class);
    }




}
