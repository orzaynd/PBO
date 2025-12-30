package com.puskesmas.inventory.web.config;

/**
 * Application configuration constants.
 */
public final class AppConfig {
    
    public static final String DB_URL = System.getenv().getOrDefault(
        "DB_URL", "jdbc:postgresql://localhost:5432/puskesmas_db"
    );
    
    public static final String DB_USER = System.getenv().getOrDefault(
        "DB_USER", "postgres"
    );
    
    public static final String DB_PASSWORD = System.getenv().getOrDefault(
        "DB_PASSWORD", "postgres"
    );
    
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    public static final int BCRYPT_STRENGTH = 10;
    
    public static final String USER_SESSION_KEY = "user";
    public static final String ERROR_ATTRIBUTE_KEY = "error";
    public static final String SUCCESS_ATTRIBUTE_KEY = "success";
    
    private AppConfig() {
        throw new AssertionError("Cannot instantiate utility class");
    }
}
