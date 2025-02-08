package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.UserCourseImage;

import java.util.Optional;

public interface UserCourseImageRepo extends JpaRepository<UserCourseImage, Long> {
    Optional<UserCourseImage> findByCourseId(Long courseId);

    Page<UserCourseImage> findByCourseId(Long courseId, Pageable pageable);

}
