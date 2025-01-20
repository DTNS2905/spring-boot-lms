package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CourseItemDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.PaginationResponse.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CartService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getCartSummary(@RequestParam String username) {
        Map<String, Object> cartSummary = cartService.getCartSummary(username);
        return ResponseEntity.ok(cartSummary);
    }

    @PostMapping("{userName}/add")
    public ResponseEntity<ApiResponse<List<Course>>> addCoursesToCart(
            @PathVariable String userName,
            @RequestBody CourseItemDTO courseItemDTO
            ) {

        List<Course> addedCourses = cartService.addCoursesToCart(userName, courseItemDTO);

        ApiResponse<List<Course>> response = ApiResponse.success(
                "Courses added to user cart successfully",
                addedCourses
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("{username}/remove")
    public ResponseEntity<ApiResponse<List<Course>>> removeCoursesFromCart(
            @PathVariable String username,
            @RequestBody CourseItemDTO courseItemDTO
    ) {
        List<Course> removedCourses = cartService.removeCoursesFromCart(username, courseItemDTO);

        ApiResponse<List<Course>> response = ApiResponse.success(
                "Courses added to user cart successfully",
                removedCourses
        );
        return ResponseEntity.ok(response);
    }

}

