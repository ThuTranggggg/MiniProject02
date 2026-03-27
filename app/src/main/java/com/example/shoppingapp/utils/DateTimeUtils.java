package com.example.shoppingapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateTimeUtils {

    private static final String DEFAULT_PATTERN = "dd/MM/yyyy HH:mm";
    private static final String ISO_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DISPLAY_DATE_PATTERN = "dd/MM/yyyy";

    private DateTimeUtils() {
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(new Date());
    }

    public static String formatBookingTimestamp(long timestamp) {
        return new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(new Date(timestamp));
    }

    public static String formatShowDate(String isoDate) {
        try {
            Date date = new SimpleDateFormat(ISO_DATE_PATTERN, Locale.getDefault()).parse(isoDate);
            if (date == null) {
                return isoDate;
            }
            return new SimpleDateFormat(DISPLAY_DATE_PATTERN, Locale.getDefault()).format(date);
        } catch (Exception exception) {
            return isoDate;
        }
    }

    public static String formatShowDateTime(String showDate, String showTime) {
        return formatShowDate(showDate) + " • " + showTime;
    }
}
