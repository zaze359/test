package com.zaze.demo.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.zaze.utils.DescriptionUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-09-29 - 15:31
 */
public class TestDebug {
    public static void a(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            TestDebug.hasPermissionToReadNetworkStats(context);
//        }

        ZLog.d(ZTag.TAG_DEBUG, "toByteUnit : %s", DescriptionUtil.toByteUnit(1024 * 1024 * 1024 + 1024 * 1024));
//        File file = new File("/proc/net/xt_qtaguid/stats");
//        AnalyzeTrafficCompat.getNewestNetworkTraffic();
//        AnalyzeTrafficCompat.analyzeProcStat();
//        AnalyzeTrafficCompat.getInstance(context).archiveNetworkTraffic(bootTime);
//        String cmd = "cat /proc/net/xt_qtaguid/stats | grep 10135";
//        ZCommand.CommandResult commandResult;
//        if (ZCommand.isRoot()) {
//            commandResult = ZCommand.execRootCmdForRes(cmd);
//        } else {
//            commandResult = ZCommand.execCmdForRes(cmd);
//        }
//        ZLog.d(ZTag.TAG_DEBUG, "analyzeFileFirstLineIsTag : " + AnalyzeUtil.analyzeFileFirstLineIsTag("/proc/net/xt_qtaguid/stats", "\n", "\\s+"));
//        ZLog.d(ZTag.TAG_DEBUG, "getDayNetworkTraffic : " + AnalyzeTrafficCompat.getInstance(context).getDayNetworkTraffic());
//        ZLog.d(ZTag.TAG_DEBUG, "rx_bytes : " + ZFileUtil.INSTANCE.readFromFile("/sys/class/net/wlan0/statistics/rx_bytes"));
//        ZLog.d(ZTag.TAG_DEBUG, "rx_bytes : " + AnalyzeTrafficCompat.getInstance(context).getDayNetworkTraffic());

//        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        context.startActivity(intent);
//        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
//        Calendar calendar = Calendar.getInstance();
//        long endTime = calendar.getTimeInMillis();
//        calendar.add(Calendar.YEAR, -1);
//        long startTime = calendar.getTimeInMillis();
//        List usageStatsList = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, System.currentTimeMillis());
//            ZLog.d(ZTag.TAG_DEBUG, "usageStatsList : " + usageStatsList);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasPermissionToReadNetworkStats(Context context) {
//        final AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
//                android.os.Process.myUid(), context.getPackageName());
//        if (mode == AppOpsManager.MODE_ALLOWED) {
//            return true;
//        }
        // 打开“有权查看使用情况的应用”页面
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        context.startActivity(intent);
        return false;
    }

}
