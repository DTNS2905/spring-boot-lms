package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.stereotype.Repository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {

}
