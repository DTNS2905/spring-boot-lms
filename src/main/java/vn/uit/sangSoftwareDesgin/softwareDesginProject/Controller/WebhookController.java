package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CartService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.PurchaseService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/webhook")
public class WebhookController {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CartService cartService;

    @PostMapping("/pay-success")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, HttpServletRequest request) {
        try {
            // Verify and construct the Stripe event
            Event event = Webhook.constructEvent(
                    payload,
                    request.getHeader("Stripe-Signature"),
                    webhookSecret
            );

            // Handle specific event types
            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow(() -> new RuntimeException("Failed to deserialize PaymentIntent"));

                // Extract metadata
                Map<String, String> metadata = paymentIntent.getMetadata();

                String username = metadata.get("username");
                if (username == null) {
                    throw new RuntimeException("User ID is missing in metadata");
                }

                // Extract course IDs from metadata
                List<Long> paidCourseIds = metadata.entrySet().stream()
                        .filter(entry -> entry.getKey().startsWith("course_id_")) // Filter keys starting with "course_id_"
                        .map(entry -> Long.valueOf(entry.getValue())) // Get course IDs
                        .toList();

                if (!paidCourseIds.isEmpty()) {
                    throw new RuntimeException("Course IDs are missing in metadata");
                }
                // Handle successful payment
                handleSuccessfulPayment(username,paidCourseIds);
            }

            return ResponseEntity.ok("Webhook handled successfully");
        } catch (SignatureVerificationException e) {
            // Invalid signature
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature: " + e.getMessage());
        } catch (Exception e) {
            // General error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook error: " + e.getMessage());
        }
    }

    private void handleSuccessfulPayment(String username, List<Long> paidCourseIds) {
        // Get the list of paid course IDs from the customer's cart (logic depends on your payment flow)
        purchaseService.moveItemsToHistory(paidCourseIds);
        // Remove the purchased items from the cart
        cartService.clearCart(username, paidCourseIds);
    }
}
