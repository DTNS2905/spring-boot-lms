package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);
    @Autowired
    private ProfileRepo profileRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProfileTransactionService profileTransactionService;


    @Override
    public Profile getProfile(String username) {
        try{
            Optional<User> foundUser = userRepository.findByUsername(username);
            if (foundUser.isEmpty()) {
                throw new IllegalArgumentException("Profile Not Found.");
            }

            User user = foundUser.get();
            Profile profile = user.getProfile();

            if (profile != null) {
                return modelMapper.map(profile, Profile.class);
            }

            return null;
        } catch(Exception e) {
            log.error("error while fetching profile {}",e.getMessage(), e);
            return null;
        }

    }

    @Override
    public Profile createProfile(String username, ProfileCreateDTO profileCreateDTO) {
        // Check if user exists
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        // Check if the user already has a profile
        if (user.getProfile() != null) {
            throw new IllegalArgumentException("Profile already exists for the user.");
        }

        // Map DTO to Profile entity
        Profile profile = modelMapper.map(profileCreateDTO, Profile.class);

        // Save profile using transaction service
        profileTransactionService.saveProfile(user, profile);

        return profile;
    }

    @Override
    public Profile updateProfile(String username, ProfileUpdateDTO profileUpdateDTO) {
        try {
            // Fetch user by username
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

            // Ensure the user has an existing profile
            if (user.getProfile() == null) {
                throw new IllegalArgumentException("Profile not found. Create a profile first.");
            }

            // Get the existing profile
            Profile updateProfile = modelMapper.map(profileUpdateDTO, Profile.class);

            // Save and return the updated profile
            return profileTransactionService.saveProfile(user, updateProfile);

        } catch (Exception e) {
            log.error("Error while updating profile {}", e.getMessage(), e);
            throw e; // Propagate the exception for handling upstream (optional)
        }
    }
}
