package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.CartRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.CourseRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.HistoryPurchaseRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.UserRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.PurchaseService;

import java.util.*;

@Transactional
@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private HistoryPurchaseRepo historyPurchaseRepository;

    @Autowired
    private CourseRepo courseRepository;

    /**
     * Checks if the user has already purchased any course in the provided list.
     *
     * @param username The username of the user.
     * @param courseIds The list of course IDs to check.
     * @return A map of course IDs and a boolean indicating if each course has been purchased.
     */
    @Override
    public Map<Long, Boolean> hasUserPurchasedCourses(String username, List<Long> courseIds) {
        // Fetch all purchases for the user and course IDs
        List<HistoryPurchase> purchases = historyPurchaseRepository.findByUserUsernameAndCourseIdIn(username, courseIds);

        // Create a result map to track purchase status for each course
        Map<Long, Boolean> result = new HashMap<>();
        for (Long courseId : courseIds) {
            // If the course exists in the purchase list, mark it as purchased
            result.put(courseId, purchases.stream().anyMatch(purchase -> purchase.getCourse().getId().equals(courseId)));
        }

        return result;
    }

    @Override
    public List<Long> getAlreadyPurchasedCourses(String username, List<Long> courseIds) {
        List<HistoryPurchase> purchases = historyPurchaseRepository.findByUserUsernameAndCourseIdIn(username, courseIds);

        // Extract already purchased course IDs
        return purchases.stream()
                .map(purchase -> purchase.getCourse().getId())
                .distinct()
                .toList();
    }


    @Transactional
    @Override
    public void moveItemsToHistory(List<Long> courseIds) {
        // Loop through each course ID and move it to the HistoryPurchase table
        for (Long courseId : courseIds) {
            // Fetch the course from the database
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

            // Create and save the HistoryPurchase object
            HistoryPurchase historyPurchase = new HistoryPurchase();
            historyPurchase.setCourse(course);
            historyPurchase.setCheckoutAt(new Date());
            historyPurchaseRepository.save(historyPurchase);
        }
    }



}
