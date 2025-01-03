package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Cart;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Cart addCourseToCart(Long userId, Long courseId);
    Cart deleteCourseFromCart(Long userId, Long courseId);
    List<Course> viewCart(Long userId);
    Cart checkout(Long userId);
}
