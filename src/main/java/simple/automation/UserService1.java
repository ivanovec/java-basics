package simple.automation;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service layer for user management operations.
 */
public class UserService1 {
    private static final Logger LOGGER = Logger.getLogger(UserService1.class.getName());

    private final UserRepository1 userRepository;
    private final EmailService1 emailService;

    public UserService1(UserRepository1 userRepository, EmailService1 emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public User1 registerUser(String username, String email, String password) {
        LOGGER.info("Registering new user: " + username);

        validateUsername(username);
        validateEmail(email);
        validatePassword(password);

        String passwordHash = hashPassword(password);
        User1 user = new User1(username, email, passwordHash);

        User1 savedUser = userRepository.save(user);

        // Send welcome email
        emailService.sendWelcomeEmail(savedUser);

        LOGGER.info("User1 registered successfully: " + savedUser.getId());
        return savedUser;
    }

    public boolean authenticateUser(String username, String password) {
        Optional<User1> userOpt = userRepository.findByUsername(username);

        if (!userOpt.isPresent()) {
            LOGGER.warning("Authentication failed: user not found - " + username);
            return false;
        }

        User1 user = userOpt.get();

        if (!user.isActive()) {
            LOGGER.warning("Authentication failed: user inactive - " + username);
            return false;
        }

        boolean isValid = user.verifyPassword(password);

        if (isValid) {
            user.login();
            LOGGER.info("User1 authenticated successfully: " + username);
        } else {
            LOGGER.warning("Authentication failed: invalid password - " + username);
        }

        return isValid;
    }

    public void deactivateUser(String userId, String reason) {
        User1 user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User1 not found: " + userId));

        user.deactivate();
        emailService.sendAccountDeactivationEmail(user, reason);

        LOGGER.info("User1 deactivated: " + userId + ", reason: " + reason);
    }

    public void promoteToAdmin(String userId, String promotedBy) {
        User1 user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User1 not found: " + userId));

        user.promoteToAdmin();
        emailService.sendRoleChangeEmail(user, UserRole1.ADMIN);

        LOGGER.info("User1 promoted to admin: " + userId + " by " + promotedBy);
    }

    public List<User1> getActiveUsers() {
        return userRepository.findActiveUsers();
    }

    public List<User1> getUsersByRole(UserRole1 role) {
        return userRepository.findByRole(role);
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Username can only contain letters, numbers, and underscores");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }

    private String hashPassword(String password) {
        // Simplified - in production use BCrypt
        return String.valueOf(password.hashCode());
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
