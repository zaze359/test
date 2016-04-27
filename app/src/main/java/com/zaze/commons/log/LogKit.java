package com.zaze.commons.log;

import android.util.Log;

import java.util.Locale;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class LogKit {
    private static boolean sendToService = false;
    private static int curLevel = 5;
    //
    private static boolean E = true;
    private static boolean W = true;
    private static boolean I = true;
    private static boolean D = true;
    private static boolean V = true;

    public static void setLogLevel(@LogLevel.LogAnno int level) {
        curLevel = level;
        E = false;
        W = false;
        I = false;
        D = false;
        V = false;
        if (level >= LogLevel.ERROR) {
            E = true;
        }
        if (level >= LogLevel.WARN) {
            W = true;
        }
        if (level >= LogLevel.INFO) {
            I = true;
        }
        if (level >= LogLevel.DEBUG) {
            D = true;
        }
        if (level >= LogLevel.VERBOSE) {
            V = true;
        }
    }

    public static String getTag(StackTraceElement ste) {
        String clazzName = ste.getClassName();
        clazzName = clazzName.substring(clazzName.lastIndexOf(".") + 1);
        return String.format(Locale.getDefault(), "%s.%s(L:%d)", clazzName, ste.getMethodName(), ste.getLineNumber());
    }

    // --------------------------
    public static void v(String msg) {
        if (V) {
            if (msg == null) {
                msg = "";
            }
            String tag = getTag(getStackTraceElement());
            Log.v(tag, msg);
            if (sendToService)
                ServiceLog.s(tag, msg);
        }
    }

    public static void d(String msg) {
        if (D) {
            if (msg == null) {
                msg = "";
            }
            String tag = getTag(getStackTraceElement());
            Log.d(tag, msg);
            if (sendToService)
                ServiceLog.s(tag, msg);
        }
    }

    public static void i(String msg) {
        if (I) {
            if (msg == null) {
                msg = "";
            }
            String tag = getTag(getStackTraceElement());
            Log.i(tag, msg);
            if (sendToService)
                ServiceLog.s(tag, msg);
        }
    }

    public static void w(String msg) {
        if (W) {
            if (msg == null) {
                msg = "";
            }
            String tag = getTag(getStackTraceElement());
            Log.w(tag, msg);
            if (sendToService)
                ServiceLog.w(tag, msg);
        }
    }

    public static void e(String msg) {
        if (E) {
            if (msg == null) {
                msg = "";
            }
            String tag = getTag(getStackTraceElement());
            Log.e(tag, msg);
            if (sendToService)
                ServiceLog.e(tag, msg);
        }
    }

    private static StackTraceElement getStackTraceElement() {
        return StackTraceHelper.getCallerStackTraceElement();
    }

    // ------------------
    public static void closeAllLog() {
        E = false;
        W = false;
        I = false;
        D = false;
        V = false;
    }

    public static void openAllLog() {
        E = true;
        W = true;
        I = true;
        D = true;
        V = true;
    }

    public static void isSendToService(boolean b) {
        sendToService = b;
    }
}
