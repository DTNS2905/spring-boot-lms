package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Enums.TokenType;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.ResponseInstance.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.AuthService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Util.JwtTokenUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>>
    registerUser(@Valid @RequestBody RegisterRequestDTO userInfo) {
        try {
            // Call the service to add the user
            RegisterRequestDTO savedUser = service.addUser(userInfo);

            // Prepare the response DTO
            RegisterResponseDTO responseDTO = new RegisterResponseDTO(savedUser);

            // Wrap in ApiResponse
            ApiResponse<RegisterResponseDTO> response = new ApiResponse<>(
                    "success",
                    "User registered successfully",
                    responseDTO
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            // Handle user already exists
            ApiResponse<RegisterResponseDTO> response = new ApiResponse<>(
                    "error",
                    "User already exists",
                    null
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception e) {
            // Handle unexpected errors
            ApiResponse<RegisterResponseDTO> response = new ApiResponse<>(
                    "error",
                    "An unexpected error occurred",
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//    @GetMapping("/user/userProfile")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public String userProfile() {
//        return "Welcome to User Profile";
//    }
//
//    @GetMapping("/admin/adminProfile")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public String adminProfile() {
//        return "Welcome to Admin Profile";
//    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>>
    authenticateAndGetToken(@RequestBody LoginRequestDTO authRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            // If authenticated, generate JWT token
            if (authentication.isAuthenticated()) {
                String accessToken = jwtTokenUtil.generateToken(authRequest.getUsername(), TokenType.ACCESS);
                String refreshToken = jwtTokenUtil.generateToken(authRequest.getUsername(), TokenType.REFRESH);


                // Prepare the response DTO
                LoginResponseDTO responseDTO = new LoginResponseDTO(accessToken,refreshToken);

                // Wrap in ApiResponse
                ApiResponse<LoginResponseDTO> response = new ApiResponse<>(
                        "success",
                        "Login successful",
                        responseDTO
                );

                return ResponseEntity.ok(response);
            } else {
                throw new UsernameNotFoundException("Invalid user request!");
            }

        } catch (UsernameNotFoundException e) {
            // Handle invalid user requests
            ApiResponse<LoginResponseDTO> response = new ApiResponse<>(
                    "error",
                    e.getMessage(),
                    null
            );
            log.error(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            // Handle unexpected errors
            ApiResponse<LoginResponseDTO> response = new ApiResponse<>(
                    "error",
                    "An unexpected error occurred",
                    null
            );
            log.error("An unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshResponseDTO>> refreshAccessToken(@RequestBody RefreshRequestDTO refreshTokenDTO) {

        try {
            // Extract the refresh token value
            String refreshToken = refreshTokenDTO.getRefreshToken();

            // Validate the refresh token
            if (!jwtTokenUtil.validateToken(refreshToken, null, TokenType.REFRESH)) {
                ApiResponse<RefreshResponseDTO> response = new ApiResponse<>(
                        "error",
                        "Invalid refresh token",
                        null
                );
                log.error("Invalid refresh token received");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Extract the username from the refresh token
            String username = jwtTokenUtil.extractUsername(refreshToken);

            // Load user details
            UserDetails userDetails = service.loadUserByUsername(username);

            // Generate a new access token
            String newAccessToken = jwtTokenUtil.generateToken(userDetails.getUsername(), TokenType.ACCESS);

            // Create the response DTO
            RefreshResponseDTO responseDTO = new RefreshResponseDTO(newAccessToken);

            // Return the new access token
            ApiResponse<RefreshResponseDTO> responseSuccess = new ApiResponse<>(
                    "success",
                    "Access token refreshed successfully",
                    responseDTO
            );
            return ResponseEntity.ok(responseSuccess);

        } catch (Exception e) {
            // Handle unexpected errors
            ApiResponse<RefreshResponseDTO> response = new ApiResponse<>(
                    "error",
                    "An unexpected error occurred",
                    null
            );
            log.error("Error while refreshing access token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
