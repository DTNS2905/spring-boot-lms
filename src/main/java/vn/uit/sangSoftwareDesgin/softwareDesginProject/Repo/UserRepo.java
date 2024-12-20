package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
