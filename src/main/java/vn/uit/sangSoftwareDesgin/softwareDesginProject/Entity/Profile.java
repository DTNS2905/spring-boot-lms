package vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstName",nullable = false)
    @Pattern(
            regexp = "^[a-zA-Z0-9_]{3,20}$",
            message = "Invalid username format"
    )
    private String firstName;
    @Column(name = "lastName",nullable = false)
    @Pattern(
            regexp = "^[a-zA-Z0-9_]{3,20}$",
            message = "Invalid username format"
    )
    private String lastName;

    @Column(name = "phone")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Invalid mobile number"
    )
    private String phone;
    private String address;
    private String description;

    @OneToOne(mappedBy = "profile")
    private User user;


}
