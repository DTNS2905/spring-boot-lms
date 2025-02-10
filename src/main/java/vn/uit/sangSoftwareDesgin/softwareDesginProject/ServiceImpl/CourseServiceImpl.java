package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CourseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CourseResponseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.CourseRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.UserCourseImageRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CourseService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepo courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserCourseImageRepo userCourseImageRepository;

    @Override
    public Page<CourseResponseDTO> getAllCourses(Pageable pageable) {
        Page<Course> coursePage = courseRepository.findAll(pageable);

        return coursePage.map(course -> {
            CourseResponseDTO courseResponse = modelMapper.map(course, CourseResponseDTO.class);

            List<String> imageUrls = userCourseImageRepository.findManyByCourseId(course.getId())
                    .stream()
                    .map(uci -> uci.getImage().getUrl())
                    .collect(Collectors.toList());

            courseResponse.setImageUrls(imageUrls.isEmpty() ? null : imageUrls);
            return courseResponse;
        });
    }


    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
        return modelMapper.map(course, CourseDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = modelMapper.map(courseDTO, Course.class);
        Course savedCourse = courseRepository.save(course);
        return modelMapper.map(savedCourse, CourseDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));

        // Update fields
        existingCourse.setCourseName(courseDTO.getCourseName());
        existingCourse.setTitle(courseDTO.getTitle());
        existingCourse.setDescription(courseDTO.getDescription());
        existingCourse.setBeginDate(courseDTO.getBeginDate());
        existingCourse.setEndDate(courseDTO.getEndDate());
        existingCourse.setPrice(courseDTO.getPrice());

        Course updatedCourse = courseRepository.save(existingCourse);
        return modelMapper.map(updatedCourse, CourseDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}
