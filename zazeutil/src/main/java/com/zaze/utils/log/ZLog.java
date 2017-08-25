package com.zaze.utils.log;


import android.util.Log;

import com.zaze.utils.ZStackTraceHelper;
import com.zaze.utils.ZStringUtil;

import java.util.Locale;

/**
 * Desc :
 *
 * @author zaze
 * @version 2017/3/23 - 下午11:52 1.0
 */
public class ZLog {
    private static ZLogFace logFace;
    private static boolean needStack = true;
    // --------------------------------------------------
    private static boolean E = true;
    private static boolean W = true;
    private static boolean I = true;
    private static boolean D = true;
    private static boolean V = true;

    public static void setLogLevel(@ZLogLevel.LogAnno int level) {
        E = false;
        W = false;
        I = false;
        D = false;
        V = false;
        if (level >= ZLogLevel.ERROR) {
            E = true;
        }
        if (level >= ZLogLevel.WARN) {
            W = true;
        }
        if (level >= ZLogLevel.INFO) {
            I = true;
        }
        if (level >= ZLogLevel.DEBUG) {
            D = true;
        }
        if (level >= ZLogLevel.VERBOSE) {
            V = true;
        }
    }

    public static void setNeedStack(boolean needStack) {
        ZLog.needStack = needStack;
    }

    public static String getTag(StackTraceElement ste) {
        if (needStack) {
            String clazzName = ste.getClassName();
            clazzName = clazzName.substring(clazzName.lastIndexOf(".") + 1);
            return String.format(Locale.getDefault(),
                    "%s.%s(L:%d)", clazzName, ste.getMethodName(), ste.getLineNumber());
        } else {
            return "";
        }
    }

    private static String getStackTrace(String format, Object... args) {
        return ZStringUtil.format("[" + getTag(ZStackTraceHelper.getStackTraceElement(5)) + "] : " + format, args);
    }

    // ----------- V -----------
    public static void v(String tag, String format, Object... args) {
        if (V) {
            if (logFace != null) {
                logFace.v(tag, getStackTrace(format, args));
            } else {
                Log.v(tag, getStackTrace(format, args));
            }
        }
    }

    // ----------- D -----------
    public static void d(String tag, String format, Object... args) {
        if (D) {
            if (logFace != null) {
                logFace.d(tag, getStackTrace(format, args));
            } else {
                Log.d(tag, getStackTrace(format, args));
            }
        }
    }

    // ----------- I -----------
    public static void i(String tag, String format, Object... args) {
        if (I) {
            if (logFace != null) {
                logFace.i(tag, getStackTrace(format, args));
            } else {
                Log.i(tag, getStackTrace(format, args));
            }
        }

    }

    // ----------- W -----------
    public static void w(String tag, String format, Object... args) {
        if (W) {
            if (logFace != null) {
                logFace.w(tag, getStackTrace(format, args));
            } else {
                Log.w(tag, getStackTrace(format, args));
            }
        }
    }

    // ----------- E -----------
    public static void e(String tag, String format, Object... args) {
        if (E) {
            if (logFace != null) {
                logFace.e(tag, getStackTrace(format, args));
            } else {
                Log.e(tag, getStackTrace(format, args));
            }
        }
    }

    // ---------------------------------------------------

    public static void setLogFace(ZLogFace face) {
        logFace = face;
    }

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
}
