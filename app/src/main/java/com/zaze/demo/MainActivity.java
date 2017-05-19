package com.zaze.demo;


import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.commons.task.TaskCallback;
import com.zaze.aarrepo.commons.task.TaskEntity;
import com.zaze.demo.component.table.ui.TableFragment;

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
        public void onExecute(TaskEntity entity) {
            ZLog.v("", "onExecute : " + entity);
        }
    };

    public void testClick(View view) {

//        ConfigHelper configHelper = ConfigHelper.newInstance("/sdcard/text.ini");
//        configHelper.setProperty("aa", "2");
//        int a = StringUtil.parseInt(configHelper.getProperty("aa"));
//        ZLog.v("aa", "" + a);
//        String str = null;
//        File file = new File("/sdcard/xuehai/download/app/com.xuehai.launcher/2/com.xuehai.launcher.apk");
//        ZipUtil.unZipFolder("/sdcard/xuehai/download/app/com.xuehai.launcher/2/com.xuehai.launcher.apk", "/sdcard/aa/");
        changeLight();
    }

    // --------------------------------------------------

    private void changeLight() {
        ContentResolver contentResolver = this.getContentResolver();
        Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 24);
        contentResolver.notifyChange(uri, null);
    }

    // -------------------------

    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<ZBaseFragment> fragmentList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm, List<ZBaseFragment> list) {
            super(fm);
            this.fragmentList.clear();
            if(list != null) {
                fragmentList.addAll(list);
            }
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
