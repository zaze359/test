package com.zaze;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.zaze.aarrepo.commons.base.ZBaseActivity;
import com.zaze.aarrepo.commons.base.ZBaseFragment;
import com.zaze.aarrepo.commons.log.LogKit;
import com.zaze.aarrepo.commons.task.TaskCallback;
import com.zaze.aarrepo.commons.task.TaskEntity;
import com.zaze.aarrepo.commons.task.TaskFilterThread;
import com.zaze.aarrepo.utils.JsonUtil;
import com.zaze.component.table.ui.TableFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends ZBaseActivity {

    @Bind(R.id.main_toolbar)
    Toolbar mainToolbar;
    @Bind(R.id.main_tab)
    SmartTabLayout mainTab;
    @Bind(R.id.main_viewpager)
    ViewPager mainViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //
        initToolBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
            getWindow().setExitTransition(explode);
        }
        //
        List<ZBaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(TableFragment.newInstance("1"));
        mainViewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList));
        mainTab.setViewPager(mainViewpager);
    }

    private void initToolBar() {
        mainToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    // -------------------------
    private TaskCallback taskCallback = new TaskCallback() {
        @Override
        public void onExecute() {
            LogKit.v("onExecute");
        }
    };

    public void testClick(View view) {
//        LocalRepo.print();
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setAction("AAAA");
        taskEntity.setLoopTime(1000L);
        TaskFilterThread.getInstance().pushTask(JsonUtil.objToJson(taskEntity), taskCallback);
    }

    // -------------------------

    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<ZBaseFragment> fragmentList;

        public MyPagerAdapter(FragmentManager fm, List<ZBaseFragment> list) {
            super(fm);
            this.fragmentList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
