package vn.uit.sangSoftwareDesgin.softwareDesginProject.service;

import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.UserDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.ExceptionHandler.UserNotFoundException;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.model.Course;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.repo.CourseRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.repo.UserRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService  {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private CourseRepo courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserDTO> getAllUsers() {
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

        Set<Course> courses = new HashSet<>(courseRepository.findAllById(courseIds));
        user.setCourses(courses);

        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDTO.class);
    }
}
