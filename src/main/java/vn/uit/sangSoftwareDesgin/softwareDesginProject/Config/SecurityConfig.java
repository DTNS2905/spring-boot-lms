package vn.uit.sangSoftwareDesgin.softwareDesginProject.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Security.RoleRules.RoleBasedAccessRules;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Security.AuthService;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class SecurityConfig {
    public static final String[] WHITE_LIST_URL = {
            "api/auth/**",
            "api/auth/refresh"

    };

    private final RoleBasedAccessRules roleBasedAccessRules;
    private final JwtAuthenticationFilter authFilter;
    private final AuthService authService;


    // Constructor Injection for Dependencies
    public SecurityConfig(RoleBasedAccessRules roleBasedAccessRules, JwtAuthenticationFilter authFilter, AuthService authService) {
        this.roleBasedAccessRules = roleBasedAccessRules;
        this.authFilter = authFilter;
        this.authService = authService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return authService; // AuthService implements UserDetailsService
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection entirely
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(WHITE_LIST_URL).permitAll();
                    roleBasedAccessRules.configure(auth); // Apply role-based rules
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
