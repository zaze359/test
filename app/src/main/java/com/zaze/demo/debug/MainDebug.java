package com.zaze.demo.debug;


import com.zaze.aarrepo.commons.date.DateUtil;

import java.util.TimeZone;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-06-15 - 16:07
 */
public class MainDebug extends DebugOut {
    public static void main(String args[]) {

        long time = System.currentTimeMillis();
        String str = "";
        str = DateUtil.timeMillisToString(time, "yyyy-MM-dd HH:mm:ss");
        out("time before ", str);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        str = DateUtil.timeMillisToString(time, "yyyy-MM-dd HH:mm:ss");
        out("time after ", str);

    }
}
