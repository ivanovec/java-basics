package simple.automation;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean isValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    public ValidationResult validate(String email) {
        if (email == null || email.isEmpty()) {
            return new ValidationResult(false, "Email cannot be empty");
        }

        if (email.length() > 254) {
            return new ValidationResult(false, "Email is too long");
        }

        if (!pattern.matcher(email).matches()) {
            return new ValidationResult(false, "Invalid email format");
        }

        return new ValidationResult(true, "Valid email");
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
