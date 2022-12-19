package nl.tudelft.sem.template.authentication.controllers;

import nl.tudelft.sem.template.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.template.authentication.authentication.JwtUserDetailsService;
import nl.tudelft.sem.template.authentication.domain.user.*;
import nl.tudelft.sem.template.authentication.domain.user.FacultyName;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import sem.commons.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainUserController {

    private final transient AuthenticationManager authenticationManager;

    private final transient JwtTokenGenerator jwtTokenGenerator;

    private final transient JwtUserDetailsService jwtUserDetailsService;

    private final transient RegistrationService registrationService;

    /**
     * Instantiates a new Main user controller.
     *
     * @param authenticationManager the authentication manager
     * @param jwtTokenGenerator     the jwt token generator
     * @param jwtUserDetailsService the jwt user details service
     * @param registrationService   the registration service
     */
    public MainUserController(
            AuthenticationManager authenticationManager,
            JwtTokenGenerator jwtTokenGenerator,
            JwtUserDetailsService jwtUserDetailsService,
            RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.registrationService = registrationService;
    }

    /**
     * Add new user status dto.
     *
     * @param userDTO the user dto
     * @return the status dto
     */
    @KafkaListener(
            topics = "add-user-topic",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactoryUserDTO"
    )
    @SendTo
    public StatusDTO addNewUser(UserDTO userDTO) {
        try {
            NetId netId = new NetId(userDTO.getNetId());
            Password password = new Password(userDTO.getPassword());
            Role role;
            try {
                role = Role.valueOf(userDTO.getRole());
            } catch (Exception e) {
                return new StatusDTO("Provided role does not exist!");
            }
            List<String> facultiesStrings = userDTO.getFaculties();

            List<FacultyName> faculties = new ArrayList<>();

            for (String name : facultiesStrings) {
                try {
                    faculties.add(FacultyName.valueOf(name));
                } catch (Exception e) {
                    return new StatusDTO("Wrong faculty name");
                }
            }

            registrationService.registerUser(netId, password, role, faculties);
        } catch (Exception e) {
            return new StatusDTO(e.getMessage());
        }

        return new StatusDTO("OK");
    }

    /**
     * Authenticate user token dto.
     *
     * @param userCredentials the user credentials
     * @return the token dto
     */
    @KafkaListener(
            topics = "user-auth-topic",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactoryUserCredentials"
    )
    @SendTo
    public TokenDTO authenticateUser(UserCredentials userCredentials) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userCredentials.getNetId(),
                            userCredentials.getPassword()));
        } catch (DisabledException e) {
            return new TokenDTO("USER_DISABLED", "");
        } catch (BadCredentialsException e) {
            return new TokenDTO("INVALID_CREDENTIALS", "");
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userCredentials.getNetId());
        final String jwtToken = jwtTokenGenerator.generateToken(userDetails);
        return new TokenDTO("OK", jwtToken);
    }
}
