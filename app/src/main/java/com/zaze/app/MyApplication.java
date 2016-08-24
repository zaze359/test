package com.zaze.app;

import android.app.Application;

import com.zz.library.util.LocalDisplay;

/**
 * Created by zaze on 16/4/26.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocalDisplay.init(this);
    }
}
