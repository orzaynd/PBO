package dev.oryzaa.projekpbo.web.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hash(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt(10));
    }

    public static boolean matches(String raw, String hashed) {
        if (hashed == null || raw == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(raw, hashed);
        } catch (Exception ex) {
            return raw.equals(hashed);
        }
    }
}
