package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CheckoutRequest;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CartService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.PurchaseService;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/create-checkout-session")
    public String createCheckoutSession(@RequestBody CheckoutRequest checkoutRequest) throws Exception {
        // Check if the course has already been purchased
        if (purchaseService.hasUserPurchasedCourse(checkoutRequest.getUserId(), checkoutRequest.getCourseId())) {
            throw new IllegalArgumentException("You have already purchased this course.");
        }

        // Fetch course details from the cart
        var cartItem = cartService.getCartItem(checkoutRequest.getUserId(), checkoutRequest.getCourseId());
        if (cartItem == null) {
            throw new IllegalArgumentException("Course not found in the cart.");
        }

        // Create Stripe session
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://yourdomain.com/success")
                .setCancelUrl("https://yourdomain.com/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(cartItem.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(cartItem.getPrice())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(cartItem.getProductName())
                                                                .build())
                                                .build())
                                .build())
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
