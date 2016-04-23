package com.zaze.commons.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by zaze on 16/4/23.
 */
public class TimeUtil implements TimeFace {

    private static TimeFace timeFace;
    public static TimeFace getInstance() {
        if(timeFace == null) {
            timeFace = new TimeUtil();
        }
        return timeFace;
    }

    @Override
    public Date stringToDate(String dateStr, String pattern) {
        return DateTime.parse(dateStr, getFormat(pattern)).toDate();
    }

    @Override
    public String dateToString(Date date, String pattern) {
        return null;
    }

    @Override
    public String timeMillisToString(long timeMillis, String pattern) {
        return null;
    }


    private DateTimeFormatter getFormat(String pattern) {
        return DateTimeFormat.forPattern(pattern);
    }

    private DateTime getDateTime() {
        return new DateTime();
    }



}
