package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ProfileCreateDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ProfileUpdateDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;


public interface ProfileService {

    Profile getProfile(String name);

    Profile createProfile(String username,ProfileCreateDTO profileCreateDTO);

    Profile updateProfile(String username, ProfileUpdateDTO profileUpdateDTO);

}
