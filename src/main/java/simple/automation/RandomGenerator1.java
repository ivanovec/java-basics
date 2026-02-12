package simple.automation;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class RandomGenerator1 {
    private final Random random;
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";

    public RandomGenerator1() {
        this.random = new Random();
    }

    public RandomGenerator1(boolean secure) {
        this.random = secure ? new SecureRandom() : new Random();
    }

    public int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public long randomLong(long min, long max) {
        return min + (long) (random.nextDouble() * (max - min));
    }

    public double randomDouble(double min, double max) {
        return min + (random.nextDouble() * (max - min));
    }

    public boolean randomBoolean() {
        return random.nextBoolean();
    }

    public String randomString(int length) {
        return randomString(length, ALPHANUMERIC);
    }

    public String randomAlphabetic(int length) {
        return randomString(length, ALPHABETIC);
    }

    public String randomNumeric(int length) {
        return randomString(length, NUMERIC);
    }

    private String randomString(int length, String characters) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }

    public String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public String randomEmail() {
        return randomString(10) + "@" + randomString(8) + ".com";
    }

    public <T> T randomElement(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[random.nextInt(array.length)];
    }
}
