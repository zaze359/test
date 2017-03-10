package com.zaze.aarrepo.commons.cache;

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
public class DataLevel {
    // 低优先级
    public static final int LOW_LEVEL_DATA = 100;
    public static final int LOW_LEVEL_BITMAP = 101;
    // 普通优先级
    public static final int DATA = 1000;
    public static final int BITMAP = 1001;
    // 高优先级
    public static final int HIGH_LEVEL_DATA = 10000;
    public static final int HIGH_LEVEL_BITMAP = 10001;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOW_LEVEL_DATA, LOW_LEVEL_BITMAP, 
            DATA, BITMAP, 
            HIGH_LEVEL_DATA, HIGH_LEVEL_BITMAP})
    public @interface DataAnno {
        
    }
    
}