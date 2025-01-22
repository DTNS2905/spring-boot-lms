package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CheckoutRequest {

    public enum Currency {
        VND, EUR, USD;
    }
    private String username;
    private List<Long> courseIds;
    private String description; // Description of the payment
    private Currency currency;  // Currency for the payment (USD, EUR)
}
