package com.zaze.common.base.header;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.Window;

import com.zaze.common.base.BaseActivity;
import com.zaze.common.base.BaseView;
import com.zaze.common.util.ActivityUtil;
import com.zaze.common.util.ViewUtil;
import com.zaze.common.widget.head.BaseHeadView;
import com.zaze.common.widget.head.HeadFace;
import com.zaze.common.widget.head.HeadWidget;
import com.zaze.utils.ZTipUtil;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 22:59 2.0
 */
public abstract class BaseHeaderActivity extends BaseActivity implements BaseView {
    private BaseHeadView headFace;

    // --------------------------------------------------

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (isNeedHead()) {
            initToolBar(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
//        onWindowFocusChanged(true);
//        StatusBarHelper.hideStatusBar(this);
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setStatusBarColor(getResources().getColor(R.color.head_background));
//        }
//        StatusBarHelper.statusBarLightMode(this);
    }

    private void initToolBar(int layoutResID) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        headFace = new HeadWidget(this, layoutResID);
        super.setContentView(headFace.getContainerView());
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
        ZTipUtil.toast(this, getString(msg));
    }

    @Override
    public void showToast(String msg) {
        ZTipUtil.toast(this, msg);
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

    public <T extends View> T findView(int resId) {
        return ViewUtil.findView(this, resId);
    }

    /**
     * 是否需要使用 通用的header
     */
    protected boolean isNeedHead() {
        return true;
    }

    /**
     * @return 返回hearer 操作类
     */
    public HeadFace getHeadWidget() {
        return headFace;
    }

}
