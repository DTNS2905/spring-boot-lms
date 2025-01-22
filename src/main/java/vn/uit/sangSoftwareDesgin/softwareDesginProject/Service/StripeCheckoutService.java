package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import com.stripe.exception.StripeException;
import org.springframework.stereotype.Repository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;

import java.util.List;


public interface StripeCheckoutService {
    String createCheckoutSession(List<Course> cartItems, String currency) throws StripeException;

}
