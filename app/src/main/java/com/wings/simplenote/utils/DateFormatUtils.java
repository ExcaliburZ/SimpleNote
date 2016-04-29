package com.wings.simplenote.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wing on 2016/4/19.
 */
public class DateFormatUtils {
    private static final SimpleDateFormat mSimpleDateTimeFormat
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat mSimpleDateFormat
            = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat mSimpleTimeFormat
            = new SimpleDateFormat("HH:mm");

    private static final SimpleDateFormat mSimpleTextFormat
            = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String formatDateTime(Date date) {
        return mSimpleDateTimeFormat.format(date);
    }

    public static String formatDate(Date date) {
        return mSimpleDateFormat.format(date);
    }

    public static String formatTime(Date date) {
        return mSimpleTimeFormat.format(date);
    }


    public static Date parse(String dateStr) {
        try {
            return mSimpleDateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("date formatDateTime error");
        }
    }

    public static Date parseText(String dateStr) {
        try {
            return mSimpleTextFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("date formatDateTime error");
        }
    }

    public static Date parseDate(String dateStr) {
        try {
            return mSimpleDateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("date formatDateTime error");
        }
    }


}
