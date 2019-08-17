package com.zaze.common.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zaze.common.util.ActivityUtil;
import com.zaze.utils.ZTipUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 22:59 2.0
 */
@Deprecated
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

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
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ZLog.v(ZTag.TAG_DEBUG, "onActivityResult : " + this.getClass().getName());
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

}
