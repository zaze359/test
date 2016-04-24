package com.zaze;

import com.zaze.debug.DebugDateTime;

/**
 * Description :
 * date : 2016-04-21 - 12:26
 *
 * @author : zaze
 * @version : 1.0
 */
public class Main {
    public static void main(String[] args) {
//        DebugDate.debugWeek();
//        DebugDate.debugDateToStr();
//        DebugDate.debugStringToDate();
//        DebugDate.debugTimeMillisToStr();

        DebugDateTime.debugStartEnd();
        DebugDateTime.debugWeek();
        DebugDateTime.debugDateToStr();
        DebugDateTime.debugStringToStr();
        DebugDateTime.debugTimeMillisToStr();
        DebugDateTime.debugBetween();
    }
}
