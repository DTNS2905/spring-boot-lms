package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ProfileCreateDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ProfileUpdateDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;

public interface ProfileService {

    Profile createProfile(Long userId,ProfileCreateDTO profileCreateDTO);

    Profile updateProfile(Long id, ProfileUpdateDTO profileUpdateDTO);

}
