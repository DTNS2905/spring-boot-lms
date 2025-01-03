package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;

import java.util.Date;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private Date createdDate;
    private Profile profile;
    private Set<Long> courses; // Only course IDs for simplicity

}

