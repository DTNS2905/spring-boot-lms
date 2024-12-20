package vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Setter
@Getter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Make courseName mandatory
    private String courseName;

    @Column(nullable = false) // Make title mandatory
    private String title;

    @Lob // For potentially large descriptions
    private String description;

    private LocalDate beginDate; // Start date of the course
    private LocalDate endDate;   // End date of the course

    @Column(precision = 10, scale = 2) // Ensure proper storage of price
    private BigDecimal price;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    // Parameterized constructor
    public Course(String courseName, String title, String description, LocalDate beginDate, LocalDate endDate, BigDecimal price) {
        this.courseName = courseName;
        this.title = title;
        this.description = description;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.price = price;
    }

    // Helper method to add a user
    public void addUser(User user) {
        this.users.add(user);
        user.getCourses().add(this);
    }

    // Helper method to remove a user
    public void removeUser(User user) {
        this.users.remove(user);
        user.getCourses().remove(this);
    }
}
