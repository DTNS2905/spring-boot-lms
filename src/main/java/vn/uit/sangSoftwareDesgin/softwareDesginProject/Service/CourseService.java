package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CourseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CourseResponseDTO;

import java.util.List;

public interface CourseService {
    Page<CourseResponseDTO> getAllCourses(Pageable pageable);
    CourseDTO getCourseById(Long id);
    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(Long id, CourseDTO courseDTO);
    void deleteCourse(Long id);
}
