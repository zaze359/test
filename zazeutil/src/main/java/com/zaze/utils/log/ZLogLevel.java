package com.zaze.utils.log;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
@IntDef({ZLogLevel.ERROR, ZLogLevel.WARN, ZLogLevel.INFO, ZLogLevel.DEBUG, ZLogLevel.VERBOSE})
@Retention(RetentionPolicy.SOURCE)
public @interface ZLogLevel {
    int ERROR = 1;
    int WARN = 2;
    int INFO = 3;
    int DEBUG = 4;
    int VERBOSE = 5;
}