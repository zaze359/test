package com.zaze.common.widget.head;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-07-12 - 17:26
 */
@IntDef({ZOrientation.LEFT, ZOrientation.CENTER, ZOrientation.RIGHT})
@Retention(RetentionPolicy.CLASS)
public @interface ZOrientation {
    int LEFT = 2;
    int RIGHT = 3;
    int CENTER = 5;
}
