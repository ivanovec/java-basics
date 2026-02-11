package simple.automation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorTest {

    private final EmailValidator validator = new EmailValidator();

    @Test
    void isValidRejectsNullOrEmpty() {
        assertFalse(validator.isValid(null));
        assertFalse(validator.isValid(""));
    }

    @Test
    void isValidAcceptsCommonAddresses() {
        assertTrue(validator.isValid("john.doe@example.com"));
        assertTrue(validator.isValid("a_b+tag-123@sub.example.org"));
    }

    @Test
    void isValidRejectsMalformedAddresses() {
        assertFalse(validator.isValid("john..doe@example.com"));
        assertFalse(validator.isValid("john.doe@example"));
        assertFalse(validator.isValid("@example.com"));
        assertFalse(validator.isValid("john.doe@.com"));
    }

    @Test
    void validateExplainsEmptyInput() {
        EmailValidator.ValidationResult result = validator.validate("");

        assertFalse(result.isValid());
        assertEquals("Email cannot be empty", result.getMessage());
    }

    @Test
    void validateRejectsTooLongEmail() {
        String localPart = "a".repeat(200);
        String domain = "b".repeat(60) + ".com";
        String email = localPart + "@" + domain;

        EmailValidator.ValidationResult result = validator.validate(email);

        assertFalse(result.isValid());
        assertEquals("Email is too long", result.getMessage());
    }

    @Test
    void validateExplainsInvalidFormat() {
        EmailValidator.ValidationResult result = validator.validate("john..doe@example.com");

        assertFalse(result.isValid());
        assertEquals("Invalid email format", result.getMessage());
    }

    @Test
    void validateConfirmsValidEmail() {
        EmailValidator.ValidationResult result = validator.validate("john.doe@example.com");

        assertTrue(result.isValid());
        assertEquals("Valid email", result.getMessage());
    }
}
