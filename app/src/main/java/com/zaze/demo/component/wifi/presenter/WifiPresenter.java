package com.zaze.demo.component.wifi.presenter;

import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.component.wifi.contract.WifiContract;
import com.zaze.demo.receiver.WifiReceiver;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.AppUtil;
import com.zaze.utils.ZNetUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-25 07:33 1.0
 */
public class WifiPresenter extends BaseMvpPresenter<WifiContract.View> implements WifiContract.Presenter, WifiReceiver.WifiCallBack {
    private WifiManager wifiManager;
    private List<ScanResult> scanResultList = new ArrayList<>();
    private boolean autoRefresh = false;

    private static HandlerThread workThread = new HandlerThread("workThread");

    static {
        workThread.start();
    }

    private Handler workHandler = new Handler(workThread.getLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    };

    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            wifiManager.startScan();
            scanResultList.clear();
            HashSet<String> hashSet = new HashSet<>();
            List<ScanResult> tempList = wifiManager.getScanResults();
            if (tempList != null && !tempList.isEmpty()) {
                for (ScanResult scanResult : tempList) {
                    if (!TextUtils.isEmpty(scanResult.SSID)
                            && !hashSet.contains(scanResult.SSID)) {
                        scanResultList.add(scanResult);
                        hashSet.add(scanResult.SSID);
                    }
                }
            }
            ThreadManager.getInstance().runInUIThread(refreshListRunnable);
        }
    };

    private Runnable refreshListRunnable = new Runnable() {
        @Override
        public void run() {
            workHandler.removeCallbacks(scanRunnable);
            if (isViewAttach()) {
                getView().showWifiList(scanResultList);
                if (autoRefresh) {
                    workHandler.postDelayed(scanRunnable, 5000L);
                }
            }
        }
    };

    public WifiPresenter(WifiContract.View view) {
        super(view);
        wifiManager = ZNetUtil.getWifiManager(view.getContext());
    }

    @Override
    public void getNetworkState() {
//        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getView().getContext().getSystemService(NETWORK_STAT_SERVICE);
        ZLog.i(ZTag.TAG_DEBUG, "TrafficStats.getTotalRxBytes() : " + TrafficStats.getTotalRxBytes());
        ZLog.i(ZTag.TAG_DEBUG, "TrafficStats.getTotalTxBytes() : " + TrafficStats.getTotalTxBytes());
        ZLog.i(ZTag.TAG_DEBUG, "TrafficStats.getTotalTxBytes() : " + TrafficStats.getUidTxBytes(AppUtil.INSTANCE.getApplicationInfo(getView().getContext(), null).uid));
    }

    @Override
    public void startScan() {
        workHandler.post(scanRunnable);
    }

    @Override
    public void onReceive(@Nullable NetworkInfo networkInfo) {
        workHandler.removeCallbacks(scanRunnable);
        workHandler.post(scanRunnable);
    }
}
