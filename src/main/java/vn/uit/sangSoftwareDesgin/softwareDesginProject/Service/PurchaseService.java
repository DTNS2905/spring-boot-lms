package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import java.util.List;
import java.util.Map;

public interface PurchaseService {

    Map<Long, Boolean> hasUserPurchasedCourses(String username, List<Long> courseId);
    List<Long> getAlreadyPurchasedCourses(String username, List<Long> courseIds);
    void moveItemsToHistory(List<Long> courseIds);

}
