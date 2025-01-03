package vn.uit.sangSoftwareDesgin.softwareDesginProject.Security.RoleRules;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RoleBasedAccessRules {

    public void configure(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth
    ) {
        try {
            auth.requestMatchers("/auth/user/**").hasAuthority("ROLE_PARTICIPANT")
                    .requestMatchers("/auth/lecturer/**").hasAuthority("ROLE_LECTURER");
        } catch (Exception e) {
            log.error("Error while configuring role-based access rules: {}", e.getMessage(), e);
        }
    }
}
