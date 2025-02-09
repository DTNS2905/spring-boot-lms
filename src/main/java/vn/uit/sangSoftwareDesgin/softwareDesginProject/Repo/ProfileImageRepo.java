package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.ProfileImage;

import java.util.List;
import java.util.Optional;

public interface ProfileImageRepo extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findByProfileId(Long profileId);

    @Query("SELECT pi FROM ProfileImage pi WHERE pi.profile.id = :profileId")
    List<ProfileImage> findManyByProfileId(@Param("profileId") Long profileId);

    Page<ProfileImage> findByProfileId(Long profileId, Pageable pageable);
}
