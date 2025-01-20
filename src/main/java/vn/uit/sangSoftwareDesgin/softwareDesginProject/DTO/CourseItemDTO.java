package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;

import java.util.List;

@Getter
@Setter
@Data
public class CourseItemDTO {
    private List<Long> courseID;
}
