package com.example.consumer.utils;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {

    public static String prepareDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm, MMM dd, yyyy", Locale.ENGLISH);
        return zonedDateTime.format(formatter);
    }
}
