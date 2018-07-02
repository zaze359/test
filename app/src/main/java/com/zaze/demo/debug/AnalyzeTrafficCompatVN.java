package com.zaze.demo.debug;


import android.content.Context;

import com.zaze.utils.FileUtil;
import com.zaze.utils.ZStringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description : Android 7.0
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 15:05
 */
public class AnalyzeTrafficCompatVN extends AnalyzeTrafficCompat {

    public AnalyzeTrafficCompatVN(Context applicationContext) {
    }

    /**
     * 分析程序网络流量统计
     * /proc/net/xt_qtaguid/stats
     *
     * @return JSONArray
     */
    @Override
    protected List<NetTrafficStats> getNewestNetworkTraffic() {
        File file = new File("/proc/uid_stat");
        List<NetTrafficStats> networkStatList = new ArrayList<>();

        if (file.exists()) {
            Set<Integer> set = new HashSet<>();
            File[] uidDirs = file.listFiles();
            if (uidDirs.length > 0) {
                for (File uidDir : uidDirs) {
                    try {
                        int uid = ZStringUtil.parseInt(uidDir.getName());
                        if (!set.contains(uid)) {
                            set.add(uid);
                            NetTrafficStats netTrafficStats = new NetTrafficStats();
                            netTrafficStats.setUid(uid);
                            netTrafficStats.setRxBytes(Long.valueOf(FileUtil.INSTANCE.readFromFile(uidDir.getAbsolutePath() + "/tcp_rcv").toString()));
                            netTrafficStats.setTxBytes(Long.valueOf(FileUtil.INSTANCE.readFromFile(uidDir.getAbsolutePath() + "/tcp_snd").toString()));
                            networkStatList.add(netTrafficStats);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        return networkStatList;
    }
    // --------------------------------------------------
}
