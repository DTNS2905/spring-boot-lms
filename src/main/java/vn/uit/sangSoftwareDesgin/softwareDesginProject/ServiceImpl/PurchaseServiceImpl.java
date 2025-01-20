package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.HistoryPurchaseRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.PurchaseService;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private HistoryPurchaseRepo historyPurchaseRepository;

    /**
     * Checks if the user has already purchased a specific course.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course.
     * @return true if the course has been purchased by the user, false otherwise.
     */
    @Override
    public boolean hasUserPurchasedCourse(String username, String courseId) {
        return historyPurchaseRepository.existsByUserUsernameAndCourseId(username, Long.parseLong(courseId));
    }

}
