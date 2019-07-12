package com.zaze.utils;

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

    /**
     * @return 直接调用方法的堆栈信息
     */
    public static StackTraceElement getCurrentStackTraceElement() {
        return getStackTraceElement(3);
    }

    /**
     * @return 间接调用的堆栈信息
     */
    public static StackTraceElement getCallerStackTraceElement() {
        return getStackTraceElement(4);
    }

    /**
     * @param position 堆栈位置
     * @return 堆栈信息
     */
    public static StackTraceElement getStackTraceElement(int position) {
        return Thread.currentThread().getStackTrace()[position];
    }

    /**
     * @param e 异常
     * @return 获取报错的堆栈信息
     */
    public static String getErrorMsg(Throwable e) {
        if (e != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter, false);
            try {
                e.printStackTrace(printWriter);
                printWriter.flush();
                return stringWriter.toString();
            } finally {
                printWriter.close();
            }
        } else {
            return "";
        }
    }
}
