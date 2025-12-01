package no.nav.bidrag.commons.testdatagenerering.time;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static LocalDate parseDate(String dateStr) {
        return dateStr != null
                ? LocalDate.parse(dateStr, DEFAULT_DATE_FORMAT)
                : null;
    }
}
