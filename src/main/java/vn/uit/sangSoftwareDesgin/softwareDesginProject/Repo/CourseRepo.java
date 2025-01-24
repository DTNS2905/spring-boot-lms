package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {

}
