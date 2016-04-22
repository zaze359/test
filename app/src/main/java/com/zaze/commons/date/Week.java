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

    String name_cn;
    String name_cn_s;
    String name_en;
    String name_en_s;
    int number;

    Week(String name_cn, String name_cn_s, String name_en, String name_en_s, int number) {
        this.name_cn = name_cn;
        this.name_cn_s = name_cn_s;
        this.name_en = name_en;
        this.name_en_s = name_en_s;
        this.number = number;
    }

    public String getChName() {
        return name_cn;
    }

    public String getEnName() {
        return name_en;
    }

    public String getEnShortName() {
        return name_en_s;
    }

    public int getNumber() {
        return number;
    }
}
