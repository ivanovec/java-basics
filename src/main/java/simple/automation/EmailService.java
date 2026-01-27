package simple.automation;

import java.util.logging.Logger;

/**
 * Service for sending emails to users.
 * In production, this would integrate with an actual email provider.
 */
public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());
    
    private final String fromAddress;
    private final boolean enabled;

    public EmailService(String fromAddress, boolean enabled) {
        this.fromAddress = fromAddress;
        this.enabled = enabled;
    }

    public void sendWelcomeEmail(User user) {
        if (!enabled) {
            LOGGER.info("Email service disabled, skipping welcome email");
            return;
        }

        String subject = "Welcome to Our Platform!";
        String body = String.format(
            "Hello %s,\n\n" +
            "Welcome to our platform! Your account has been created successfully.\n\n" +
            "Username: %s\n" +
            "Email: %s\n\n" +
            "Best regards,\n" +
            "The Team",
            user.getUsername(), user.getUsername(), user.getEmail()
        );

        sendEmail(user.getEmail(), subject, body);
    }

    public void sendAccountDeactivationEmail(User user, String reason) {
        if (!enabled) {
            return;
        }

        String subject = "Account Deactivation Notice";
        String body = String.format(
            "Hello %s,\n\n" +
            "Your account has been deactivated.\n\n" +
            "Reason: %s\n\n" +
            "If you believe this is an error, please contact support.\n\n" +
            "Best regards,\n" +
            "The Team",
            user.getUsername(), reason
        );

        sendEmail(user.getEmail(), subject, body);
    }

    public void sendRoleChangeEmail(User user, UserRole newRole) {
        if (!enabled) {
            return;
        }

        String subject = "Role Update Notification";
        String body = String.format(
            "Hello %s,\n\n" +
            "Your role has been updated to: %s\n\n" +
            "Description: %s\n\n" +
            "Best regards,\n" +
            "The Team",
            user.getUsername(), newRole, newRole.getDescription()
        );

        sendEmail(user.getEmail(), subject, body);
    }

    public void sendPasswordResetEmail(User user, String resetToken) {
        if (!enabled) {
            return;
        }

        String subject = "Password Reset Request";
        String body = String.format(
            "Hello %s,\n\n" +
            "A password reset was requested for your account.\n\n" +
            "Reset token: %s\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "Best regards,\n" +
            "The Team",
            user.getUsername(), resetToken
        );

        sendEmail(user.getEmail(), subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        // In production, integrate with SendGrid, AWS SES, etc.
        LOGGER.info(String.format(
            "Sending email:\n" +
            "From: %s\n" +
            "To: %s\n" +
            "Subject: %s\n" +
            "Body:\n%s\n",
            fromAddress, to, subject, body
        ));
    }
}
