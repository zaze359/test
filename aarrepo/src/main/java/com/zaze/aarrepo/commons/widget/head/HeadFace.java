package com.zaze.aarrepo.commons.widget.head;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-10 - 15:33
 */
public interface HeadFace {
    int LEFT = 2;
    int CENTER = 5;
    int RIGHT = 3;

    @IntDef({LEFT, CENTER, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @interface Orientation {

    }


    /**
     * 设置文本
     *
     * @param resId       resId
     * @param orientation orientation
     * @return HeadFace
     */
    HeadFace setText(int resId, @Orientation int orientation);

    /**
     * 设置文本
     *
     * @param text        text
     * @param orientation orientation
     * @return HeadFace
     */
    HeadFace setText(String text, @Orientation int orientation);

    /**
     * 设置可见图标
     *
     * @param resIcon     resIcon
     * @param orientation orientation
     * @return HeadFace
     */
    HeadFace setIcon(int resIcon, @Orientation int orientation);

    /**
     * 设置监听
     *
     * @param listener    OnClickListener
     * @param orientation orientation
     * @return HeadFace
     */
    HeadFace setOnClickListener(View.OnClickListener listener, @Orientation int orientation);

    /**
     * 设置返回监听
     *
     * @param activity activity
     * @return HeadFace
     */
    HeadFace setBackClickListener(Activity activity);

    /**
     * 设置可见隐藏
     *
     * @param visibility  visibility
     * @param orientation orientation
     * @return HeadFace
     */
    HeadFace setVisibility(int visibility, @Orientation int orientation);

    /**
     * @param orientation orientation
     * @return View
     */
    View getView(@Orientation int orientation);

}
