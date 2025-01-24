package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CheckoutRequest;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CartService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.PurchaseService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.StripeCheckoutService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class CheckoutController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CartService cartService;

    @Autowired
    private StripeCheckoutService stripeCheckoutService;

    /**
     * Create a Stripe checkout session for all courses in the cart.
     */
    @PostMapping("/stripe/{userName}/check-out-session")
    public ResponseEntity<?> createCheckoutSession(
            @RequestBody CheckoutRequest checkoutRequest,
            @PathVariable String userName
    ) {
        try {
            // Step 1: Check if courses have already been purchased
            List<Long> alreadyPurchasedCourses = purchaseService.getAlreadyPurchasedCourses(
                    userName  ,
                    checkoutRequest.getCourseIds()
            );

            if (!alreadyPurchasedCourses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "Some courses have already been purchased.",
                                "purchasedCourses", alreadyPurchasedCourses
                        ));
            }

            // Step 2: Fetch courses from the cart after filtering
            List<Course> cartItems = cartService.getCoursesByIdsInCart(
                    userName,
                    checkoutRequest.getCourseIds()
            );
            if (cartItems == null || cartItems.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No courses found in the cart."));
            }

            // Step 3: Create Stripe checkout session
            String sessionUrl = stripeCheckoutService.createCheckoutSession(
                    cartItems,
                    String.valueOf(checkoutRequest.getCurrency()),
                    userName
            );
            return ResponseEntity.ok(Map.of("checkoutUrl", sessionUrl));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create checkout session.", "details", e.getMessage()));
        }
    }
}

