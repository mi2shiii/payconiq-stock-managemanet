package com.payconiq.backend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateProcessor {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static String toString(final LocalDateTime date) {
        return date.format(formatter);
    }
}
