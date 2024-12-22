package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ProfileCreateDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ProfileUpdateDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.User;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.ProfileRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.UserRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.ProfileService;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepo profileRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ModelMapper modelMapper;


    public Profile createProfile(Long userId, ProfileCreateDTO profileCreateDTO) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }

        User user = userOptional.get();

        // Check if the user already has a profile
        if (user.getProfile() != null) {
            throw new IllegalArgumentException("User already has a profile. Update the existing profile instead.");
        }

        // Map DTO to Profile entity
        Profile profile = modelMapper.map(profileCreateDTO, Profile.class);
        profile.setUser(user); // Associate profile with the user

        return profileRepository.save(profile);
    }

    public Profile updateProfile(Long id, ProfileUpdateDTO profileUpdateDTO) {
        Optional<Profile> existingProfile = profileRepository.findById(id);
        if (existingProfile.isEmpty()) {
            throw new IllegalArgumentException("Profile not found. Create a profile first.");
        }

        Profile profile = existingProfile.get();
        modelMapper.map(profileUpdateDTO, profile); // Map updated fields to existing entity

        return profileRepository.save(profile);
    }

//    public Profile
}
