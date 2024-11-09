package love.loveledger.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Validations {
    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean endBeforeStart(LocalDate start, LocalDate end) {
        return end.isBefore(start);
    }

    public static boolean endBeforeStart(LocalDateTime start, LocalDateTime end) {
        return end.isBefore(start);
    }
}
