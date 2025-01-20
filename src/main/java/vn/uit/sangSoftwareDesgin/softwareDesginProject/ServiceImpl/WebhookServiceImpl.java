package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.WebhookService;

public class WebhookServiceImpl implements WebhookService {

    public void handleCheckoutSessionCompleted(Event event) {
        // Extract session details
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

        if (session != null) {
            // Logic to handle successful checkout
            String userId = session.getMetadata().get("userId");
            String courseIds = session.getMetadata().get("courseIds");
            System.out.println("Checkout session completed for user: " + userId + " with courses: " + courseIds);
        }
    }

    public void handlePaymentIntentSucceeded(Event event) {
        // Logic to handle successful payment intent
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        System.out.println("Payment succeeded for PaymentIntent: " + paymentIntent.getId());
    }

    public void handlePaymentIntentFailed(Event event) {
        // Logic to handle failed payment intent
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        System.out.println("Payment failed for PaymentIntent: " + paymentIntent.getId());
    }

}

