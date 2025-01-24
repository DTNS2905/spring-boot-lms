package vn.uit.sangSoftwareDesgin.softwareDesginProject.Config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @PostConstruct
    public void initStripe() {
        // Set Stripe API Key
        Stripe.apiKey = secretKey;
    }
}
