package com.zaze.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zaze.demo.app.MyApplication;
import com.zaze.utils.ThreadManager;
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
        if (intent.getData() != null) {
            final String packageName = intent.getData().getSchemeSpecificPart();
//        XHLog.i(LcTag.TAG_DEBUG, "PackageReceiver : %s（%s）", action, packageName);
//        XHLog.i(LcTag.TAG_DEBUG, "PackageReceiver : %s", intent.getDataString());
            if (!MyApplication.getInstance().getPackageName().equals(packageName)) {
                ThreadManager.getInstance().runInSingleThread(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.equals(action, Intent.ACTION_PACKAGE_ADDED)) {
                            afterAppAdded(packageName);
                        } else if (TextUtils.equals(action, Intent.ACTION_PACKAGE_REPLACED)) {
                            afterAppReplaced(packageName);
                        } else if (TextUtils.equals(action, Intent.ACTION_PACKAGE_REMOVED)) {
                            afterAppRemoved(packageName);
                        }
                    }
                });
            }
        }
    }

    // --------------------------------------------------
    private void afterAppAdded(String packageName) {
        ZLog.i(ZTag.TAG_DEBUG, "添加应用 : " + packageName);
//        afterAppInstalled(packageName, UpdateType.ADD);
    }

    private void afterAppReplaced(String packageName) {
        ZLog.i(ZTag.TAG_DEBUG, "替换应用 : " + packageName);
//        afterAppInstalled(packageName, UpdateType.UPDATE);
    }

    private void afterAppRemoved(String packageName) {
        ZLog.i(ZTag.TAG_DEBUG, "卸载成功" + packageName);
//        LauncherSPUtil.updateNewAppStatus(packageName, false);
//        if (SessionData.getUserId() > 0) {
//            int versionCode = CacheManager.INSTANCE.getLastVersionCode(packageName);
//            AppUpdateHelper.postUpdateStatus(packageName, versionCode, 0, UpdateStatus.NEED_DOWNLOAD);
//            AppUpdateHelper.uploadAppInstallStatus(packageName, versionCode, UpdateType.DELETE);
//        }
//        CacheManager.INSTANCE.clearLastVersionCode(packageName);
    }

//    // --------------------------------------------------
//    private void afterAppInstalled(String packageName, int updateType) {
//        LauncherSPUtil.updateNewAppStatus(packageName, true);
//        checkApp(packageName);
//        // -- 重置缓存中的版本号
//        CacheManager.INSTANCE.clearLastVersionCode(packageName);
//        int versionCode = CacheManager.INSTANCE.getLastVersionCode(packageName);
//        XHLog.i(LcTag.TAG_APP, "安装成功 : %s(%s)", packageName, versionCode);
//        if (SessionData.getUserId() > 0) {
//            AppUpdateHelper.postUpdateStatus(packageName, versionCode, 100, UpdateStatus.FINISH);
//            AppUpdateHelper.uploadAppInstallStatus(packageName, versionCode, updateType);
//        }
//        EventBus.getDefault().post(new RefreshEvent(RefreshEvent.RefreshType.REFRESH_CURR_PAGE_APP));
//    }
//

//    // --------------------------------------------------
//    private void checkApp(final String packageName) {
//        Set<String> blackList = DeviceSupportUtil.INSTANCE.getAppBlackList();
//        if (blackList.contains(packageName)) {
//            XHLog.i(LcTag.TAG_DELETE, "检查到安装了'走私'应用 " + packageName);
//            ProcessFactory.getAppProcess().unInstallApk(packageName);
//        }
//    }
}
