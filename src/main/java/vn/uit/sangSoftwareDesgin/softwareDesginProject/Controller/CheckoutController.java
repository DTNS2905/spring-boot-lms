package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CheckoutRequest;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CartService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.PurchaseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class CheckoutController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CartService cartService;

    @PostMapping("/stripe/check-out-session")
    public ResponseEntity<?> createCheckoutSessionForAll(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            // Check if the course has already been purchased
            if (purchaseService.hasUserPurchasedCourse(checkoutRequest.getUsername(), checkoutRequest.getCourseId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "You have already purchased this course."));
            }

            // Fetch course details from the cart
            List<Course> cartItems = cartService.getAllCoursesInCart(checkoutRequest.getUsername());
            if (cartItems == null || cartItems.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No courses found in the cart."));
            }

            // Create Stripe session
            SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://yourdomain.com/success")
                    .setCancelUrl("https://yourdomain.com/cancel");

            // Add each course in the cart as a line item
            for (Course course : cartItems) {
                sessionBuilder.addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L) // Assuming each course is bought only once
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(course.getPrice().multiply(BigDecimal.valueOf(100)).longValue()) // Stripe uses cents
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(course.getTitle())
                                                                .build())
                                                .build())
                                .build());
            }

            // Build the session
            SessionCreateParams params = sessionBuilder.build();
            Session session = Session.create(params);

            // Return the session URL as a response
            return ResponseEntity.ok(Map.of("successUrl", session.getSuccessUrl()));

        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create checkout session.", "details", e.getMessage()));
        }
    }
}

