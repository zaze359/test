package com.zaze.commons.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Description :
 * date : 2016-04-21 - 12:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class DateTimeUtil {

    // ----------------- about trans -----------------
    /**
     * @param dateStr String
     * @param pattern 日期格式
     * @return
     */
    public static String stringToString(String dateStr, String pattern) {
        DateTime dateTime = getDateTime(dateStr, pattern);
        if (dateTime != null) {
            return dateTime.toString();
        }
        return dateStr;
    }

    /**
     * @param dateStr String
     * @param pattern 日期格式
     * @return
     */
    public static DateTime stringToDate(String dateStr, String pattern) {
        return getDateTime(dateStr, pattern);
    }

    /**
     * @param date    Date 对象
     * @param pattern 日期格式
     * @return 转换特定格式的日期字符串
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (null == pattern || "".equals(pattern)) {
            return date.toString();
        }
        return new DateTime(date).toString(pattern);
    }

    /**
     * @param dateTime    Date 对象
     * @param pattern 日期格式
     * @return 转换特定格式的日期字符串
     */
    public static String dateToString(DateTime dateTime, String pattern) {
        if (dateTime == null) {
            return "";
        }
        if (null == pattern || "".equals(pattern)) {
            return dateTime.toString();
        }
        return dateTime.toString(pattern);
    }

    /**
     * @param timeMillis
     * @param pattern
     * @return
     */
    public static String timeMillisToString(long timeMillis, String pattern) {
        return dateToString(getDateTime(timeMillis), pattern);
    }

    // ----------------  about int ------------------
//    public static int getYear(DateTime dateTime) {
//        if(dateTime == null) {
//            return 0;
//        }
//        return dateTime.getYear();
//    }
//
//    /**
//     * @param dateTime
//     * @return day num
//     */
//    public static int getDay(DateTime dateTime) {
//        if(dateTime == null) {
//            return 0;
//        }
//        return dateTime.getDayOfMonth();
//    }
//
//    /**
//     * @param dateTime
//     * @return hour num
//     */
//    public static int getHour(DateTime dateTime) {
//        if(dateTime == null) {
//            return 0;
//        }
//        return dateTime.getHourOfDay();
//    }
    // ----------------  about week ------------------

    /**
     * 获取日期的星期。失败返回null。
     *
     * @param date    日期字符串
     * @param pattern 日期格式
     * @return 星期
     */
    public static Week getWeek(String date, String pattern) {
        return getWeek(stringToDate(date, pattern));
    }

    /**
     * 获取日期的星期。失败返回null。
     *
     * @param dateTime 日期
     * @return 星期
     */
    public static Week getWeek(DateTime dateTime) {
        Week week = null;
        switch (dateTime.getDayOfWeek()) {
            case 0:
                week = Week.SUNDAY;
                break;
            case 1:
                week = Week.MONDAY;
                break;
            case 2:
                week = Week.TUESDAY;
                break;
            case 3:
                week = Week.WEDNESDAY;
                break;
            case 4:
                week = Week.THURSDAY;
                break;
            case 5:
                week = Week.FRIDAY;
                break;
            case 6:
                week = Week.SATURDAY;
                break;
        }
        return week;
    }

    // ---------------- private func ------------------
    public static DateTime getDateTime(String dateStr, String pattern) {
        if (null == dateStr || "".equals(dateStr) || null == pattern || "".equals(pattern)) {
            return null;
        }
        return DateTime.parse(dateStr, getFormat(pattern));
    }

    public static DateTime getDateTime(String dateStr) {
        if (null == dateStr || "".equals(dateStr)) {
            return null;
        }
        return DateTime.parse(dateStr);
    }

    public static DateTime getDateTime(long timeMillis) {
        return new DateTime(timeMillis);
    }

//    private static DateTime getDateTime(Date date) {
//        if (null == date) {
//            return null;
//        }
//        return new DateTime(date);
//    }

    private static DateTimeFormatter getFormat(String pattern) {
        return DateTimeFormat.forPattern(pattern);
    }

}
