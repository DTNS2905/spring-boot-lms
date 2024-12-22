package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileCreateDTO {

        @NotNull(message = "First name is required")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]{3,20}$",
                message = "Invalid format for first name"
        )
        private String firstName;

        @NotNull(message = "Last name is required")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]{3,20}$",
                message = "Invalid format for last name"
        )
        private String lastName;

        @Pattern(
                regexp = "^[0-9]{10}$",
                message = "Invalid mobile number"
        )
        private String phone;

        @Size(max = 255, message = "Address can be up to 255 characters")
        private String address;

        @Size(max = 500, message = "Description can be up to 500 characters")
        private String description;
}

