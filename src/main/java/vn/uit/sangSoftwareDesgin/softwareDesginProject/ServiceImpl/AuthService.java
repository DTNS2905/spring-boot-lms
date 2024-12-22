package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.RegisterRequestDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Enums.Role;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.User;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.UserRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Security.UserInfoDetails;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Util.ValidationUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepo.findByUsername(username); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public RegisterRequestDTO addUser(RegisterRequestDTO userInfo) {
        try {
            String username = userInfo.getUsername();
            String password = userInfo.getPassword();
            String email = userInfo.getEmail();
            // Log received user info
            log.info("Adding new user with username: {}", username);
            log.info("Adding new user with password: {}", password);


            if (!ValidationUtils.isValidName(username)) {
                throw new IllegalArgumentException("Username must have string length is between 3 and 20 characters, inclusive and single " +
                        "and quote ('), double quote (\"), semicolon (;) are not allowed");
            }

            // Map DTO to Entity
            User user = modelMapper.map(userInfo, User.class);
            if(!ValidationUtils.isValidPassword(password)) {
                throw new IllegalArgumentException("Password must have 8 to 32 characters long and contain at least one uppercase letter" +
                        ", one lowercase letter, one digit, one special character from the set @$!%*?&");
            }

            if(!ValidationUtils.isValidEmail(email)) {
                throw new IllegalArgumentException("Wrong email format");
            }

            user.setPassword(encoder.encode(user.getPassword()));

            user.setRoles(List.of(Role.PARTICIPANT)); // Default role for new users

            // Save user to the database
            User savedUser = userRepo.save(user);

            // Log successful save
            log.info("User with username {} saved successfully.", savedUser.getUsername());

            // Map back to DTO and return
            return modelMapper.map(savedUser, RegisterRequestDTO.class);
        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving user: {}", e.getMessage());
            throw new IllegalArgumentException("User already exists");
        } catch (Exception e) {
            log.error("Unexpected error while adding user: {}", e.getMessage());
            throw e;
        }
    }

    public Authentication authenticateUser(String username, String password) {
        try {
            log.info("Authenticating user: {}", username);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", username, e);
            throw new IllegalStateException("Authentication failed. Please check your credentials.");
        }
    }

}
