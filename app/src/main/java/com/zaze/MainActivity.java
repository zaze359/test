package com.zaze;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.zaze.component.table.ui.TableFragment;
import com.zz.library.commons.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

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
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        getWindow().setExitTransition(explode);
        //
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(TableFragment.newInstance("1"));
        mainViewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList));
        mainTab.setViewPager(mainViewpager);
    }

    private void initToolBar() {
        mainToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> fragmentList;

        public MyPagerAdapter(FragmentManager fm, List<BaseFragment> list) {
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
