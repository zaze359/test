package com.example;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by zaze on 16/4/24.
 */
public class Text {
    public static void main(String[] arg) {
        DateTime dateTime = new DateTime(new Date());
        System.out.print(dateTime.toString());
    }
}
