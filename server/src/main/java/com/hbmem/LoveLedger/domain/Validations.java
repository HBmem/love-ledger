package com.hbmem.LoveLedger.domain;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$";

    /**
     * Validate that string is not null or blank
     * @param value
     * value for validation
     * @return true valid value, false invalid value
     */
    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * Validate email with regular expression
     * @param email
     * email for validation
     * @return true valid email, false invalid email
     */
    public static boolean validEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
