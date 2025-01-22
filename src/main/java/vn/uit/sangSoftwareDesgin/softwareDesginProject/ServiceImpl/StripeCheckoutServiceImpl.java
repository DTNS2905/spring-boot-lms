package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.StripeCheckoutService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StripeCheckoutServiceImpl implements StripeCheckoutService {

    @Override
    public String createCheckoutSession(List<Course> cartItems, String currency) throws StripeException {
        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel");

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
                            .build());
        }

        // Create and return the session
        SessionCreateParams params = sessionBuilder.build();
        Session session = Session.create(params);
        // Return the checkout session URL
        return "https://checkout.stripe.com/pay/" + session.getId();
    }
}
