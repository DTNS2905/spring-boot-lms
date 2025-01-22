package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.HistoryPurchase;

import java.util.List;

public interface HistoryPurchaseRepo extends JpaRepository<HistoryPurchase, Long> {

    boolean existsByUserUsernameAndCourseId(String username, Long courseId);

    List<HistoryPurchase> findByUserUsernameAndCourseIdIn(String username, List<Long> courseIds);
}
