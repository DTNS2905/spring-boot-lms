package vn.uit.sangSoftwareDesgin.softwareDesginProject.Controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Enums.TokenType;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.PaginationResponse.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Security.AuthService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Util.JwtTokenUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;



    @PostMapping("/register")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> registerUser(
            @Valid @RequestBody RegisterRequestDTO userInfo) {
        try {
            log.info("Received registration request for username: {}", userInfo.getUsername());

            // Add the user to the system
            RegisterRequestDTO savedUser = service.addUser(userInfo);
            if (savedUser == null) {
                log.warn("User creation failed: savedUser is null.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        ApiResponse.error("Failed to create user. Please try again later.")
                );
            }
            log.info("User created successfully with username: {}", savedUser.getUsername());

            // Authenticate the newly registered user
            Authentication authentication = service.authenticateUser(userInfo.getUsername(), userInfo.getPassword());
            if (!authentication.isAuthenticated()) {
                log.error("Authentication failed for username: {}", userInfo.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.error("Authentication failed. Please try again later.")
                );
            }

            // Generate access and refresh tokens
            String accessToken = jwtTokenUtil.generateToken(userInfo.getUsername(), TokenType.ACCESS);
            String refreshToken = jwtTokenUtil.generateToken(userInfo.getUsername(), TokenType.REFRESH);

            // Prepare response
            TokenResponseDTO tokenResponse = new TokenResponseDTO(accessToken, refreshToken);
            ApiResponse<TokenResponseDTO> response = ApiResponse.success(
                    "User registered successfully.", tokenResponse
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("User registration error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponse.error(e.getMessage())
            );

        } catch (Exception e) {
            log.error("Unexpected error during user registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("An unexpected error occurred. Please try again later.")
            );
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDTO>>
    authenticateAndGetToken(@RequestBody LoginRequestDTO authRequest) {
        try {
            Authentication authentication = service.authenticateUser(authRequest.getUsername(), authRequest.getPassword());

            // If authenticated, generate JWT token
            if (authentication.isAuthenticated()) {
                String accessToken = jwtTokenUtil.generateToken(authRequest.getUsername(), TokenType.ACCESS);
                String refreshToken = jwtTokenUtil.generateToken(authRequest.getUsername(), TokenType.REFRESH);


                // Prepare the response DTO
                TokenResponseDTO responseDTO = new TokenResponseDTO(accessToken,refreshToken);

                // Wrap in ApiResponse
                ApiResponse<TokenResponseDTO> response = ApiResponse.success(
                        "Login successful",
                        responseDTO
                );

                return ResponseEntity.ok(response);
            } else {
                throw new UsernameNotFoundException("Invalid user request!");
            }

        } catch (UsernameNotFoundException e) {
            // Handle invalid user requests
            ApiResponse<TokenResponseDTO> response = ApiResponse.error(
                    e.getMessage()
            );
            log.error(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            // Handle unexpected errors
            ApiResponse<TokenResponseDTO> response = ApiResponse.error(
                    "An unexpected error occurred"
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
                ApiResponse<RefreshResponseDTO> response = ApiResponse.error(
                        "Invalid refresh token"
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
            ApiResponse<RefreshResponseDTO> responseSuccess = ApiResponse.success(
                    "Access token refreshed successfully", responseDTO
            );
            return ResponseEntity.ok(responseSuccess);

        } catch (Exception e) {
            // Handle unexpected errors
            ApiResponse<RefreshResponseDTO> response = ApiResponse.error(
                    "An unexpected error occurred"
            );
            log.error("Error while refreshing access token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
