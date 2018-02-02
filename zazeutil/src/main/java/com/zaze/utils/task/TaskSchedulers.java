package com.zaze.utils.task;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 15:16
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TaskSchedulers.SYNC, TaskSchedulers.ASYNC, TaskSchedulers.ASYNC_AUTO, TaskSchedulers.ASYNC_MULTI})
public @interface TaskSchedulers {

    /**
     * 同步单次执行
     */
    int SYNC = 0;

    /**
     * 异步单次执行
     */
    int ASYNC = 1;
    /**
     * 异步多个单次执行
     */
    int ASYNC_MULTI = 2;
    /**
     * 异步自动依序执行
     */
    int ASYNC_AUTO = 3;


}
