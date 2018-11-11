package com.zaze.demo.debug;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.TrafficStats;

import com.zaze.demo.app.MyApplication;
import com.zaze.utils.AppUtil;

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
        List<ApplicationInfo> allAppList = AppUtil.getInstalledApplications(MyApplication.getInstance(), 0);
        List<NetTrafficStats> networkStatList = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (ApplicationInfo applicationInfo : allAppList) {
            int uid = applicationInfo.uid;
            if (!set.contains(uid)) {
                set.add(uid);
                long rxBytes = TrafficStats.getUidRxBytes(uid);
                long txBytes = TrafficStats.getUidTxBytes(uid);
                if (rxBytes > 0 || txBytes > 0) {
                    NetTrafficStats netTrafficStats = new NetTrafficStats();
                    netTrafficStats.setUid(uid);
                    netTrafficStats.setRxBytes(rxBytes);
                    netTrafficStats.setTxBytes(txBytes);
                    netTrafficStats.setAppShortcut(ApplicationManager.getAppShortcut(applicationInfo.packageName));
                    networkStatList.add(netTrafficStats);
                }
            }
        }
        return networkStatList;
    }
}
