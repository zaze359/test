package com.zaze.demo.app;


import android.content.Intent;
import android.util.Log;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.QbSdk.PreInitCallback;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.smtt.sdk.WebView;
import com.zaze.common.base.BaseApplication;

import java.util.HashMap;

public class WebViewApplication extends BaseApplication {

    private static final String TAG = "WebViewApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        /* [new] 独立Web进程演示 */
        if (!startX5WebProcessPreinitService()) {
            return;
        }

        /* 设置允许移动网络下进行内核下载。默认不下载，会导致部分一直用移动网络的用户无法使用x5内核 */
//        QbSdk.setDownloadWithoutWifi(true);
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_PRIVATE_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        QbSdk.setCoreMinVersion(QbSdk.CORE_VER_ENABLE_202112);
        /* SDK内核初始化周期回调，包括 下载、安装、加载 */

        QbSdk.setTbsListener(new TbsListener() {

            /**
             * @param stateCode 用户可处理错误码请参考{@link com.tencent.smtt.sdk.TbsCommonCode}
             */
            @Override
            public void onDownloadFinish(int stateCode) {
                Log.i(TAG, "onDownloadFinished: " + stateCode);
            }

            /**
             * @param stateCode 用户可处理错误码请参考{@link com.tencent.smtt.sdk.TbsCommonCode}
             */
            @Override
            public void onInstallFinish(int stateCode) {
                Log.i(TAG, "onInstallFinished: " + stateCode);
            }

            /**
             * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
             * @param progress 0 - 100
             */
            @Override
            public void onDownloadProgress(int progress) {
                Log.i(TAG, "Core Downloading: " + progress);
            }
        });

        /* 此过程包括X5内核的下载、预初始化，接入方不需要接管处理x5的初始化流程，希望无感接入 */
        QbSdk.initX5Environment(this, new PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
            }

            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖wifi网络下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * 内核下发请求发起有24小时间隔，卸载重装、调整系统时间24小时后都可重置
             * 调试阶段建议通过 WebView 访问 debugtbs.qq.com -> 安装线上内核 解决
             * @param isX5 是否使用X5内核
             */
            @Override
            public void onViewInitFinished(boolean isX5) {
                Log.i(TAG, "onViewInitFinished: " + isX5);
                // hint: you can use QbSdk.getX5CoreLoadHelp(context) anytime to get help.
            }
        });
    }

    /**
     * 启动X5 独立Web进程的预加载服务。优点：
     * 1、后台启动，用户无感进程切换
     * 2、启动进程服务后，有X5内核时，X5预加载内核
     * 3、Web进程Crash时，不会使得整个应用进程crash掉
     * 4、隔离主进程的内存，降低网页导致的App OOM概率。
     *
     * 缺点：
     * 进程的创建占用手机整体的内存，demo 约为 150 MB
     */
    private boolean startX5WebProcessPreinitService() {
        String currentProcessName = QbSdk.getCurrentProcessName(this);
        // 设置多进程数据目录隔离，不设置的话系统内核多个进程使用WebView会crash，X5下可能ANR
        WebView.setDataDirectorySuffix(QbSdk.getCurrentProcessName(this));
        Log.i(TAG, currentProcessName);
        if (currentProcessName.equals(this.getPackageName())) {
            this.startService(new Intent(this, com.zaze.demo.app.X5ProcessInitService.class));
            return true;
        }
        return false;
    }

}
