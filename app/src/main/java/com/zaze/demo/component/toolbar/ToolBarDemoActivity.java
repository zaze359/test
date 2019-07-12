package com.zaze.demo.component.toolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-10-29 - 00:41
 */
public class ToolBarDemoActivity extends BaseActivity {

    Toolbar toolBarDemoBar;
    ViewPager toolBarDemoViewpager;
    Button toolBarDemoBtn1;
    Button toolBarDemoBtn2;
    Button toolBarDemoBtn3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_bar_activity);
        toolBarDemoBar = findViewById(R.id.tool_bar_demo_bar);
        toolBarDemoViewpager = findViewById(R.id.tool_bar_demo_viewpager);
        toolBarDemoBtn1 = findViewById(R.id.tool_bar_demo_btn_1);
        toolBarDemoBtn2 = findViewById(R.id.tool_bar_demo_btn_2);
        toolBarDemoBtn3 = findViewById(R.id.tool_bar_demo_btn_3);

        //
        toolBarDemoBar.setTitle("ToolBarDemo");
        toolBarDemoBar.setSubtitle("SubTitle");
        setSupportActionBar(toolBarDemoBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initTab();
        toolBarDemoBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function1();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initTab() {
//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                getSupportFragmentManager(), FragmentPagerItems.with(this)
//                .add("Tab One", ProgressFragment.class, getBundle("Tab One Content"))
//                .add("Tab Two", TempFragment.class, getBundle("Tab Two Content"))
//                .create()
//        );
//        toolBarDemoViewpager.setAdapter(adapter);
//        toolBarDemoTab.setViewPager(toolBarDemoViewpager);
    }

    public Bundle getBundle(String msg) {
        Bundle bundle = new Bundle();
        bundle.putString("title", msg);
        return bundle;
    }

    // ----------------  状态栏样式 ----------------
    public void reset() {
//        View decorView = getWindow().getDecorView();
//        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(option);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
    }

    public void function1() {
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN; // 下拉就显示 并且不会自动恢复
//        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(option);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    public void function2() {   // 透明
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
    }

    public void function3() {
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
//        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
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

