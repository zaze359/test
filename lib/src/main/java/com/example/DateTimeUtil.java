package com.example;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Tom on 15/12/16.
 */
public class DateTimeUtil {
    
    
    public static long getGMT8TimeMillis() {
        return System.currentTimeMillis() + 28800000l;
    }
    
    private static void setGMTTimeZone() { // 服务器时间已经是东八区 设置为GMT 防止转换时多加了时区
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }
    
    /**
     * 根据日期获取当天的0点时间
     * @param timeMillis
     * @return
     */
    public static long getStartTime(long timeMillis) {
        setGMTTimeZone();
        timeMillis = getStamp(timeMillis);
        Calendar day = Calendar.getInstance();
        day.setTime(new Date(timeMillis));
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        return day.getTime().getTime();
    }
    
    /**
     * 根据日期获取周几
     * @param date
     * @return
     */
    public static String getWeekDay(String date) {
        setGMTTimeZone();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] weeks = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        try {
            c.setTime(format.parse(date));
            int dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            return weeks[dayForWeek];
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * 根据日期获取周几
     *
     * @param date
     * @return
     */
    public static String getWeekDay(long date) {
        setGMTTimeZone();
        date = getStamp(date);
        Calendar c = Calendar.getInstance();
        String[] weeks = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        c.setTime(new Date(date));
        int dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        return weeks[dayForWeek];
    }

    /**
     * 根据日期获取月日
     * @param date
     * @return
     */
    public static String getMonthDay(long date) {
        setGMTTimeZone();
        date = getStamp(date);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd");
        Timestamp now = new Timestamp(date);
        return df.format(now).replace("-",".");
    }

    /**
     * 根据日期获取月日
     *
     * @param date
     * @return
     */
    public static String getMonthDay(String date) {
        setGMTTimeZone();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(format.parse(date));

            return c.MONTH + "." + c.DAY_OF_MONTH;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 根据日期获取当前时间
     *
     * @return
     */
    public static String getDay(long date) {
        setGMTTimeZone();
        String str = getDate(date);
        if (str.contains(getYestoryDate()))
            return "昨天";
        if (str.contains(getTodayDate()))
            return "今天";
        String[] strs = str.split("-");
        String[] datas = new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        return strs[2] + "   " + datas[Integer.parseInt(strs[1]) - 1];
    }

    public static String getNowTime(){
        return getNowTime(System.currentTimeMillis());
    }

    public static String getNowTime(long time){
        time = getStamp(time);
        SimpleDateFormat df  = new SimpleDateFormat("HH:mm");
        Timestamp now = new Timestamp(time);
        return df.format(now);
    }

    /** 获取使用时间 */
    public static String getUsetime(long time){
        SimpleDateFormat df  = new SimpleDateFormat("mm:ss");
        Timestamp now = new Timestamp(time);
        return df.format(now);
    }

    /** 获取使用时间 */
    public static Long getUsetime(String time){
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");
        ParsePosition pos = new ParsePosition(0);
        return df.parse(time, pos).getTime();
    }

    /**
     * 得到昨天的日期
     * @return
     */
    private static String getYestoryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /** 时间戳转换为日期 */
    public static String getDate(long time) {
        time = getStamp(time);
        SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd");

        Timestamp now = new Timestamp(time);
        String str = df.format(now);
        return str;
    }

    /** 时间戳转换为时间 */
    public static String getTime(long time) {
        time = getStamp(time);
        SimpleDateFormat df =  new SimpleDateFormat("HH:mm:ss");

        Timestamp now = new Timestamp(time);
        String str = df.format(now);
        return str;
    }

    /** 时间戳转换为日期时间 */
    public static String getDateTime(long time) {
        time = getStamp(time);
        SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Timestamp now = new Timestamp(time);
        String str = df.format(now);
        return str;
    }

    public static String addDay(String date, int day){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(format.parse(date));
            c.set(c.DAY_OF_MONTH,
                    c.get(Calendar.DAY_OF_MONTH) + day);
            date = c.get(Calendar.YEAR) + "-"
                    + (c.get(Calendar.MONTH) + 1) + "-"
                    + c.get(Calendar.DAY_OF_MONTH);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String addMonth(String date, int month){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(format.parse(date));
            c.set(c.MONTH,
                    c.get(Calendar.MONTH) + month);
            date = c.get(Calendar.YEAR) + "-"
                    + (c.get(Calendar.MONTH) + 1) + "-"
                    + c.get(Calendar.DAY_OF_MONTH);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 得到今天的日期
     *
     * @return
     */
    private static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    private static long getStamp(long time){
        int length = (time+"").length();
        for(int i = length;i<13;i++){
            time = time*10;
        }
        return time;
    }
}
