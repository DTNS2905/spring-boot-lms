package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private Date createdDate;
    private Profile profile;
    private Set<Long> courses; // Only course IDs for simplicity
    private Set<Long> profiles;
}

