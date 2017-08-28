package com.zaze.utils.cache;

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
@Retention(RetentionPolicy.SOURCE)
@IntDef({DataLevel.LOW_LEVEL_DATA, DataLevel.LOW_LEVEL_BITMAP,
        DataLevel.DATA, DataLevel.BITMAP,
        DataLevel.HIGH_LEVEL_DATA, DataLevel.HIGH_LEVEL_BITMAP})
public @interface DataLevel {
    // 低优先级
    int LOW_LEVEL_DATA = 100;
    int LOW_LEVEL_BITMAP = 101;
    // 普通优先级
    int DATA = 1000;
    int BITMAP = 1001;
    // 高优先级
    int HIGH_LEVEL_DATA = 10000;
    int HIGH_LEVEL_BITMAP = 10001;


}