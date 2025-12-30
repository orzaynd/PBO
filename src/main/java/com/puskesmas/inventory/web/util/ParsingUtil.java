package com.puskesmas.inventory.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Utility for parsing and validating request parameters.
 */
public final class ParsingUtil {
    
    private static final Logger log = LoggerFactory.getLogger(ParsingUtil.class);

    private ParsingUtil() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Parse integer from string with default value on error.
     */
    public static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

    /**
     * Parse BigDecimal from string with ZERO default on error.
     */
    public static BigDecimal parseBigDecimal(String value) {
        try {
            if (value == null || value.isEmpty()) {
                return BigDecimal.ZERO;
            }
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Validate string is not null/empty.
     */
    public static boolean isValid(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
