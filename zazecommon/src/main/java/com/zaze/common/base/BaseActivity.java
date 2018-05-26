package com.zaze.common.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
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
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 22:59 2.0
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    private BaseHeadView headFace;
    private LoadingWidget loadProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ZLog.v(ZTag.TAG_DEBUG, "onCreate : " + this.getClass().getName());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        ZLog.v(ZTag.TAG_DEBUG, "onNewIntent : " + this.getClass().getName());
        super.onNewIntent(intent);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        ZLog.v(ZTag.TAG_DEBUG, "onPostCreate : " + this.getClass().getName());
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        ZLog.v(ZTag.TAG_DEBUG, "onRestart : " + this.getClass().getName());
        super.onRestart();
    }

    @Override
    protected void onStart() {
        ZLog.v(ZTag.TAG_DEBUG, "onStart : " + this.getClass().getName());
        super.onStart();
    }

    @Override
    protected void onResume() {
        ZLog.v(ZTag.TAG_DEBUG, "onResume : " + this.getClass().getName());
        super.onResume();
    }

    @Override
    protected void onResumeFragments() {
        ZLog.v(ZTag.TAG_DEBUG, "onResumeFragments : " + this.getClass().getName());
        super.onResumeFragments();
    }

    @Override
    protected void onPause() {
        ZLog.v(ZTag.TAG_DEBUG, "onPause : " + this.getClass().getName());
        super.onPause();
    }

    @Override
    protected void onStop() {
        ZLog.v(ZTag.TAG_DEBUG, "onStop : " + this.getClass().getName());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ZLog.v(ZTag.TAG_DEBUG, "onDestroy : " + this.getClass().getName());
        hideProgress();
        super.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ZLog.v(ZTag.TAG_DEBUG, "onSaveInstanceState : " + this.getClass().getName());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        ZLog.v(ZTag.TAG_DEBUG, "onSaveInstanceState : " + this.getClass().getName());
        super.onRestoreInstanceState(savedInstanceState);
    }

    // --------------------------------------------------

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
    public void finishSelf() {
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
