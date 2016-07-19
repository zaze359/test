package com.zaze.debug;

import com.zaze.commons.date.DateTimeUtil;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Description :
 * @author : ZAZE
 * @version : 2016-06-15 - 16:07
 */
public class DebugDateTime extends DebugOut {

    public static void main(String[] args) {
        debugStartEnd();
        debugWeek();
        debugDateToStr();
        debugStringToStr();
        debugTimeMillisToStr();
        debugBetween();
    }
    // -----------------------------------
    public static void debugStartEnd() {
        String[] tags = {"start", "end"};
        String[] results = new String[tags.length];
        results[0] = DateTimeUtil.getDayStart(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
        results[1] = DateTimeUtil.getDayEnd(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
        out("debugStartEnd", tags, results);
    }

    public static void debugWeek() {
        out("debugWeek", DateTimeUtil.getWeek(System.currentTimeMillis()).getCn());
    }

    public static void debugDateToStr() {
        String time = DateTimeUtil.dateToString(new Date(), "yyyy年MM月dd日 HH点mm分ss秒");
        out("debugDateToStr", time);
    }

    public static void debugStringToStr() {
        String[] tags = {"before", "after"};
        String[] results = new String[tags.length];
        results[0] = "2016-04-23 10:23:42";
        results[1] = DateTimeUtil.stringToString(results[0], "yyyy-MM-dd HH:mm:ss", "yyyy MM dd hh-mm-ss");
        out("debugStringToStr", tags, results);
    }

    public static void debugTimeMillisToStr() {
        String str = DateTimeUtil.timeMillisToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
        out("debugTimeMillisToStr", str);

    }

    public static void debugBetween() {
        DateTime nowTime = new DateTime();
        DateTime nextTime = nowTime.plusDays(3);
        out("debugBetween day", String.valueOf(DateTimeUtil.offsetDay(nowTime, nextTime)));
//        out("debugBetween hour", String.valueOf(DateTimeUtil.offsetHour(nowTime, nextTime)));
    }
    //

}