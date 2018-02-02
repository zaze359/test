package com.zaze.demo.debug;


import android.content.Context;
import android.net.TrafficStats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description : Android 5.0
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 15:05
 */
public class AnalyzeTrafficCompatVL extends AnalyzeTrafficCompat {

    public AnalyzeTrafficCompatVL(Context applicationContext) {
        super();
    }


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
                long rxBytes = TrafficStats.getUidRxBytes(uid);
                long txBytes = TrafficStats.getUidTxBytes(uid);
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