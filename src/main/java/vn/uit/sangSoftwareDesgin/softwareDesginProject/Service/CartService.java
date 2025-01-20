package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CourseItemDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Cart;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.CartCourse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;

import java.util.List;
import java.util.Map;

public interface CartService {
    Map<String, Object> getCartSummary(String username);
    CartCourse getCartItem(String username, String courseId);
    List<Course> getAllCoursesInCart(String username);
    List<Course> addCoursesToCart(String username, CourseItemDTO courseItemDTO);
    List<Course> removeCoursesFromCart(String username, CourseItemDTO courseItemDTO);

}
