package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.HistoryPurchase;

public interface HistoryPurchaseRepo extends JpaRepository<HistoryPurchase, Long> {

    /**
     * Checks if a record exists where a user has purchased a specific course.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course.
     * @return true if the user has purchased the course, false otherwise.
     */
    boolean existsByUserUsernameAndCourseId(String username, Long courseId);

}
