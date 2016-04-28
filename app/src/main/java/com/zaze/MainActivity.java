package com.zaze;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.zaze.ui.fragment.HomeFragment;

/**
 * Description :
 * date : 2016-04-21 - 12:46
 *
 * @author : zaze
 * @version : 1.0
 */
public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.BlueTheme);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment.newInstance()).commit();
    }
}
