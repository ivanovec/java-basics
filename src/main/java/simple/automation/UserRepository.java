package simple.automation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for managing user data with in-memory storage.
 */
public class UserRepository {
    private final Map<String, User> usersById;
    private final Map<String, User> usersByUsername;
    private final Map<String, User> usersByEmail;

    public UserRepository() {
        this.usersById = new ConcurrentHashMap<>();
        this.usersByUsername = new ConcurrentHashMap<>();
        this.usersByEmail = new ConcurrentHashMap<>();
    }

    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Check for duplicates
        if (usersByUsername.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        if (usersByEmail.containsKey(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        usersById.put(user.getId(), user);
        usersByUsername.put(user.getUsername(), user);
        usersByEmail.put(user.getEmail(), user);

        return user;
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email));
    }

    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }

    public List<User> findActiveUsers() {
        return usersById.values().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }

    public List<User> findByRole(UserRole role) {
        return usersById.values().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }

    public boolean delete(String id) {
        User user = usersById.remove(id);
        if (user != null) {
            usersByUsername.remove(user.getUsername());
            usersByEmail.remove(user.getEmail());
            return true;
        }
        return false;
    }

    public void clear() {
        usersById.clear();
        usersByUsername.clear();
        usersByEmail.clear();
    }

    public int count() {
        return usersById.size();
    }

    public boolean exists(String id) {
        return usersById.containsKey(id);
    }
}
