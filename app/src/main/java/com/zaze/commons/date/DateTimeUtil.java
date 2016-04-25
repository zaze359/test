package com.zaze.commons.date;

import org.joda.time.DateTime;
import org.joda.time.Days;
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

    // ----------------- about calculate -----------------
//    /**
//     * @param one
//     * @param two
//     * @return
//     */
//    public static long offsetHour(DateTime one, DateTime two) {
//        long offset = offsetTimeMillis(one, two);
//        return offset / 3600;
//    }
    /**
     * @param one
     * @param two
     * @return
     */
    public static int offsetDay(DateTime one, DateTime two) {
        return Days.daysBetween(one, two).getDays();
    }

//    public static long offsetTimeMillis(String , String, String)
    /**
     * @param one
     * @param two
     * @return 时间偏移量
     */
    public static long offsetTimeMillis(DateTime one, DateTime two) {
        long first = 0;
        long second = 0;
        if(one != null) {
            first = one.getMillis();
        }
        if(two != null) {
            second = two.getMillis();
        }
        return first - second;
    }
    /**
     * @return 从现在到当天结束 还有多少时间
     */
    public static long nowToEnd() {
        return getEnd(new DateTime()).getMillis() - System.currentTimeMillis();
    }

    /**
     * @return 从开始到现在 过去了多少时间
     */
    public static long nowToStart() {
        return System.currentTimeMillis() - getStart(new DateTime()).getMillis();

    }
    // ----------------- about start or end
    public static long getDayEnd(String dateStr, String pattern) {
        return getTimeMillis(getEnd(dateStr, pattern));
    }
    public static String getDayEnd(long timeMillis, String pattern) {
        return dateToString(getEnd(timeMillis), pattern);
    }
    //
    public static long getDayStart(String dateString, String pattern) {
        return getTimeMillis(getStart(dateString, pattern));
    }
    public static String getDayStart(long timeMillis, String pattern) {
        return dateToString(getStart(timeMillis), pattern);
    }
    // ----------------- about trans -----------------
    // ----------------- trans String
    /**
     * @param timeMillis
     * @param pattern 输出日期格式
     * @return
     */
    public static String timeMillisToString(long timeMillis, String pattern) {
        return dateToString(timeMillisToDate(timeMillis), pattern);
    }

    /**
     * @param dateStr String
     * @param oldPat 当前日期格式
     * @param newPat 输出日期格式
     * @return
     */
    public static String stringToString(String dateStr, String oldPat, String newPat) {
        DateTime dateTime = stringToDate(dateStr, oldPat);
        if (dateTime != null) {
            return dateTime.toString(newPat);
        }
        return dateStr;
    }

    /**
     * @param date    Date 对象
     * @param pattern 输出日期格式
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
     * @param dateTime Date 对象
     * @param pattern  输出日期格式
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

    // ----------------- trans DateTime
    /**
     * @param dateStr String
     * @param pattern 日期格式
     * @return
     */
    public static DateTime stringToDate(String dateStr, String pattern) {
        if (null == dateStr || "".equals(dateStr) ||null == pattern || "".equals(pattern)) {
            return null;
        }
        return DateTime.parse(dateStr, getFormat(pattern));
    }

//    /**
//     * @param dateStr String
//     * @return
//     */
//    public static DateTime stringToDate(String dateStr) {
//        if (null == dateStr || "".equals(dateStr)) {
//            return null;
//        }
//        return DateTime.parse(dateStr);
//    }

    /**
     * @param timeMillis
     * @return DateTime
     */
    public static DateTime timeMillisToDate(long timeMillis) {
        return new DateTime(timeMillis);
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
     * @param timeMillis 日期字符串
     * @return 星期
     */
    public static Week getWeek(long timeMillis) {
        return getWeek(timeMillisToDate(timeMillis));
    }

    /**
     * @param date 日期字符串
     * @return 星期。失败返回null。
     */
    public static Week getWeek(String date, String pattern) {
        return getWeek(stringToDate(date, pattern));
    }

    /**
     * @param dateTime 日期
     * @return 星期
     */
    public static Week getWeek(DateTime dateTime) {
        Week week = null;
        int a = dateTime.getDayOfWeek();
        switch (dateTime.getDayOfWeek()) {
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
            case 7:
                week = Week.SUNDAY;
                break;
        }
        return week;
    }
    // ---------------- private func ------------------
    private static DateTimeFormatter getFormat(String pattern) {
        return DateTimeFormat.forPattern(pattern);
    }
    //
    private static long getTimeMillis(DateTime dateTime) {
        if(dateTime != null) {
            return dateTime.getMillis();
        }
        return 0;
    }
    //
    public static DateTime getEnd(String dateStr, String pattern) {
        return getEnd(stringToDate(dateStr, pattern));
    }
    public static DateTime getEnd(long timeMillis) {
        return getEnd(timeMillisToDate(timeMillis));
    }
    public static DateTime getStart(String dateString, String pattern) {
        return getStart(stringToDate(dateString, pattern));
    }
    public static DateTime getStart(long timeMillis) {
        return getStart(timeMillisToDate(timeMillis));
    }
    private static DateTime getStart(DateTime dateTime) {
        if(dateTime != null) {
            return dateTime.millisOfDay().withMinimumValue();
        }
        return null;
    }
    private static DateTime getEnd(DateTime dateTime) {
        if(dateTime != null) {
            return dateTime.millisOfDay().withMaximumValue();
        }
        return null;
    }
    //
}
