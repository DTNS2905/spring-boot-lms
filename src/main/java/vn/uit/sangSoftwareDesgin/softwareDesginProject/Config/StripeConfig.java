package vn.uit.sangSoftwareDesgin.softwareDesginProject.Config;

import com.stripe.Stripe;

public class StripeConfig {
    public StripeConfig() {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");
    }
}
