package simple.automation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Manages user sessions and authentication tokens.
 */
public class AuthenticationManager1 {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationManager1.class.getName());
    private static final int SESSION_TIMEOUT_MINUTES = 30;
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 15;

    private final Map<String, Session> activeSessions;
    private final Map<String, LoginAttempts> loginAttempts;
    private final UserRepository1 userRepository;

    public AuthenticationManager1(UserRepository1 userRepository) {
        this.userRepository = userRepository;
        this.activeSessions = new ConcurrentHashMap<>();
        this.loginAttempts = new ConcurrentHashMap<>();
    }

    /**
     * Attempts to log in a user and create a session.
     */
    public Session login(String username, String password) {
        // Check if account is locked
        LoginAttempts attempts = loginAttempts.get(username);
        if (attempts != null && attempts.isLocked()) {
            LOGGER.warning("Login attempt for locked account: " + username);
            throw new SecurityException("Account is temporarily locked due to too many failed attempts");
        }

        // Find user
        User1 user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    recordFailedAttempt(username);
                    return new IllegalArgumentException("Invalid username or password");
                });

        // Verify password
        if (!user.verifyPassword(password)) {
            recordFailedAttempt(username);
            LOGGER.warning("Failed login attempt for user: " + username);
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Check if user is active
        if (!user.isActive()) {
            throw new SecurityException("User1 account is not active");
        }

        // Clear failed attempts and create session
        loginAttempts.remove(username);
        user.login();

        Session session = createSession(user);
        LOGGER.info("User1 logged in successfully: " + username);

        return session;
    }

    /**
     * Logs out a user by invalidating their session.
     */
    public void logout(String sessionToken) {
        Session session = activeSessions.remove(sessionToken);
        if (session != null) {
            LOGGER.info("User1 logged out: " + session.getUser().getUsername());
        }
    }

    /**
     * Validates a session token and returns the associated user.
     */
    public User1 validateSession(String sessionToken) {
        Session session = activeSessions.get(sessionToken);

        if (session == null) {
            throw new SecurityException("Invalid or expired session");
        }

        if (session.isExpired()) {
            activeSessions.remove(sessionToken);
            throw new SecurityException("Session has expired");
        }

        // Refresh session
        session.refresh();
        return session.getUser();
    }

    /**
     * Checks if a user has a specific permission based on their role.
     */
    public boolean hasPermission(String sessionToken, UserRole1.Permission permission) {
        User1 user = validateSession(sessionToken);
        return user.getRole().hasPermission(permission);
    }

    /**
     * Gets all active sessions count.
     */
    public int getActiveSessionsCount() {
        cleanupExpiredSessions();
        return activeSessions.size();
    }

    /**
     * Invalidates all sessions for a specific user.
     */
    public void invalidateUserSessions(String userId) {
        activeSessions.entrySet().removeIf(entry ->
                entry.getValue().getUser().getId().equals(userId));
        LOGGER.info("All sessions invalidated for user: " + userId);
    }

    /**
     * Cleans up expired sessions.
     */
    public void cleanupExpiredSessions() {
        int removed = 0;
        for (Map.Entry<String, Session> entry : activeSessions.entrySet()) {
            if (entry.getValue().isExpired()) {
                activeSessions.remove(entry.getKey());
                removed++;
            }
        }
        if (removed > 0) {
            LOGGER.info("Cleaned up " + removed + " expired sessions");
        }
    }

    private Session createSession(User1 user) {
        String token = UUID.randomUUID().toString();
        Session session = new Session(token, user, SESSION_TIMEOUT_MINUTES);
        activeSessions.put(token, session);
        return session;
    }

    private void recordFailedAttempt(String username) {
        LoginAttempts attempts = loginAttempts.computeIfAbsent(
                username,
                k -> new LoginAttempts(MAX_LOGIN_ATTEMPTS, LOCKOUT_DURATION_MINUTES)
        );
        attempts.recordAttempt();

        if (attempts.isLocked()) {
            LOGGER.warning("Account locked due to too many failed attempts: " + username);
        }
    }

    /**
     * Represents an active user session.
     */
    public static class Session {
        private final String token;
        private final User1 user;
        private final int timeoutMinutes;
        private LocalDateTime lastAccessTime;
        private final LocalDateTime createdAt;

        public Session(String token, User1 user, int timeoutMinutes) {
            this.token = token;
            this.user = user;
            this.timeoutMinutes = timeoutMinutes;
            this.lastAccessTime = LocalDateTime.now();
            this.createdAt = LocalDateTime.now();
        }

        public void refresh() {
            this.lastAccessTime = LocalDateTime.now();
        }

        public boolean isExpired() {
            LocalDateTime expiryTime = lastAccessTime.plusMinutes(timeoutMinutes);
            return LocalDateTime.now().isAfter(expiryTime);
        }

        public String getToken() {
            return token;
        }

        public User1 getUser() {
            return user;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public long getSessionDurationMinutes() {
            return ChronoUnit.MINUTES.between(createdAt, LocalDateTime.now());
        }
    }

    /**
     * Tracks failed login attempts for rate limiting.
     */
    private static class LoginAttempts {
        private final int maxAttempts;
        private final int lockoutDurationMinutes;
        private int attemptCount;
        private LocalDateTime firstAttemptTime;
        private LocalDateTime lockoutTime;

        public LoginAttempts(int maxAttempts, int lockoutDurationMinutes) {
            this.maxAttempts = maxAttempts;
            this.lockoutDurationMinutes = lockoutDurationMinutes;
            this.attemptCount = 0;
        }

        public void recordAttempt() {
            if (firstAttemptTime == null) {
                firstAttemptTime = LocalDateTime.now();
            }
            attemptCount++;

            if (attemptCount >= maxAttempts) {
                lockoutTime = LocalDateTime.now();
            }
        }

        public boolean isLocked() {
            if (lockoutTime == null) {
                return false;
            }

            LocalDateTime unlockTime = lockoutTime.plusMinutes(lockoutDurationMinutes);
            if (LocalDateTime.now().isAfter(unlockTime)) {
                // Lockout period has passed, reset
                reset();
                return false;
            }

            return true;
        }

        private void reset() {
            attemptCount = 0;
            firstAttemptTime = null;
            lockoutTime = null;
        }
    }
}
