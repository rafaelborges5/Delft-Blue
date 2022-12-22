package nl.tudelft.sem.template.gateway.authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Authentication Manager.
 */
@Component
public class AuthManager {
    /**
     * Interfaces with spring security to get the name of the user in the current context.
     *
     * @return The name of the user.
     */
    public String getNetId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getFaculties() {
        return ((SimpleGrantedAuthority)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[1]).getAuthority();
    }
}
