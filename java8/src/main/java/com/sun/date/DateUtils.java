package com.sun.date;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {
    private DateUtils(){}

    private static ZonedDateTime getZonedDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault());
    }

    public static LocalTime getLocalTime(Date date) {
        return getZonedDateTime(date).toLocalTime();
    }
    public static LocalDate getLocalDate(Date date) {
        return getZonedDateTime(date).toLocalDate();
    }
    public static LocalDateTime getLocalDateTime(Date date) {
        return getZonedDateTime(date).toLocalDateTime();
    }

    public static Date localDate2Date(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static long betweenLocalDateTimeDay(LocalDateTime localDateTime1,LocalDateTime localDateTime2) {
        return ChronoUnit.DAYS.between(localDateTime1,localDateTime2);
    }
}
