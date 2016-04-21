package com.zaze.util;

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


    


    // ----------------------------------
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
    // ----------------------------------
    /**
     * @param date      日期
     * @param dateType  年，月，日...
     * @return
     */
    private static int get(Date date, int dateType) {
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
