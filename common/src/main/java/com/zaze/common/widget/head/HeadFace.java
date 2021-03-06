package com.zaze.common.widget.head;

import android.app.Activity;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-10 - 15:33
 */
public interface HeadFace {

    /**
     * 设置文本
     *
     * @param resId       resId
     * @param orientation orientation
     * @return HeadFace
     */
    HeadFace setText(@StringRes int resId, @ZOrientation int orientation);

    /**
     * 设置文本
     *
     * @param text        text
     * @param orientation orientation
     * @return HeadFace
     */
    HeadFace setText(String text, @ZOrientation int orientation);

    /**
     * 设置可见图标
     *
     * @param resIcon     resIcon
     * @param orientation orientation
     * @return HeadFace
     */
    HeadFace setIcon(@DrawableRes int resIcon, @ZOrientation int orientation);

    /**
     * 设置监听
     *
     * @param listener    OnClickListener
     * @param orientation orientation
     * @return HeadFace
     */
    HeadFace setOnClickListener(View.OnClickListener listener, @ZOrientation int orientation);

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
    HeadFace setVisibility(int visibility, @ZOrientation int orientation);

    /**
     * 获取指定view
     *
     * @param orientation orientation
     * @return View
     */
    View getView(@ZOrientation int orientation);

}
