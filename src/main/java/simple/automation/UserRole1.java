package simple.automation;

/**
 * Defines the roles available in the system with associated permissions.
 */
public enum UserRole1 {
    USER("Basic user with limited permissions"),
    MODERATOR("Can moderate content and manage users"),
    ADMIN("Full system access with all permissions");

    private final String description;

    UserRole1(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasPermission(Permission permission) {
        switch (this) {
            case ADMIN:
                return true;
            case MODERATOR:
                return permission == Permission.READ ||
                        permission == Permission.WRITE ||
                        permission == Permission.MODERATE;
            case USER:
                return permission == Permission.READ ||
                        permission == Permission.WRITE;
            default:
                return false;
        }
    }

    public enum Permission {
        READ,
        WRITE,
        MODERATE,
        ADMIN;

        public void newStubMethod() {
            // TODO: implement
        }
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
