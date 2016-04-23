package com.zaze.commons.date;

import java.util.Date;

/**
 * Created by zaze on 16/4/23.
 */
public interface TimeFace {
    /**
     * @param dateStr String
     * @param pattern 日期格式
     * @return
     */
    Date stringToDate(String dateStr, String pattern);
    /**
     * @param date    Date 对象
     * @param pattern 日期格式
     * @return 转换特定格式的日期字符串
     */
    String dateToString(Date date, String pattern);
    /**
     * @param timeMillis
     * @param pattern
     * @return
     */
    String timeMillisToString(long timeMillis, String pattern);

}
