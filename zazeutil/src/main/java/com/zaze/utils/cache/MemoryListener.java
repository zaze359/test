package com.zaze.utils.cache;

import android.content.res.Configuration;

/**
 * Description :
 * date : 2015-12-18 - 10:49
 *
 * @author : zaze
 * @version : 1.0
 */
interface MemoryListener {

    void onTrimMemory(int level);

    void onLowMemory();

    void onConfigurationChanged(Configuration newConfig);
}
