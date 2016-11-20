package com.zz.library.commons.date;

import com.zz.library.commons.log.LogKit;

import org.joda.time.DateTime;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-20 - 02:13
 */
public class DateTimeUtilTest {
    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void offsetDay() throws Exception {
        DateTime nowTime = new DateTime();
        DateTime nextTime = nowTime.plusDays(3);
        LogKit.v("%d", DateTimeUtil.offsetDay(nowTime, nextTime));
    }

    @org.junit.Test
    public void offsetTimeMillis() throws Exception {

    }

    @org.junit.Test
    public void nowToEnd() throws Exception {
        LogKit.v(DateTimeUtil.getDayStart(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
    }

    @org.junit.Test
    public void nowToStart() throws Exception {
        LogKit.v(DateTimeUtil.getDayEnd(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
    }

    @org.junit.Test
    public void getDayEnd() throws Exception {

    }

    @org.junit.Test
    public void getDayEnd1() throws Exception {

    }

    @org.junit.Test
    public void getDayStart() throws Exception {

    }

    @org.junit.Test
    public void getDayStart1() throws Exception {

    }

    @org.junit.Test
    public void timeMillisToString() throws Exception {

    }

    @org.junit.Test
    public void stringToString() throws Exception {

    }

    @org.junit.Test
    public void dateToString() throws Exception {

    }

    @org.junit.Test
    public void dateToString1() throws Exception {

    }

    @org.junit.Test
    public void stringToDate() throws Exception {

    }

    @org.junit.Test
    public void timeMillisToDate() throws Exception {

    }

    @org.junit.Test
    public void getWeek() throws Exception {

    }

    @org.junit.Test
    public void getWeek1() throws Exception {

    }

    @org.junit.Test
    public void getWeek2() throws Exception {

    }

    @org.junit.Test
    public void getEnd() throws Exception {

    }

    @org.junit.Test
    public void getEnd1() throws Exception {

    }

    @org.junit.Test
    public void getStart() throws Exception {

    }

    @org.junit.Test
    public void getStart1() throws Exception {

    }

}