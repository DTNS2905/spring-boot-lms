package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class CourseDTO {

    private String courseName;
    private String title;
    private String description;
    private LocalDate beginDate;
    private LocalDate endDate;
    private BigDecimal price;
}
