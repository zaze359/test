package com.zaze;

import com.zaze.util.DateUtil;

import java.util.Date;

/**
 * Description :
 * date : 2016-04-21 - 12:26
 *
 * @author : zaze
 * @version : 1.0
 */
public class Main {
    public static void main(String[] args) {
        String time = DateUtil.dateToString(new Date(), "");
        System.out.println(time);
    }
}
