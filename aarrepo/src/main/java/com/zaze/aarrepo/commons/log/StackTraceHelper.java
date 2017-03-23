package com.zaze.aarrepo.commons.log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class StackTraceHelper {

    public static StackTraceElement getCurrentStackTraceElement() {
        return getStackTraceElement(3);
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return getStackTraceElement(4);
    }

    public static StackTraceElement getStackTraceElement(int position) {
        return Thread.currentThread().getStackTrace()[position];
    }

    /**
     * @param e
     * @return 获取报错的堆栈信息
     */
    public static String getErrorMsg(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

}
