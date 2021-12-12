package com.zaze.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zaze.demo.app.MyApplication;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author zaze
 * @version 2017/8/2 - 下午1:29 1.0
 */
public class PackageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action != null) {
            final String packageName = intent.getData().getSchemeSpecificPart();
            ZLog.i(ZTag.TAG_DEBUG, ZStringUtil.format("PackageReceiver : %s（%s）", action, packageName));
            ZLog.i(ZTag.TAG_DEBUG, "PackageReceiver : " + intent.getDataString());
            if (!MyApplication.getInstance().getPackageName().equals(packageName)) {
                if (TextUtils.equals(action, Intent.ACTION_PACKAGE_ADDED)) {
                    afterAppAdded(packageName);
                } else if (TextUtils.equals(action, Intent.ACTION_PACKAGE_REPLACED)) {
                    afterAppReplaced(packageName);
                } else if (TextUtils.equals(action, Intent.ACTION_PACKAGE_REMOVED)) {
                    afterAppRemoved(packageName);
                }
            }
        }
    }

    // --------------------------------------------------
    private void afterAppAdded(String packageName) {
        ZLog.i(ZTag.TAG_DEBUG, "PackageReceiver 添加应用 : " + packageName);
    }

    private void afterAppReplaced(String packageName) {
        ZLog.i(ZTag.TAG_DEBUG, "PackageReceiver 替换应用 : " + packageName);
    }

    private void afterAppRemoved(String packageName) {
        ZLog.i(ZTag.TAG_DEBUG, "PackageReceiver 卸载成功" + packageName);
    }
}
