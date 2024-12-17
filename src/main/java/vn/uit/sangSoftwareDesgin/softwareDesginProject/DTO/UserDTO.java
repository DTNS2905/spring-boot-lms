package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class UserDTO {
    // Getters and Setters
    private Long id;

    private String fullname;
    private String email;
    private String description;

    private Set<Long> courseIds; // Only course IDs for simplicity

}

