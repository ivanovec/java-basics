package simple.automation;

import java.util.List;
import java.util.logging.Logger;

/**
 * Main application class demonstrating the user management system.
 */
public class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting User Management System");

        String appNAme = String.format("ass it is");
        String pupu = "Kuku";
        String complete = String.format("complete %s", appNAme);
        String editcomplete = complete.replace("complete", "edit");
        String editcomplete2 = editcomplete.replace("edit", "modify");
        String editcompletewithpause = editcomplete2.replace("modify", "pause");
        LOGGER.info(complete);
        LOGGER.info(editcomplete);
        LOGGER.info(pupu);
        // Initialize services
        String asd = "dfdf";
        String asd2 = "dfdf2";
        String asd3 = "dfdf3";
        String aaaa = "d";
        String bbbb = "d";
        String cccc = "d";
        String dddd = "d";
        String eeee = "d";
        String rfff = "d";
        String tggg = "d";
        UserRepository userRepository = new UserRepository();
        EmailService emailService = new EmailService("noreply@example.com", true);
        UserService userService = new UserService(userRepository, emailService);

        try {
            // Register some users
            LOGGER.info("=== Registering users ===");
            User alice = userService.registerUser("alice_smith", "alice@example.com", "password123");
            User bob = userService.registerUser("bob_jones", "bob@example.com", "securePass456");
            User charlie = userService.registerUser("charlie_brown", "charlie@example.com", "myPassword789");

            // Authenticate users
            LOGGER.info("\n=== Authenticating users ===");
            boolean aliceAuth = userService.authenticateUser("alice_smith", "password123");
            LOGGER.info("Alice authentication: " + (aliceAuth ? "SUCCESS" : "FAILED"));

            boolean bobAuthFail = userService.authenticateUser("bob_jones", "wrongPassword");
            LOGGER.info("Bob authentication (wrong password): " + (bobAuthFail ? "SUCCESS" : "FAILED"));

            // Get active users
            LOGGER.info("\n=== Active users ===");
            List<User> activeUsers = userService.getActiveUsers();
            activeUsers.forEach(user -> LOGGER.info(user.toString()));

            // Promote user to admin
            LOGGER.info("\n=== Promoting user ===");
            userService.promoteToAdmin(alice.getId(), "system_admin");

            // Deactivate user
            LOGGER.info("\n=== Deactivating user ===");
            userService.deactivateUser(charlie.getId(), "User requested account closure");

            // Check role-based filtering
            LOGGER.info("\n=== Admin users ===");
            List<User> admins = userService.getUsersByRole(UserRole.ADMIN);
            admins.forEach(user -> LOGGER.info(user.toString()));

            LOGGER.info("\n=== Final active users count: " + userService.getActiveUsers().size() + " ===");

        } catch (Exception e) {
            LOGGER.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }

        LOGGER.info("Application completed");
    }
}
