package simple.automation;

import java.util.List;
import java.util.logging.Logger;

/**
 * Main1 application class demonstrating the user management system.
 */
public class Application1 {
    private static final Logger LOGGER = Logger.getLogger(Application1.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting User1 Management System");

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
        String yyyy = "d";
        String mmmm = "dff";
        String ddddd = "fd";
        String dfdfdfdf = "ddd";
        UserRepository1 userRepository = new UserRepository1();
        EmailService1 emailService = new EmailService1("noreply@example.com", true);
        UserService1 userService = new UserService1(userRepository, emailService);

        try {
            // Register some users
            LOGGER.info("=== Registering users ===");
            User1 alice = userService.registerUser("alice_smith", "alice@example.com", "password123");
            User1 bob = userService.registerUser("bob_jones", "bob@example.com", "securePass456");
            User1 charlie = userService.registerUser("charlie_brown", "charlie@example.com", "myPassword789");

            // Authenticate users
            LOGGER.info("\n=== Authenticating users ===");
            boolean aliceAuth = userService.authenticateUser("alice_smith", "password123");
            LOGGER.info("Alice authentication: " + (aliceAuth ? "SUCCESS" : "FAILED"));

            boolean bobAuthFail = userService.authenticateUser("bob_jones", "wrongPassword");
            LOGGER.info("Bob authentication (wrong password): " + (bobAuthFail ? "SUCCESS" : "FAILED"));

            // Get active users
            LOGGER.info("\n=== Active users ===");
            List<User1> activeUsers = userService.getActiveUsers();
            activeUsers.forEach(user -> LOGGER.info(user.toString()));

            // Promote user to admin
            LOGGER.info("\n=== Promoting user ===");
            userService.promoteToAdmin(alice.getId(), "system_admin");

            // Deactivate user
            LOGGER.info("\n=== Deactivating user ===");
            userService.deactivateUser(charlie.getId(), "User1 requested account closure");

            // Check role-based filtering
            LOGGER.info("\n=== Admin users ===");
            List<User1> admins = userService.getUsersByRole(UserRole1.ADMIN);
            admins.forEach(user -> LOGGER.info(user.toString()));

            LOGGER.info("\n=== Final active users count: " + userService.getActiveUsers().size() + " ===");

        } catch (Exception1 e) {
            LOGGER.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }

        LOGGER.info("Application1 completed");
    }

    /**
     * Processes user data with additional validation.
     *
     * @param username the username to process
     * @return processed username in uppercase
     */
    public static String processUserData(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return username.trim().toUpperCase();
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
