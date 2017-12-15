package com.zaze.demo.debug;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-09-29 - 15:31
 */
public class TestDebug {
    public static void a(Context context) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        context.startActivity(intent);
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


}
