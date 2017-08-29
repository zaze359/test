package com.zaze.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class ZViewUtil {
    public static <T extends View> T findView(View parent, int resId) {
        T view = (T) parent.findViewById(resId);
        return view;
    }

    public static <T extends View> T findView(Activity activity, int resId) {
        T view = (T) activity.findViewById(resId);
        return view;
    }
    
    //
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    @IntDef({LEFT, TOP, RIGHT, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Oritation {

    }

    public static void setCompoundDrawablesWithIntrinsicBounds(Context context, int position, TextView view, @Oritation int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        switch (position) {
            case LEFT:
                view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                break;
            case TOP:
                view.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                break;
            case RIGHT:
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                break;
            case BOTTOM:
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                break;
            default:
                break;
        }
    }

}
