package com.zaze.commons.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Description :
 * date : 2016-04-21 - 12:11
 * @author : zaze
 * @version : 1.0
 */
public class DateUtil {

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
        return null;
    }
    /**
     * @param timeMillis
     * @param pattern
     * @return
     */
    public static String timeMillisToString(long timeMillis, String pattern){
        return dateToString(new Date(timeMillis), pattern);
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
        return getInteger(date, Calendar.HOUR);
    }
    public static int getMinute(Date date) {
        return getInteger(date, Calendar.MINUTE);
    }
    public static int getSecond(Date date) {
        return getInteger(date, Calendar.SECOND);
    }

    // ----------------  about week ------------------
    /**
     * 获取日期的星期。失败返回null。
     * @param date 日期字符串
     * @param pattern 日期格式
     * @return 星期
     */
    public static Week getWeek(String date, String pattern) {
        return getWeek(stringToDate(date, pattern));
    }
    /**
     * 获取日期的星期。失败返回null。
     * @param date 日期
     * @return 星期
     */
    public static Week getWeek(Date date) {
        Week week = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (weekNumber) {
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
    /**
     * @param date      日期
     * @param dateType  年，月，日...
     * @return
     */
    private static int getInteger(Date date, int dateType) {
        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
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

}
