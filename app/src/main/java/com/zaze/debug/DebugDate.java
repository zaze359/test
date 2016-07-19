package com.zaze.debug;

import com.zaze.commons.date.DateUtil;

import java.util.Date;

/**
 * Created by zaze on 16/4/23.
 */
@Deprecated
public class DebugDate extends DebugOut {
    // -----------------------------------
    public static void debugWeek(){
        Date date = new Date();
        out("debugWeek", DateUtil.getWeek(date).getCn());
    }
    public static void debugDateToStr(){
        String time = DateUtil.dateToString(new Date(), "yyyy年MM月dd日 HH点mm分ss秒");
        out("debugDateToStr", time);
    }
    public static void debugStringToDate() {
        String[] tags = {"before", "after"};
        String[] results = new String[tags.length];
        results[0] = "2016-04-23 10:23:42";
        Date date = DateUtil.stringToDate("2016-04-23 10:23:42", "yyyy-MM-dd HH:mm:ss");
        results[1] = date.toString();
        out("debugStringToDate", tags, results);
    }
    public static void debugTimeMillisToStr() {
        String str = DateUtil.timeMillisToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");

        System.out.println("" + str);

    }
    public static void debugTimeMillisToDate() {
        DateUtil.timeMillisToString(System.currentTimeMillis(), "");
    }
    //

}