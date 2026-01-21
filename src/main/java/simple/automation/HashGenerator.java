package simple.automation;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashGenerator {

    public String md5(String input) throws NoSuchAlgorithmException {
        return generateHash(input, "MD5");
    }

    public String sha256(String input) throws NoSuchAlgorithmException {
        return generateHash(input, "SHA-256");
    }

    public String sha512(String input) throws NoSuchAlgorithmException {
        return generateHash(input, "SHA-512");
    }

    private String generateHash(String input, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public String base64Encode(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String base64Decode(String encoded) {
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public boolean verifyHash(String input, String hash, String algorithm) {
        try {
            String computedHash = generateHash(input, algorithm);
            return computedHash.equals(hash);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
}
