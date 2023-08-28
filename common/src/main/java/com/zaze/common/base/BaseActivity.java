package com.zaze.common.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zaze.common.util.ActivityUtil;
import com.zaze.utils.ToastUtil;
import com.zaze.utils.log.ZLog;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 22:59 2.0
 */
@Deprecated
public abstract class BaseActivity extends AbsThemeActivity implements BaseView {
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
//        onWindowFocusChanged(true);
//        StatusBarHelper.hideStatusBar(this);
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setStatusBarColor(getResources().getColor(R.color.head_background));
//        }
//        StatusBarHelper.statusBarLightMode(this);
    }

//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        // 沉浸式
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }


    @Override
    public void showToast(int msg) {
        ToastUtil.toast(this, getString(msg));
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.toast(this, msg);
    }

    @Override
    public void finishSelf() {
        ActivityUtil.finish(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void jumpToOtherUI(Intent intent, Class<?> cls) {
        ActivityUtil.startActivity(this, cls, intent);
    }

    @Override
    public void jumpToOtherUI(Class<?> cls) {
        ActivityUtil.startActivity(this, cls);
    }

    @Override
    public void jumpToOtherUI(Class<?> cls, int code) {
        ActivityUtil.startActivityForResult(this, cls, code);
    }

    @Override
    public void jumpToOtherUI(Intent intent, Class<?> cls, int code) {
        ActivityUtil.startActivityForResult(this, cls, intent, code);
    }

    @Override
    public void finishSelf(Intent intent, int code) {
        setResult(code, intent);
        finish();
    }

    // --------------------------------------------------
    @Override
    public int getDimen(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    @Override
    public String[] getStringArray(int res) {
        return getResources().getStringArray(res);
    }

    // --------------------------------------------------

}
