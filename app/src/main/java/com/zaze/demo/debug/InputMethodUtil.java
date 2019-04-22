package com.zaze.demo.debug;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2015-10-23 - 11:20
 */
public class InputMethodUtil {
    /**
     * 显示键盘
     *
     * @param context context
     */
    public static void show(Context context) {
        if (context == null) {
            return;
        }
        //显示键盘
        InputMethodManager imm = getInputMethodManager(context);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param context context
     * @param view    获取焦点的view
     */
    public static void dismiss(Context context, View view) {
        if (context == null || view == null) {
            return;
        }
        InputMethodManager imm = getInputMethodManager(context);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param context context
     */
    public static void dismiss(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                return;
            }
            InputMethodManager imm = getInputMethodManager(context);
            try {
                View focusView = activity.getCurrentFocus();
                if (focusView != null && imm != null) {
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断键盘是否弹出
     *
     * @param context context
     * @return true/false
     */
    public static boolean isActive(Context context) {
        InputMethodManager imm = getInputMethodManager(context);
        if (imm == null) {
            return false;
        }
        return imm.isActive();
    }


    /**
     * 获取InputMethodManager
     *
     * @param context context
     * @return InputMethodManager
     */
    private static @Nullable
    InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
