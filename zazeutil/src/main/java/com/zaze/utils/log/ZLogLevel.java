package com.zaze.utils.log;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ZLogLevel {
    int NULL = 0;
    int ERROR = 1;
    int WARN = 2;
    int INFO = 3;
    int DEBUG = 4;
    int VERBOSE = 5;
}
