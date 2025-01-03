package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.ProfileUpdateDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Profile;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.User;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.ProfileRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.UserRepo;

@Service
public class ProfileTransactionService {

    private final UserRepo userRepository;

    private final ProfileRepo profileRepository;

    public ProfileTransactionService(UserRepo userRepository, ProfileRepo profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public Profile saveProfile(User userInfo, Profile profile) {

        // Save the Profile entity
        Profile savedProfile = profileRepository.save(profile);

        // Associate the saved Profile with the User
        userInfo.setProfile(savedProfile);

        // Persist the updated User entity
        userRepository.save(userInfo);

        return savedProfile;
    }
}
