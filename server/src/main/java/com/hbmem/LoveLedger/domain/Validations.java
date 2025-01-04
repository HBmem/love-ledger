package com.hbmem.LoveLedger.domain;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {
    private static final String EMAIL_PATTERN = "^((([!#$%&'*+\\-/=?^_`{|}~\\w])|([!#$%&'*+\\-/=?^_`{|}~\\w][!#$%&'*+\\-/=?^_`{|}~\\.\\w]{0,}[!#$%&'*+\\-/=?^_`{|}~\\w]))[@]\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)$";

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
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    /**
     * Validate that the end date is before the start date
     * @param start
     * event start date
     * @param end
     * event end date
     * @return true end is before start, start is before end
     */
    public static boolean endBeforeStart(LocalDate start, LocalDate end) {
        return end.isBefore(start);
    }

    /**
     * Validate that the end date is before the start date
     * @param start
     * event start date
     * @param end
     * event end date
     * @return true end is before start, start is before end
     */
    public static boolean endBeforeStart(LocalDateTime start, LocalDateTime end) {
        return end.isBefore(start);
    }
}
