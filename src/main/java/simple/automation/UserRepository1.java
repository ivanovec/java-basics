package simple.automation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for managing user data with in-memory storage.
 */
public class UserRepository1 {
    private final Map<String, User1> usersById;
    private final Map<String, User1> usersByUsername;
    private final Map<String, User1> usersByEmail;

    public UserRepository1() {
        this.usersById = new ConcurrentHashMap<>();
        this.usersByUsername = new ConcurrentHashMap<>();
        this.usersByEmail = new ConcurrentHashMap<>();
    }

    public User1 save(User1 user) {
        if (user == null) {
            throw new IllegalArgumentException("User1 cannot be null");
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

    public Optional<User1> findById(String id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public Optional<User1> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    public Optional<User1> findByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email));
    }

    public List<User1> findAll() {
        return new ArrayList<>(usersById.values());
    }

    public List<User1> findActiveUsers() {
        return usersById.values().stream()
                .filter(User1::isActive)
                .collect(Collectors.toList());
    }

    public List<User1> findByRole(UserRole1 role) {
        return usersById.values().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }

    public boolean delete(String id) {
        User1 user = usersById.remove(id);
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
