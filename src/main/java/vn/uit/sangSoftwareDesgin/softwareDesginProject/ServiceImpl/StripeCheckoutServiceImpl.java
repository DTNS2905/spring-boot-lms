package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.StripeCheckoutService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StripeCheckoutServiceImpl implements StripeCheckoutService {
    @Value("${frontendUrl}")
    private String frontendUrl;

    @Override
    public String createCheckoutSession(List<Course> cartItems, String currency, String Username) throws StripeException {
        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/home")
                .setCancelUrl(frontendUrl + "/checkout")
                .putMetadata("Username", Username);

        // Add each course as a line item
        for (Course course : cartItems) {
            sessionBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L) // Each course is bought only once
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency(currency)
                                            .setUnitAmount(course.getPrice().multiply(BigDecimal.valueOf(100)).longValue()) // Stripe uses cents
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(course.getTitle())
                                                            .build())
                                            .build())
                            .build()
            );

            // Add metadata for this course
            sessionBuilder.putMetadata("course_id_" + course.getId(), String.valueOf(course.getId()));
            sessionBuilder.putMetadata("course_title_" + course.getId(), course.getTitle());

        }

        // Create and return the session
        SessionCreateParams params = sessionBuilder.build();
        System.out.println("SessionCreateParams: " + params); // Check what's being added
        Session session = Session.create(params);
        // Return the checkout session URL
        return session.getUrl();
    }
}
