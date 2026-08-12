package utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    private static final DateTimeFormatter MM_DD_YYYY = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final DateTimeFormatter MM_DD_YYYY_HH_MM_SS = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    /**
     * Converts epoch millis to a LocalDate in the given zone.
     * Use this when you only care about the calendar date, not time-of-day.
     */
    public static LocalDate toLocalDate(long epochMillis, ZoneId zoneId) {
        return Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate();
    }

    /**
     * Converts epoch millis to a LocalDateTime in the given zone.
     * Use this when time-of-day matters.
     */
    public static LocalDateTime toLocalDateTime(long epochMillis, ZoneId zoneId) {
        return Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDateTime();
    }

    /** Convenience: epoch millis -> "MM-dd-yyyy" string, in a specific zone. */
    public static String toFormattedDate(long epochMillis, ZoneId zoneId) {
        return toLocalDate(epochMillis, zoneId).format(MM_DD_YYYY);
    }

    /**
     * Convenience: epoch millis -> "MM-dd-yyyy HH:mm:ss" string, in a specific
     * zone.
     */
    public static String toFormattedDateTime(long epochMillis, ZoneId zoneId) {
        return toLocalDateTime(epochMillis, zoneId).format(MM_DD_YYYY_HH_MM_SS);
    }
}