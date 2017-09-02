package com.zaze.common.base;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.zaze.common.widget.LoadingWidget;
import com.zaze.common.widget.head.BaseHeadView;
import com.zaze.common.widget.head.HeadFace;
import com.zaze.common.widget.head.HeadWidget;
import com.zaze.utils.ZActivityUtil;
import com.zaze.utils.ZTipUtil;
import com.zaze.utils.ZViewUtil;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 22:59 2.0
 */
public abstract class ZBaseActivity extends AppCompatActivity implements ZBaseView {
    private BaseHeadView headFace;
    private LoadingWidget loadProgress;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (isNeedHead()) {
            initToolBar(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
        loadProgress = new LoadingWidget(this);
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
    protected void onDestroy() {
        hideProgress();
        super.onDestroy();
    }

    /**
     * 显示一个普通等待弹窗
     */
    @Override
    public void showProgress() {
        loadProgress.showProgress();
    }

    @Override
    public void showProgress(String msg) {
        loadProgress.setText(msg);
        loadProgress.showProgress();
    }

    @Override
    public void showProgress(String msg, View.OnClickListener onClickListener) {
        showProgress(msg);
        loadProgress.setTextOnClick(onClickListener);
    }

    @Override
    public void hideProgress() {
        loadProgress.dismissProgress();
    }

    @Override
    public void showToast(int msg) {
        ZTipUtil.toast(this, getString(msg));
    }

    @Override
    public void showToast(String msg) {
        ZTipUtil.toast(this, msg);
    }

    @Override
    public void close() {
        ZActivityUtil.finish(this);
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void jumpToOtherUI(Intent intent, Class<?> cls) {
        ZActivityUtil.startActivity(this, cls, intent);
    }

    @Override
    public void jumpToOtherUI(Class<?> cls) {
        ZActivityUtil.startActivity(this, cls);
    }

    @Override
    public void jumpToOtherUI(Class<?> cls, int code) {
        ZActivityUtil.startActivityForResult(this, cls, code);
    }

    @Override
    public void jumpToOtherUI(Intent intent, Class<?> cls, int code) {
        ZActivityUtil.startActivityForResult(this, cls, intent, code);
    }

    @Override
    public void toFinish() {
        finish();
    }

    @Override
    public void toFinish(Intent intent, int code) {
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
        return ZViewUtil.findView(this, resId);
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
