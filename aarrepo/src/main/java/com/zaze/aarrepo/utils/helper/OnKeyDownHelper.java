package com.zaze.aarrepo.utils.helper;

import android.view.KeyEvent;

import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.utils.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-05-18 - 15:56
 */
public class OnKeyDownHelper {
    private static volatile OnKeyDownHelper instance;
    private static long clickTime;

    private static long intervalTime = 1000L;

    public void setIntervalTime(long intervalTime) {
        OnKeyDownHelper.intervalTime = intervalTime;
    }

    /**
     * @param keyCode
     * @param event
     * @param callback
     * @return
     */
    public static boolean doubleClickBack(int keyCode, KeyEvent event, OnBackCallback callback) {
        ZLog.v(ZTag.TAG_DEBUG, "keyCode" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (System.currentTimeMillis() - clickTime <= intervalTime) {
                    if (callback != null) {
                        ZLog.v(ZTag.TAG_DEBUG, "keyCode onSecondClick");
                        callback.onSecondClick();
                    }
                } else {
                    if (callback != null) {
                        ZLog.v(ZTag.TAG_DEBUG, "keyCode onFirstClick");
                        callback.onFirstClick();
                    }
                    clickTime = System.currentTimeMillis();
                }
                break;
            default:
                break;
        }
        return true;
    }

    public interface OnBackCallback {
        void onFirstClick();

        void onSecondClick();
    }

}
