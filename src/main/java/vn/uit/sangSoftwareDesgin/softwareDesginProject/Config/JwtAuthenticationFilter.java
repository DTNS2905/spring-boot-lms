package vn.uit.sangSoftwareDesgin.softwareDesginProject.Config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.UserDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Enums.Role;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Enums.TokenType;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.User;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Security.AuthService;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthService authService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, AuthService authService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                processAuthentication(token, request);
            }
        } catch (Exception e) {
            log.error("JWT Authentication failed: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header.
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Processes the authentication using the provided JWT token.
     */
    private void processAuthentication(String token, HttpServletRequest request) {
        String username = jwtTokenUtil.extractUsername(token);

        if (username != null) {
            UserDetails userDetails = authService.loadUserByUsername(username);

            // Validate access token
            if (jwtTokenUtil.validateToken(token, userDetails, TokenType.ACCESS)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.info("Successfully authenticated user: {}", username);
            } else {
                log.warn("Invalid or expired access token for user: {}", username);
            }
        } else {
            log.warn("Username extraction from token failed.");
        }
    }
}
