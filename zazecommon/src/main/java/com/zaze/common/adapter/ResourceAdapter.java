package com.zaze.common.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-30 - 11:04
 */
public interface ResourceAdapter {

    <T extends View> T findView(View parent, int resId);

    int getColor(int resId);

    String getString(int resId, Object... args);

    Drawable getDrawable(int resId);

    Bitmap getBitmap(int resId);
}
