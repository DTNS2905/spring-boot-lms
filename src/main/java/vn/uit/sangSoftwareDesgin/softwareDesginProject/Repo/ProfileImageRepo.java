package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.ProfileImage;

import java.util.Optional;

public interface ProfileImageRepo extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findByProfileId(Long profileId);

    Page<ProfileImage> findByProfileId(Long profileId, Pageable pageable);
}
