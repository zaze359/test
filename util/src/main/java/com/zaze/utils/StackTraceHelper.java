package com.zaze.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class StackTraceHelper {

    private static final Set<String> STACK_CALLERS = new HashSet<>();

    static {
        registerStackCaller(StackTraceHelper.class.getName());
    }

    public static void registerStackCaller(String logCaller) {
        STACK_CALLERS.add(logCaller);
    }

    /**
     * @return 直接调用方法的堆栈信息
     */
    public static StackTraceElement getCurrentStackTraceElement() {
        return getStackTraceElement(1);
    }

    /**
     * @return 间接调用的堆栈信息
     */
    public static StackTraceElement getCallerStackTraceElement() {
        StackTraceElement stes[] = new Throwable().getStackTrace();
        int position = 0;
        for (StackTraceElement ste : stes) {
            if (STACK_CALLERS.contains(ste.getClassName())) {
                position++;
            }
        }
        if (position >= stes.length) {
            position = stes.length - 1;
        }
        return stes[position];
    }

    /**
     * @param position 堆栈位置
     * @return 堆栈信息
     */
    public static StackTraceElement getStackTraceElement(int position) {
        return new Throwable().getStackTrace()[position];
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
