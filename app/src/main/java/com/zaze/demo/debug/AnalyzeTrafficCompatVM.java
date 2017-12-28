package com.zaze.demo.debug;


import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.zaze.utils.ZReflectUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.NETWORK_STATS_SERVICE;

/**
 * Description : Android 5.0
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 15:05
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class AnalyzeTrafficCompatVM extends AnalyzeTrafficCompat {

    NetworkStatsManager networkStatsManager;

    public AnalyzeTrafficCompatVM(Context applicationContext) {
        super();
        networkStatsManager = (NetworkStatsManager) applicationContext.getSystemService(NETWORK_STATS_SERVICE);
//        networkStatsManager.querySummary()
    }

    // --------------------------------------------------

    /**
     * 分析程序网络流量统计
     * /proc/net/xt_qtaguid/stats
     *
     * @return JSONArray
     */
    @Override
    protected List<NetTrafficStats> getNewestNetworkTraffic() {

        List<AppShortcut> allAppShortcutList = ApplicationManager.getInstalledAppShortcuts();
        List<NetTrafficStats> networkStatList = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (AppShortcut appShortcut : allAppShortcutList) {
            int uid = appShortcut.getUid();
            if (!set.contains(uid)) {
                set.add(uid);
                long rxBytes = 0;
                long txBytes = 0;
                try {
                    rxBytes = (long) ZReflectUtil.executeMethodByName("android.net.TrafficStats", "nativeGetUidStat", new Integer(uid), new Integer(0));
                    txBytes = (long) ZReflectUtil.executeMethodByName("android.net.TrafficStats", "nativeGetUidStat", new Integer(uid), new Integer(2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (rxBytes > 0 || txBytes > 0) {
                    NetTrafficStats netTrafficStats = new NetTrafficStats();
                    netTrafficStats.setUid(uid);
                    netTrafficStats.setRxBytes(rxBytes);
                    netTrafficStats.setTxBytes(txBytes);
                    netTrafficStats.setAppShortcut(appShortcut);
                    networkStatList.add(netTrafficStats);
                }
            }
        }
        return networkStatList;
    }
}
