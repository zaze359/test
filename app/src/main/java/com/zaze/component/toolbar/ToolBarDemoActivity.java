package com.zaze.component.toolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.zaze.R;
import com.zaze.component.fragments.TempFragment;
import com.zaze.component.progress.ProgressFragment;
import com.zz.library.commons.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-10-29 - 00:41
 */
public class ToolBarDemoActivity extends BaseActivity {

    @Bind(R.id.tool_bar_demo_bar)
    Toolbar toolBarDemoBar;
    @Bind(R.id.tool_bar_demo_tab)
    SmartTabLayout toolBarDemoTab;
    @Bind(R.id.tool_bar_demo_viewpager)
    ViewPager toolBarDemoViewpager;
    @Bind(R.id.tool_bar_demo_btn_1)
    Button toolBarDemoBtn1;
    @Bind(R.id.tool_bar_demo_btn_2)
    Button toolBarDemoBtn2;
    @Bind(R.id.tool_bar_demo_btn_3)
    Button toolBarDemoBtn3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar_demo);
        ButterKnife.bind(this);
        //
        toolBarDemoBar.setTitle("ToolBarDemo");
        toolBarDemoBar.setSubtitle("SubTitle");
        setSupportActionBar(toolBarDemoBar);
        //
        initTab();
    }

    private void initTab() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Tab One", ProgressFragment.class, getBundle("Tab One Content"))
                .add("Tab Two", TempFragment.class, getBundle("Tab Two Content"))
                .create()
        );
        toolBarDemoViewpager.setAdapter(adapter);
        toolBarDemoTab.setViewPager(toolBarDemoViewpager);
    }

    public Bundle getBundle(String msg) {
        Bundle bundle = new Bundle();
        bundle.putString("title", msg);
        return bundle;
    }

    // ----------------  状态栏样式 ----------------

    @OnClick(R.id.tool_bar_demo_btn_1)
    public void function1() {   // 隐藏状态栏
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @OnClick(R.id.tool_bar_demo_btn_2)
    public void function2() {   // 透明
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @OnClick(R.id.tool_bar_demo_btn_3)
    public void function3() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 沉浸式
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
    }


}

