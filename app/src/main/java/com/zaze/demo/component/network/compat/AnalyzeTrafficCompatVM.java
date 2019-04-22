package com.zaze.demo.component.network.compat;


import android.annotation.TargetApi;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;

import com.zaze.demo.debug.AppShortcut;
import com.zaze.demo.debug.ApplicationManager;
import com.zaze.demo.debug.NetTrafficStats;
import com.zaze.utils.date.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.content.Context.NETWORK_STATS_SERVICE;

/**
 * Description : Android 5.0
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 15:05
 */
@TargetApi(Build.VERSION_CODES.M)
public class AnalyzeTrafficCompatVM extends AnalyzeTrafficCompat {

    private NetworkStatsManager networkStatsManager;

    AnalyzeTrafficCompatVM(Context applicationContext) {
        super();
        networkStatsManager = (NetworkStatsManager) applicationContext.getSystemService(NETWORK_STATS_SERVICE);
    }

    // --------------------------------------------------

    @Override
    public Collection<NetTrafficStats> getDayNetworkTraffic() {
        final long current = System.currentTimeMillis();
        List<NetTrafficStats> networkStatList = new ArrayList<>();
        try {
            NetworkStats stats = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, null, DateUtil.getDayStart(current), current);
            while (stats != null && stats.hasNextBucket()) {
                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                stats.getNextBucket(bucket);
                AppShortcut shortcut = ApplicationManager.getAppShortcutByUid(bucket.getUid());
                if (shortcut != null) {
                    NetTrafficStats netTrafficStats = new NetTrafficStats();
                    netTrafficStats.setUid(bucket.getUid());
                    netTrafficStats.setAppShortcut(shortcut);
                    netTrafficStats.setRxBytes(bucket.getRxBytes());
                    netTrafficStats.setTxBytes(bucket.getTxBytes());
                    networkStatList.add(netTrafficStats);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return networkStatList;
    }

    /**
     * 分析程序网络流量统计
     * /proc/net/xt_qtaguid/stats
     *
     * @return JSONArray
     */
    @Override
    protected List<NetTrafficStats> getNewestNetworkTraffic() {
        return null;
    }
}
