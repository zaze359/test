package com.zaze.utils.date;


import com.zaze.aarrepo.commons.date.Month;
import com.zaze.utils.ZStringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Description :
 * date : 2016-04-21 - 12:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class ZDateUtil {
    public static final long SECOND = 1000L;
    public static final long MINUTE = 60000L;
    public static final long HOUR = 3600000L;
    public static final long DAY = 86400000L;
    public static final long WEEK = 604800000L;
    public static final long HALF_YEAR = 110376000000L;
    public static final long YEAR = 220752000000L;


    public static String getMinAndSec(long timeMillis) {
        return ZStringUtil.format(
                "%02d' %02d'%s ", getMinute(timeMillis), getSecond(timeMillis), "'"
        );
    }

    public static String getHourAndMin(long timeMillis) {
        return timeMillisToString(timeMillis, "HH:mm");
    }

    public static long getTimeMillisByHM(String dateStr) {
        Date date = stringToDate(dateStr, "HH:mm");
        int hour = getHour(date);
        int minute = getMinute(date);
        return 1000L * (hour * 3600 + minute * 60);
    }

    public static long getTimeMillisByHM(long timeMillis) {
        Date date = timeMillisToDate(timeMillis);
        int hour = getHour(date);
        int minute = getMinute(date);
        return 1000L * (hour * 3600 + minute * 60);
    }

    // ----------------- about trans -----------------

    /**
     * @param dateStr String
     * @param pattern 日期格式
     * @return
     */
    public static Date stringToDate(String dateStr, String pattern) {
        if (dateStr != null) {
            try {
                return getDateFormat(pattern).parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param date    Date 对象
     * @param pattern 日期格式
     * @return 转换特定格式的日期字符串
     */
    public static String dateToString(Date date, String pattern) {
        if (date != null) {
            return getDateFormat(pattern).format(date);
        }
        return "";
    }

    /**
     * @param timeMillis
     * @param pattern
     * @return
     */
    public static String timeMillisToString(long timeMillis, String pattern) {
        return dateToString(new Date(timeMillis), pattern);
    }

    public static Date timeMillisToDate(long timeMillis) {
        return new Date(timeMillis);
    }

    // ----------------  about millis ------------------
    public static long getDayTimeMillis() {
        return 86400000L;
    }

    public static long getWeekTimeMillis() {
        return 604800000L;
    }

    // ----------------  about int ------------------
    public static int getYear(Date date) {
        return getInteger(date, Calendar.YEAR);
    }

    /**
     * @param date
     * @return day num
     */
    public static int getDay(Date date) {
        return getInteger(date, Calendar.DATE);
    }

    /**
     * @param date
     * @return hour num
     */
    public static int getHour(Date date) {
        return getInteger(date, Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        return getInteger(date, Calendar.MINUTE);
    }

    public static int getMinute(long timeMillis) {
        return getInteger(timeMillis, Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        return getInteger(date, Calendar.SECOND);
    }

    public static int getSecond(long timeMillis) {
        return getInteger(timeMillis, Calendar.SECOND);
    }


    // ----------------  about day ------------------

    /**
     * @param timeMillis
     * @return 一天的开始时间
     */
    public static long getDayStart(long timeMillis) {
        Calendar calendar = getCalendar(new Date(timeMillis));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * @param timeMillis
     * @return 一天的结束时间
     */
    public static long getDayEnd(long timeMillis) {
        Calendar calendar = getCalendar(new Date(timeMillis));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        return calendar.getTimeInMillis();
    }

    // ----------------  about week ------------------

    /**
     * @param timeMillis
     * @return (一周的开始)本周周一的开始时间
     */
    public static long getWeekStart(long timeMillis) {
        Week week = getWeek(timeMillis);
        int day = week.getNumber();
        return getDayStart(timeMillis - (day - 1) * DAY);
    }

    /**
     * @param timeMillis
     * @return (一周的结束)本周周日的结束
     */
    public static long getWeekEnd(long timeMillis) {
        Week week = getWeek(timeMillis);
        int day = week.getNumber();
        long endTime = getDayEnd(timeMillis);
        return endTime + (7 - day) * DAY;
    }

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

    public static Week getWeek(long timeMillis) {
        return getWeek(new Date(timeMillis));
    }

    /**
     * 获取日期的星期。失败返回null。
     *
     * @param date 日期
     * @return 星期
     */
    public static Week getWeek(Date date) {
        Week week;
        switch (getInteger(date, Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                week = Week.SUNDAY;
                break;
            case Calendar.MONDAY:
                week = Week.MONDAY;
                break;
            case Calendar.TUESDAY:
                week = Week.TUESDAY;
                break;
            case Calendar.WEDNESDAY:
                week = Week.WEDNESDAY;
                break;
            case Calendar.THURSDAY:
                week = Week.THURSDAY;
                break;
            case Calendar.FRIDAY:
                week = Week.FRIDAY;
                break;
            case Calendar.SATURDAY:
                week = Week.SATURDAY;
                break;
            default:
                week = Week.MONDAY;
                break;
        }
        return week;
    }

    // ----------------  about month ------------------
    public static long calculateMonth(long timeMillis, int offset) {
        Calendar calendar = getCalendar(new Date(timeMillis));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + offset);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取日期的月。失败返回null。
     *
     * @param date 日期
     * @return 星期
     */
    public static Month getMonth(Date date) {
        Month month;
        switch (getInteger(date, Calendar.MONTH)) {
            case Calendar.JANUARY:
                month = Month.JANUARY;
                break;
            case Calendar.FEBRUARY:
                month = Month.FEBRUARY;
                break;
            case Calendar.MARCH:
                month = Month.MARCH;
                break;
            case Calendar.APRIL:
                month = Month.APRIL;
                break;
            case Calendar.MAY:
                month = Month.MAY;
                break;
            case Calendar.JUNE:
                month = Month.JUNE;
                break;
            case Calendar.JULY:
                month = Month.JULY;
                break;
            case Calendar.AUGUST:
                month = Month.AUGUST;
                break;
            case Calendar.SEPTEMBER:
                month = Month.SEPTEMBER;
                break;
            case Calendar.OCTOBER:
                month = Month.OCTOBER;
                break;
            case Calendar.NOVEMBER:
                month = Month.NOVEMBER;
                break;
            case Calendar.DECEMBER:
                month = Month.DECEMBER;
                break;
            default:
                month = Month.JANUARY;
                break;
        }
        return month;
    }
    // ---------------- private func ------------------

//    public static long dong8QuTimeMillis() {
//        return System.currentTimeMillis() + 28800000L;
//    }

    /**
     * 服务器时间已经是东八区 设置为GMT 防止转换时多加了时区
     */
    public static void setGMTTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    /**
     * @param timeMillis 日期
     * @param dateType   年，月，日...
     * @return
     */
    private static int getInteger(long timeMillis, int dateType) {
        if (timeMillis > 0L) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(timeMillis));
            return calendar.get(dateType);
        }
        return 0;
    }

    /**
     * @param date     日期
     * @param dateType 年，月，日...
     * @return
     */
    private static int getInteger(Date date, int dateType) {
        if (date != null) {
            Calendar calendar = getCalendar(date);
            return calendar.get(dateType);
        }
        return 0;
    }

    /**
     * @param pattern 日期格式
     * @return SimpleDateFormat
     */
    private static SimpleDateFormat getDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault());
    }

    /**
     * @param date 日期
     * @return
     */
    private static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
