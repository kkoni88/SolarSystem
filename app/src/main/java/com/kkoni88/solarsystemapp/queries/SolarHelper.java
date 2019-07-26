package com.kkoni88.solarsystemapp.queries;

import android.icu.text.SimpleDateFormat;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

class SolarHelper {
    static String getDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    static String getZonedDateTime(ZonedDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        return formatter.format(dateTime);
    }

    static String getZonedDate(ZonedDateTime dateTime) {
        return getZonedDate(dateTime, "00:00:00");
    }

    static String getZonedDate(ZonedDateTime dateTime, String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(String.format("yyyy-MM-dd %s", time));
        return formatter.format(dateTime);
    }
}
