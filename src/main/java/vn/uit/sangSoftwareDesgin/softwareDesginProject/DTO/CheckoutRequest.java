package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CheckoutRequest {

    public enum Currency {
        EUR, USD, VND;
    }
    private String username;
    private String courseId;
    private String description; // Description of the payment
    private Currency currency;  // Currency for the payment (USD, EUR)
}
