package com.zaze.aarrepo.commons.base;

import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * view抽象层
 * <p>
 * Created by huangxin
 * 2016-08-10 17:18
 *
 * @version 1.0
 */
public interface BaseView {

    void showProgress();

    void showProgress(String msg);

    void showProgress(String msg, View.OnClickListener onClickListener);


    void hideProgress();

    void showToast(int msg);

    void showToast(String msg);

    void close();

    Context getContext();

    void jumpToOtherUI(Class<?> cls);

    void jumpToOtherUI(Intent intent, Class<?> cls);

    void jumpToOtherUI(Intent intent, Class<?> cls, int code);

    void jumpToOtherUI(Class<?> cls, int code);

    void toFinish();

    void toFinish(Intent intent, int code);


    // -------
    String getString(int res);

    String getString(int res, Object... objs);

    int getColor(int colorRes);

    int getDimen(int id);


}
