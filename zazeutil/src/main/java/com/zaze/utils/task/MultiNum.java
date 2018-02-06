package com.zaze.utils.task;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-01-31 - 16:09
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({MultiNum.DEFAULT, MultiNum.KEEP, MultiNum.MIN, MultiNum.MAX, MultiNum.TWO, MultiNum.THREE, MultiNum.FOUR, MultiNum.FIVE})
public @interface MultiNum {
    int DEFAULT = -1;
    int KEEP = -2;
    int MAX = -3;

    int MIN = 1;
    int TWO = 2;
    int THREE = 3;
    int FOUR = 4;
    int FIVE = 5;
}
