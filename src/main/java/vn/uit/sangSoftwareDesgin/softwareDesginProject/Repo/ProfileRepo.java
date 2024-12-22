package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;

import java.util.Optional;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {
//    Optional<Profile> findById(long id);
}
