package vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Enums.Role;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Entity
@Table(name = "history_purchases")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "checkout_at", nullable = false)
    private Date checkoutAt;


}

