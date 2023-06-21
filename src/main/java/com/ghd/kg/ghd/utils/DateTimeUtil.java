package com.ghd.kg.ghd.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateTimeUtil {
    public static String formatDateTime(LocalDateTime dateTime, String locale, FormatStyle style) {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                .withLocale(new Locale(locale))
                .format(LocalDate.of(
                        dateTime.getYear(),
                        dateTime.getMonth(),
                        dateTime.getDayOfMonth()));
    }
}
