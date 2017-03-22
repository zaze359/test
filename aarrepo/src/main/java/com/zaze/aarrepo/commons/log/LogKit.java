package com.zaze.aarrepo.commons.log;


import android.util.Log;

import com.zaze.aarrepo.utils.StringUtil;

import java.util.Locale;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class LogKit {
    private static LogFace logFace;
    private static int curLevel = 5;
    // --------------------------------------------------
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
        return String.format(Locale.getDefault(),
                "%s.%s(L:%d)", clazzName, ste.getMethodName(), ste.getLineNumber());
    }

    // ----------- V -----------
    public static void v(String format, Object... args) {
        String tag = getTag(StackTraceHelper.getCallerStackTraceElement());
        v(tag, StringUtil.format(format, args));
    }

    public static void v(String msg) {
        String tag = getTag(StackTraceHelper.getCallerStackTraceElement());
        v(tag, msg);
    }

    private static void v(String tag, String msg) {
        if (V) {
            if (msg == null) {
                msg = "";
            }
            if (logFace != null) {
                logFace.v(tag, msg);
            } else {
                Log.v(tag, msg);
            }
        }
    }

    // ----------- D -----------
    public static void d(String format, Object... args) {
        String tag = getTag(StackTraceHelper.getCallerStackTraceElement());
        d(tag, StringUtil.format(format, args));
    }

    public static void d(String msg) {
        String tag = getTag(StackTraceHelper.getCallerStackTraceElement());
        d(tag, msg);
    }

    private static void d(String tag, String msg) {
        if (D) {
            if (msg == null) {
                msg = "";
            }
            if (logFace != null) {
                logFace.d(tag, msg);
            } else {
                Log.d(tag, msg);
            }
        }
    }

    // ----------- I -----------
    public static void i(String format, Object... args) {
        String tag = getTag(StackTraceHelper.getCallerStackTraceElement());
        i(tag, StringUtil.format(format, args));

    }

    public static void i(String msg) {
        String tag = getTag(StackTraceHelper.getCallerStackTraceElement());
        i(tag, msg);
    }

    private static void i(String tag, String msg) {
        if (I) {
            if (msg == null) {
                msg = "";
            }
            if (logFace != null) {
                logFace.i(tag, msg);
            } else {
                Log.i(tag, msg);
            }
        }

    }

    // ----------- W -----------
    public static void w(String format, Object... args) {
        String tag = getTag(StackTraceHelper.getCallerStackTraceElement());
        w(tag, StringUtil.format(format, args));
    }

    public static void w(String msg) {
        String tag = getTag(StackTraceHelper.getCallerStackTraceElement());
        w(tag, msg);
    }

    private static void w(String tag, String msg) {
        if (W) {
            if (msg == null) {
                msg = "";
            }
            if (logFace != null) {
                logFace.w(tag, msg);
            } else {
                Log.w(tag, msg);
            }
        }

    }

    // ----------- E -----------
    public static void e(String format, Object... args) {
        String tag = getTag(StackTraceHelper.getCallerStackTraceElement());
        e(tag, StringUtil.format(format, args));
    }

    public static void e(String msg) {
        String tag = getTag(StackTraceHelper.getCallerStackTraceElement());
        e(tag, msg);
    }

    private static void e(String tag, String msg) {
        if (E) {
            if (msg == null) {
                msg = "";
            }
            if (logFace != null) {
                logFace.e(tag, msg);
            } else {
                Log.e(tag, msg);
            }
        }
    }

    // ---------------------------------------------------

    public static void setLogFace(LogFace face) {
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
