package com.puskesmas.inventory.web.util;

import com.puskesmas.inventory.web.config.AppConfig;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for password hashing and verification using bcrypt.
 */
public final class PasswordUtil {

    private static final Logger log = LoggerFactory.getLogger(PasswordUtil.class);

    private PasswordUtil() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Hash a plain text password using bcrypt.
     *
     * @param rawPassword the plain text password
     * @return the hashed password
     */
    public static String hash(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(AppConfig.BCRYPT_STRENGTH));
    }

    /**
     * Verify a plain text password against a hashed password.
     *
     * @param rawPassword the plain text password
     * @param hashedPassword the hashed password
     * @return true if password matches, false otherwise
     */
    public static boolean matches(String rawPassword, String hashedPassword) {
        if (hashedPassword == null || rawPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(rawPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid password hash format", e);
            return false;
        }
    }
}
