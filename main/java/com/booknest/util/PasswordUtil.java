package com.booknest.util;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utility class for securely hashing and verifying passwords using PBKDF2.
 * Does NOT provide reversible encryption (do not decrypt passwords!).
 */
public class PasswordUtil {
    private static final int SALT_LENGTH_BYTE = 16;
    private static final int HASH_LENGTH_BYTE = 32; // 256 bits
    private static final int ITERATIONS = 65536;
    /**
     * Generates a random salt.
     */
    private static byte[] getRandomSalt(int numBytes) {
        byte[] salt = new byte[numBytes];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Hashes a password with a random salt using PBKDF2WithHmacSHA256.
     * Returns a string of the form: salt:hash (both Base64 encoded).
     */
    public  String hashPassword(String password) {
        try {
            byte[] salt = getRandomSalt(SALT_LENGTH_BYTE);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_LENGTH_BYTE * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Verifies a password against a stored hash (salt:hash format).
     */
    public  boolean verifyPassword(String password, String stored) {
        try {
            String[] parts = stored.split(":");
            if (parts.length != 2) return false;
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, expectedHash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            // Constant-time compare
            if (hash.length != expectedHash.length) return false;
            int result = 0;
            for (int i = 0; i < hash.length; i++) {
                result |= hash[i] ^ expectedHash[i];
            }
            return result == 0;
        } catch (Exception e) {
            Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
}
