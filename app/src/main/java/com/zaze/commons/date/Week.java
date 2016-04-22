package com.zaze.commons.date;

/**
 * Description :
 * date : 2016-04-22 - 13:46
 *
 * @author : zaze
 * @version : 1.0
 */
public enum Week {
    MONDAY("星期一", "周一", "Monday", "Mon.", 1),
    TUESDAY("星期二", "周二", "Tuesday", "Tues.", 2),
    WEDNESDAY("星期三", "周三", "Wednesday", "Wed.", 3),
    THURSDAY("星期四", "周四", "Thursday", "Thur.", 4),
    FRIDAY("星期五", "周五", "Friday", "Fri.", 5),
    SATURDAY("星期六", "周六", "Saturday", "Sat.", 6),
    SUNDAY("星期日", "周日", "Sunday", "Sun.", 7);

    String cn;
    String cn_s;
    String en;
    String en_s;
    int number;

    Week(String cn, String cn_s, String en, String en_s, int number) {
        this.cn = cn;
        this.cn_s = cn_s;
        this.en = en;
        this.en_s = en_s;
        this.number = number;
    }
}
