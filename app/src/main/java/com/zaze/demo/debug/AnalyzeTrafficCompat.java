package com.zaze.demo.debug;


import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.zaze.utils.AnalyzeUtil;
import com.zaze.utils.ZFileUtil;
import com.zaze.utils.ZJsonUtil;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.cache.MemoryCacheManager;
import com.zaze.utils.config.ZConfigHelper;
import com.zaze.utils.date.ZDateUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 15:05
 */
public abstract class AnalyzeTrafficCompat extends AnalyzeUtil {
    private static final String KEY_UN_KNOW_APP = "un_know";
    private static final String KEY_BOOT_TIME = "boot_time";
    private static final String KEY_TRAFFIC_LIST = "traffic_list";

    private static final Object INSTANCE_LOCK = new Object();
    private static AnalyzeTrafficCompat compat;
    private static boolean isExecute = false;

    private ZConfigHelper latelyTrafficStatsFile = ZConfigHelper.newInstance(
            ZFileUtil.INSTANCE.getSDCardRoot() + "/xuehai/keep/stats/LatelyNetworkTraffic.stats");

    /**
     * 根据时间返回对应的日统计文件
     *
     * @return 日统计文件
     */
    private ZConfigHelper getDayTrafficStatsFile() {
        return ZConfigHelper.newInstance(ZStringUtil.format("%s/xuehai/keep/stats/DayNetworkTraffic_%s.stats",
                ZFileUtil.INSTANCE.getSDCardRoot(), ZDateUtil.getDayEnd(System.currentTimeMillis())));
    }

