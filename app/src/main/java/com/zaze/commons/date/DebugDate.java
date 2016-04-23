package com.zaze.commons.date;

import java.util.Date;

/**
 * Created by zaze on 16/4/23.
 */
public class DebugDate {
    //
    public static void debugWeek(){
        Date date = new Date();
        out("debugWeek", DateUtil.getWeek(date).cn);
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

    // ---------    print ------
    private static void out(String tag, String result) {
        System.out.println("\n---------------------------------------------");
        System.out.println(String.format("<!--   %s   -->", tag));
        outTag("result", result);
        System.out.println("---------------------------------------------");

    }

    private static void out(String func, String tag[], String result[]) {
        System.out.println("\n---------------------------------------------");
        System.out.println(String.format("<!--   %s   -->", func));
        for (int i = 0; i < tag.length; i ++) {
            outTag(tag[i], result[i]);
        }
        System.out.println("---------------------------------------------");

    }
    private static void outTag(String tag, String result) {
        System.out.println(String.format("<%s = %s>", tag, result));
    }
}