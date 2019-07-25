package com.kkoni88.solarsystemapp;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

class SolarHelper {
    static String getDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    static String getDateAndTime(Date date, String time) {
        return String.format("%s %s:00", SolarHelper.getDate(date), time);
    }
}
