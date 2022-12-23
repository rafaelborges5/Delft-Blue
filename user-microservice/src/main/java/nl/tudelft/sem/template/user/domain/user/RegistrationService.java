package nl.tudelft.sem.template.user.domain.user;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A DDD service for registering a new user.
 */
@Service
public class RegistrationService {
    private final transient UserRepository userRepository;
    private final transient PasswordHashingService passwordHashingService;

    /**
     * Instantiates a new UserService.
     *
     * @param userRepository  the user repository
     * @param passwordHashingService the password encoder
     */
    public RegistrationService(UserRepository userRepository,
                               PasswordHashingService passwordHashingService) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
    }

    /**
     * Register a new user.
     *
     * @param netId    The NetID of the user
     * @param password The password of the user
     * @param role     The role of the user
     * @param faculty  The faculty of the user
     * @throws Exception if the user already exists
     */
    public AppUser registerUser(NetId netId, Password password, Role role, List<FacultyName> faculty)
            throws NetIdAlreadyInUseException {

        if (checkNetIdIsUnique(netId)) {
            // Hash password
            HashedPassword hashedPassword = passwordHashingService.hash(password);

            // Create new account
            AppUser user = new AppUser(netId, hashedPassword, role, faculty);
            userRepository.save(user);

            return user;
        }

        throw new NetIdAlreadyInUseException(netId);
    }

    public boolean checkNetIdIsUnique(NetId netId) {
        return !userRepository.existsByNetId(netId);
    }
}
