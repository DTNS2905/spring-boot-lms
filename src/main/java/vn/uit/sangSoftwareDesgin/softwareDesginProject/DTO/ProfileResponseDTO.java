package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    private String address;

    private String description;
}
