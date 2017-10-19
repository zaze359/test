package com.zaze.common.widget.head;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-07-12 - 17:26
 */
@IntDef({ZOrientation.LEFT, ZOrientation.CENTER, ZOrientation.RIGHT})
@Retention(RetentionPolicy.SOURCE)
public @interface ZOrientation {
    int LEFT = 2;
    int CENTER = 5;
    int RIGHT = 3;
}
