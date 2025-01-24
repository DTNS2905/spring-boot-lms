package vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Image;


import java.util.UUID;
@Repository
public interface ImageRepo extends JpaRepository<Image, UUID> {

    

}
