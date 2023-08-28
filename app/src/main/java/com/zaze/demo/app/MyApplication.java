package com.zaze.demo.app;


import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zaze.common.base.BaseApplication;
import com.zaze.core.network.di.NetworkModule;
import com.zaze.demo.BuildConfig;
import com.zaze.demo.component.network.compat.AnalyzeTrafficCompat;
import com.zaze.demo.component.system.ScreenLockReceiver;
import com.zaze.demo.debug.DefaultNetworkCallback;
import com.zaze.demo.debug.MyCrashHandler;
import com.zaze.demo.debug.wifi.WifiCompat;
import com.zaze.demo.feature.communication.broadcast.MessageReceiver;
import com.zaze.demo.matrix.MatrixHelper;
import com.zaze.demo.receiver.BatteryReceiver;
import com.zaze.demo.receiver.PackageReceiver;
import com.zaze.dynamic.hook.HookActivityThread;
import com.zaze.utils.DeviceUtil;
import com.zaze.utils.DisplayUtil;
import com.zaze.utils.FileUtil;
import com.zaze.utils.NetUtil;
import com.zaze.utils.TraceHelper;
import com.zaze.utils.ZCommand;
import com.zaze.utils.cache.MemoryCacheManager;
import com.zaze.utils.log.ZLog;

import java.util.prefs.Preferences;

import javax.inject.Inject;
import javax.inject.Provider;

import coil.ImageLoader;
import coil.ImageLoaderFactory;
import dagger.hilt.android.HiltAndroidApp;
import kotlin.jvm.functions.Function1;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-06 - 20:4
 */
@HiltAndroidApp
public class MyApplication extends BaseApplication implements ImageLoaderFactory {
    //    BroadcastReceiver receiver;

    public static MyApplication getInstance() {
        return (MyApplication) BaseApplication.getInstance();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private ClipboardMonitor clipboardMonitor = new ClipboardMonitor();
    public static final int BIND_SERVICE = 121;

    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
        MatrixHelper.INSTANCE.initMatrix(this);
        initRouter();
        initCrash();
        HookActivityThread.INSTANCE.swapHandlerCallback(new Function1<Handler.Callback, Handler.Callback>() {
            @Override
            public Handler.Callback invoke(Handler.Callback callback) {
                return new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if (msg.what == BIND_SERVICE) {
                            Log.i("BIND_SERVICE: ", "" + msg.obj);
                        }
                        if(callback != null) {
                            return callback.handleMessage(msg);
                        } else {
                            return false;
                        }
                    }
                };
            }
        });
        DisplayUtil.init(this);
        if (isMainProcess()) {
            onMainProcess();
        }
    }

    private void initLog() {
        ZCommand.setShowLog(true);
//        FileUtil.setShowLog(true);
        MemoryCacheManager.setCacheLog(true);
        AnalyzeTrafficCompat.setNeedLog(true);
        TraceHelper.INSTANCE.enable(BuildConfig.DEBUG);
        ZLog.openAllLog();
        ZLog.openAlwaysPrint();
        ZLog.registerLogCaller(FileUtil.class.getName());
        ZLog.registerLogCaller(DeviceUtil.class.getName());
        ZLog.registerLogCaller(NetworkModule.class.getName());
//        ThreadPlugins.runInUIThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    startService(new Intent(getInstance(), LogcatService.class));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 10_000L);
    }

    private void initRouter() {
        if (isDebug()) { // 这两行必须写在init之前，否则这些配置在init过程中将无效
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    private void onMainProcess() {
//        clipboardMonitor.init(this, clipData -> {
//            if (clipData != null && clipData.getItemCount() > 0) {
//                ClipData.Item item = clipData.getItemAt(0);
//                ZLog.i(ZTag.TAG_DEBUG, "clipData : " + item.getText());
//            }
//            return Unit.INSTANCE;
//        });
//        clipboardMonitor.setEnable(true);
        //
        ScreenLockReceiver.register(this);
        PackageReceiver broadcastReceiver = new PackageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        registerReceiver(broadcastReceiver, intentFilter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WifiCompat.listenerByConn(new DefaultNetworkCallback(NetUtil.getConnectivityManager(this)));
        } else {
            WifiCompat.listenerByBroadcast(this);
        }
        new MessageReceiver().register(this);
        BatteryReceiver.Companion.register(this);
    }

    private void initCrash() {
        Thread.setDefaultUncaughtExceptionHandler(new MyCrashHandler(Thread.getDefaultUncaughtExceptionHandler()));

//        CrashReport.initCrashReport(getApplicationContext(), "ecf90d7662", true);
        //
//        FontUtil.setDefaultFontFormSystem("DEFAULT", "Roboto-Light.ttf");
        //
//        CrashReport.initCrashReport(getApplicationContext(), "900013682", false);
//        receiver = new TestBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter("android.intent.action.message.testappid");
//        registerReceiver(receiver, intentFilter);
    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Inject
    public Provider<ImageLoader> imageLoader;

    @NonNull
    @Override
    public ImageLoader newImageLoader() {
        return imageLoader.get();
    }
}
