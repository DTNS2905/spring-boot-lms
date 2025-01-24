package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CourseDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Pagination.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Get all courses
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getAllCoursesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Create a Pageable object
        Pageable pageable = PageRequest.of(page, size);

        // Fetch paginated courses
        Page<CourseDTO> coursePage = courseService.getAllCourses(pageable);

        // Create Pagination metadata
        ApiResponse.Pagination pagination = new ApiResponse.Pagination(
                coursePage.getNumber(),
                coursePage.getSize(),
                coursePage.getTotalElements(),
                coursePage.getTotalPages(),
                coursePage.isLast()
        );

        // Return response
        return ResponseEntity.ok(ApiResponse.success(
                "Courses fetched successfully",
                coursePage.getContent(),
                pagination
        ));
    }

    // Get a course by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDTO>> getCourseById(@PathVariable Long id) {
        CourseDTO course = courseService.getCourseById(id);

        ApiResponse<CourseDTO> response = ApiResponse.success(
                "Get course successfully with id " + id,
                course
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Create a new course
    @PostMapping("/create-course")
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);

        ApiResponse<CourseDTO> response = ApiResponse.success(
                "Courses created successfully",
                createdCourse
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Update an existing course
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
        ApiResponse<CourseDTO> response = ApiResponse.success(
                "Courses updated successfully for course id " + id,
                updatedCourse
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Delete a course
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        ApiResponse<Void> response = ApiResponse.success(
                "Courses deleted successfully for course id " + id,
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
