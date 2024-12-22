package vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "carts_courses")
@NoArgsConstructor
@AllArgsConstructor
public class CartCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;


    // Getters and setters
}