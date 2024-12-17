package vn.uit.sangSoftwareDesgin.softwareDesginProject.repo;

import vn.uit.sangSoftwareDesgin.softwareDesginProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

}
