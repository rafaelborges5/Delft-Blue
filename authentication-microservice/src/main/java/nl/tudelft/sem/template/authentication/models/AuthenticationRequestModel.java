package nl.tudelft.sem.template.authentication.models;

import lombok.Data;
import nl.tudelft.sem.template.authentication.domain.user.Faculty;
import nl.tudelft.sem.template.authentication.domain.user.Role;

import java.util.List;

/**
 * Model representing an authentication request.
 */
@Data
public class AuthenticationRequestModel {
    private String netId;
    private String password;
    private String role;
    private List<Faculty> faculty;
}