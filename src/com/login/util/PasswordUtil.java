package com.login.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Simple SHA-256 password hashing utility.
 * In production, prefer BCrypt (e.g. jBCrypt library).
 */
public class PasswordUtil {

    private PasswordUtil() {}

    /** Returns the SHA-256 hex digest of the given plain-text password. */
    public static String hash(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(plainText.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available.", e);
        }
    }

    /** Verifies a plain-text password against a stored hash. */
    public static boolean verify(String plainText, String storedHash) {
        return hash(plainText).equalsIgnoreCase(storedHash);
    }
}
