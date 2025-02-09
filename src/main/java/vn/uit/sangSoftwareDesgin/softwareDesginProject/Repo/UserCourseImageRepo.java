package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.ProfileImage;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.UserCourseImage;

import java.util.List;
import java.util.Optional;

public interface UserCourseImageRepo extends JpaRepository<UserCourseImage, Long> {
    Optional<UserCourseImage> findByCourseId(Long courseId);

    @Query("SELECT uci FROM UserCourseImage uci WHERE uci.course.id = :courseId")
    List<UserCourseImage> findManyByCourseId(@Param("courseId") Long courseId);

    Page<UserCourseImage> findByCourseId(Long courseId, Pageable pageable);

}
