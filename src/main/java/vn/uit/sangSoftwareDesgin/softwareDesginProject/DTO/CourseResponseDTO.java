package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CourseResponseDTO {
    private Long id;
    private String courseName;
    private String type;
    private String title;
    private String description;
    private LocalDate beginDate;
    private LocalDate endDate;
    private BigDecimal price;
    private List<String> imageUrls;
}
