package vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "lessons")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    private String image;

    private String link;


    @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss") // Format for incoming requests (e.g., form data)
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    @Column(name = "begin_time", nullable = false)
    @CreationTimestamp
    private LocalDate beginTime;

    @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss") // Format for incoming requests (e.g., form data)
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")    // Format for JSON serialization
    @Column(name = "end_time", nullable = false)
    @CreationTimestamp
    private LocalDate endTime;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

}
