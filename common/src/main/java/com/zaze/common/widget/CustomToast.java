package com.zaze.common.widget;

import android.widget.Toast;

import com.zaze.common.base.BaseApplication;
import com.zaze.common.thread.ThreadPlugins;

public class CustomToast {

    /**
     * Toast
     *
     * @param resId 消息resId
     */
    public static void showToast(int resId) {
        showToast(BaseApplication.getInstance().getString(resId));
    }

    /**
     * Toast
     *
     * @param msg 消息
     */
    public static void showToast(String msg) {
        Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Toast
     *
     * @param resId 消息resId
     */
    public static void postShowToast(final int resId) {
        ThreadPlugins.runInUIThread(new Runnable() {
            @Override
            public void run() {
                showToast(resId);
            }
        }, 0);
    }

    /**
     * Toast
     *
     * @param msg 消息
     */
    public static void postShowToast(final String msg) {
        ThreadPlugins.runInUIThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
            }
        }, 0);
    }

}
