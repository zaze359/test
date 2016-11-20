package com.zz.library.commons.date;

public class DateTime {
    public static final long SECOND = 1000L;
    public static final long MINUTE = 60000L;
    public static final long HOURE = 3600000L;
    public static final long DAY = 86400000L;
    public static final long WEEK = 604800000L;
    public static final long HALF_YEAR = 110376000000L;
    public static final long YEAR = 220752000000L;

    public DateTime() {
    }

    public @interface DateAnno {
    }
}