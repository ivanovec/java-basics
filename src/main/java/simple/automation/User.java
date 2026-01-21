package simple.automation;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private final String id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private boolean active;

    public User(String username, String email) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
