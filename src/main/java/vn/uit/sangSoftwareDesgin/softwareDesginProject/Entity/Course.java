package vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "course_name",nullable = false) // Make courseName mandatory
    private String courseName;

    @Column(nullable = false) // Make title mandatory
    private String title;

    @Column(nullable = false)
    private String type;

    @Lob // For potentially large descriptions
    private String description;

    @Column(name= "begin_date",nullable = false) // Make courseName mandatory
    private LocalDate beginDate; // Start date of the course

    @Column(name= "end_date",nullable = false) // Make courseName mandatory
    private LocalDate endDate;   // End date of the course


    @Column(precision = 10, scale = 2) // Ensure proper storage of price
    private BigDecimal price;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Lesson> lessons = new HashSet<>();

}
