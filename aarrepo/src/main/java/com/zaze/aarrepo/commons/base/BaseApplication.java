package com.zaze.aarrepo.commons.base;

import android.app.Application;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-07-15 - 14:32
 */
public abstract class BaseApplication extends Application {
    private static BaseApplication instance;

    public BaseApplication() {
    }

    public static BaseApplication getInstance() {
        if(instance == null) {
            throw new IllegalStateException();
        } else {
            return instance;
        }
    }
}
