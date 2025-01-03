package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u where u.id = :userId")
    Optional<User> findUserByUserId(@Param("userId") Long userId);

    @Query("SELECT u.id FROM User u where u.username = :username")
    Long findUserIdByUsername(@Param("username") String username);

    // Custom query to find a User by their associated Profile ID
    @Query("SELECT u.profile.id FROM User u WHERE u.username = :username")
    Optional<Profile> findProfileByUsername(@Param("username") String username);


}
