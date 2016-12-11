package com.zaze.aarrepo.commons.log;

import android.support.annotation.IntDef;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class LogLevel {
    public static final int ERROR = 1;
    public static final int WARN = 2;
    public static final int INFO = 3;
    public static final int DEBUG = 4;
    public static final int VERBOSE = 5;

    @IntDef({ERROR, WARN, INFO, DEBUG, VERBOSE})
    public @interface LogAnno {
    }
}