    public static AnalyzeTrafficCompat getInstance(Context context) {
        synchronized (INSTANCE_LOCK) {
            if (compat == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    compat = new AnalyzeTrafficCompatVN(context.getApplicationContext());
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    compat = new AnalyzeTrafficCompatVM(context.getApplicationContext());
                } else {
                    compat = new AnalyzeTrafficCompatVL(context.getApplicationContext());
                }
            }
            return compat;
        }
    }

    public Collection<NetTrafficStats> getDayNetworkTraffic() {
        analyzeNetworkTraffic();
        return archiveNetworkTraffic(true);
    }

    /**
     * 网络流量
     *
     * @return List<NetTrafficStats>
     */
    protected abstract List<NetTrafficStats> getNewestNetworkTraffic();

    // --------------------------------------------------

    Random random = new Random(10000);

    public List<NetTrafficStats> analyzeNetworkTraffic() {
        synchronized (INSTANCE_LOCK) {
            int nextLong = Math.abs(random.nextInt());
            long bootTime = getBootTime();
            Map<String, String> map = new HashMap<>();
            map.put(KEY_BOOT_TIME, String.valueOf(bootTime));
            // --------------------------------------------------
            String savedBootTime = latelyTrafficStatsFile.getProperty(KEY_BOOT_TIME);
            if (TextUtils.isEmpty(savedBootTime) || Long.valueOf(savedBootTime) != bootTime + nextLong) {
                ZLog.i(ZTag.TAG_DEBUG, "开机时间发生变更");
                archiveNetworkTraffic(true);
            }
            // --------------------------------------------------
            List<NetTrafficStats> latelyTrafficStatsList = ZJsonUtil.parseJsonToList(latelyTrafficStatsFile.getProperty(KEY_TRAFFIC_LIST),
                    new TypeToken<List<NetTrafficStats>>() {
                    }.getType());
            HashMap<Integer, NetTrafficStats> latelyTrafficStatsMap = new HashMap<>();
            if (latelyTrafficStatsList != null && !latelyTrafficStatsList.isEmpty()) {
                for (NetTrafficStats netTrafficStats : latelyTrafficStatsList) {
                    latelyTrafficStatsMap.put(netTrafficStats.getUid(), netTrafficStats);
                }
            }
            // --------------------------------------------------
            List<NetTrafficStats> list = this.getNewestNetworkTraffic();
            List<NetTrafficStats> saveList = new ArrayList<>();
            for (NetTrafficStats netTrafficStats : list) {
                netTrafficStats.setBootTime(bootTime);
                NetTrafficStats mergedStats = merge(netTrafficStats, latelyTrafficStatsMap.get(netTrafficStats.getUid()));
                if (mergedStats != null) {
                    saveList.add(mergedStats);
                }
            }
            map.put(KEY_TRAFFIC_LIST, ZJsonUtil.objToJson(saveList));
            latelyTrafficStatsFile.setProperty(map);
            return list;
        }
    }

    /**
     * 合并处理刚获取到的统计最近的统计
     *
     * @param newStats 刚获取到的统计
     * @param oldStats 最近的统计
     * @return 合并后的统计
     */
    private NetTrafficStats merge(NetTrafficStats newStats, NetTrafficStats oldStats) {
        NetTrafficStats netTrafficStats;
        if (oldStats == null) {
            netTrafficStats = newStats;
        } else {
            netTrafficStats = oldStats;
            if (newStats != null) {
                netTrafficStats.setRxBytes(newStats.getRxBytes());
                netTrafficStats.setTxBytes(newStats.getTxBytes());
                netTrafficStats.setBootTime(newStats.getBootTime());
            }
        }
        if (netTrafficStats != null) {
            AppShortcut appShortcut = netTrafficStats.getAppShortcut();
            if (appShortcut == null) {
                appShortcut = ApplicationManager.getAppShortcutByUid(netTrafficStats.getUid());
            }
            netTrafficStats.setAppShortcut(appShortcut);
        }
        return netTrafficStats;
    }


    /**
     * 流量归档存储
     * 将所有数据按照包名统计
     * [isSave] 是否保存
     */
    private Collection<NetTrafficStats> archiveNetworkTraffic(boolean isSave) {
        synchronized (INSTANCE_LOCK) {
            ZLog.i(ZTag.TAG_DEBUG, "检查数据并进行合并归档");
            long bootTime = getBootTime();
            ZConfigHelper dayTrafficStatsFile = this.getDayTrafficStatsFile();
            Collection<NetTrafficStats> dayTrafficStatsList = ZJsonUtil.parseJsonToList(dayTrafficStatsFile.getProperty(KEY_TRAFFIC_LIST),
                    new TypeToken<List<NetTrafficStats>>() {
                    }.getType());
            HashMap<String, NetTrafficStats> dayTrafficStatsMap = new HashMap<>();
            if (dayTrafficStatsList != null) {
                for (NetTrafficStats netTrafficStats : dayTrafficStatsList) {
                    AppShortcut appShortcut = netTrafficStats.getAppShortcut();
                    if (appShortcut != null && !TextUtils.isEmpty(appShortcut.getPackageName())) {
                        dayTrafficStatsMap.put(appShortcut.getPackageName(), netTrafficStats);
                    }
                }
            }
            // --------------------------------------------------
            List<NetTrafficStats> latelyTrafficStatsList = ZJsonUtil.parseJsonToList(latelyTrafficStatsFile.getProperty(KEY_TRAFFIC_LIST),
                    new TypeToken<List<NetTrafficStats>>() {
                    }.getType());
            List<NetTrafficStats> updateTrafficStatsList = new ArrayList<>();
            if (latelyTrafficStatsList != null) {
                for (NetTrafficStats netTrafficStats : latelyTrafficStatsList) {
                    AppShortcut appShortcut = netTrafficStats.getAppShortcut();
                    if (appShortcut != null && !TextUtils.isEmpty(appShortcut.getPackageName())) {
                        String packageName = appShortcut.getPackageName();
                        NetTrafficStats dayNetTrafficStats;
                        if (dayTrafficStatsMap.containsKey(packageName)) {
                            dayNetTrafficStats = dayTrafficStatsMap.get(packageName);
                        } else {
                            dayNetTrafficStats = new NetTrafficStats();
                            dayNetTrafficStats.setAppShortcut(appShortcut);
                            dayNetTrafficStats.setUid(appShortcut.getUid());
                        }
                        dayNetTrafficStats.setRxBytes(dayNetTrafficStats.getRxBytes() + netTrafficStats.getDiffRxBytes());
                        dayNetTrafficStats.setTxBytes(dayNetTrafficStats.getTxBytes() + netTrafficStats.getDiffTxBytes());
                        dayTrafficStatsMap.put(packageName, dayNetTrafficStats);
                        if (netTrafficStats.getBootTime() == bootTime) {
                            // 仅保存此次开机时间的数据，不是的合并完后都丢弃
                            updateTrafficStatsList.add(netTrafficStats);
                        }
                    }
                }
            }
            if (isSave) {
                // 更新应用流量的上一次合并大小
                latelyTrafficStatsFile.setProperty(KEY_TRAFFIC_LIST, ZJsonUtil.objToJson(updateTrafficStatsList));
                // 更新日统计数据
                dayTrafficStatsFile.setProperty(KEY_TRAFFIC_LIST, ZJsonUtil.objToJson(dayTrafficStatsMap.values()));
                dayTrafficStatsFile.setProperty(KEY_BOOT_TIME, String.valueOf(bootTime));
            }
            return dayTrafficStatsMap.values();
        }
    }

    // --------------------------------------------------

    /**
     * 获取开机时间
     *
     * @return 开机时间
     */
    private long getBootTime() {
        String key = "getBootTime";
        String value = MemoryCacheManager.getCache(key);
        try {
            if (TextUtils.isEmpty(value)) {
                // 配置文件中的时间 单位为 秒
                value = analyzeProcStat().getString("btime") + "000";
                MemoryCacheManager.saveCache(key, value);
            }
            return Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 分析 "/proc/stat" 文件
     *
     * @return JSONObject
     */
    protected JSONObject analyzeProcStat() {
        return analyzeFileFirstValueIsTag("/proc/stat", "\n", "\\s+");
    }
}
