package com.zaze.common.base;

import android.content.Context;
import android.content.Intent;
import android.view.View;


/**
 * Description :
 *
 * @author zaze
 * @version 2017/10/19 - 上午11:13 1.0
 */
public interface ZBaseView {

    /**
     * 显示Progress
     */
    void showProgress();

    /**
     * 显示Progress
     *
     * @param msg 信息
     */
    void showProgress(String msg);

    /**
     * 显示Progress
     *
     * @param msg             信息
     * @param onClickListener 点击效果
     */
    void showProgress(String msg, View.OnClickListener onClickListener);

    /**
     * 隐藏Progress
     */
    void hideProgress();
    // --------------------------------------------------
    // --------------------------------------------------

    /**
     * 提示toast
     *
     * @param msg 提示信息
     */
    void showToast(int msg);

    /**
     * 提示toast
     *
     * @param msg 提示信息
     */
    void showToast(String msg);
    // --------------------------------------------------
    // --------------------------------------------------

    /**
     * 退出当前页面
     */
    void finishSelf();

    /**
     * 退出当前页面
     *
     * @param intent     intent
     * @param resultCode resultCode
     */
    void finishSelf(Intent intent, int resultCode);
    // --------------------------------------------------

    /**
     * UI跳转
     *
     * @param cls cls
     */
    void jumpToOtherUI(Class<?> cls);

    /**
     * UI跳转
     *
     * @param intent intent
     * @param cls    cls
     */
    void jumpToOtherUI(Intent intent, Class<?> cls);

    /**
     * UI跳转
     *
     * @param cls        cls
     * @param resultCode resultCode
     */
    void jumpToOtherUI(Class<?> cls, int resultCode);

    /**
     * UI跳转
     *
     * @param intent     intent
     * @param cls        cls
     * @param resultCode resultCode
     */
    void jumpToOtherUI(Intent intent, Class<?> cls, int resultCode);
    // --------------------------------------------------

    /**
     * 获取上下文
     *
     * @return Context
     */
    Context getContext();

    // --------------------------------------------------

    /**
     * 指定资源字符串
     *
     * @param resId resId
     * @return String
     */
    String getString(int resId);

    /**
     * 指定资源字符串
     *
     * @param resId resId
     * @param objs  objs
     * @return String
     */
    String getString(int resId, Object... objs);

    /**
     * 指定资源字符数组
     *
     * @param resId resId
     * @return String[]
     */
    String[] getStringArray(int resId);

    /**
     * Color
     *
     * @param colorRes colorRes
     * @return color int
     */
    int getColor(int colorRes);

    /**
     * Dimen
     *
     * @param resId resId
     * @return dimen int
     */
    int getDimen(int resId);

}
