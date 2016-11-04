package com.zaze.component.toolbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.zaze.R;
import com.zaze.component.progress.ProgressFragment;
import com.zaze.component.fragments.TempFragment;
import com.zz.library.commons.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar_demo);
        ButterKnife.bind(this);
        toolBarDemoBar.setTitle("ToolBarDemo");
        toolBarDemoBar.setSubtitle("SubTitle");
        setSupportActionBar(toolBarDemoBar);
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
}

