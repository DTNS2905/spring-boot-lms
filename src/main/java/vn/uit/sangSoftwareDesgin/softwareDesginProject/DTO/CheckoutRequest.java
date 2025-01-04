package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class CheckoutRequest {

    public enum Currency {
        EUR, USD;
    }
    private
    private String description; // Description of the payment
    private Long amount;         // Amount in the smallest currency unit (e.g., cents for USD)
    private Currency currency;  // Currency for the payment (USD, EUR)
    private String successUrl;  // URL to redirect upon success
    private String cancelUrl;   // URL to redirect upon cancellation
}
