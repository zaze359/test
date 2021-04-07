package com.zaze.demo.usagestats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Description :
 * @author : zaze
 * @version : 2021-04-07 - 16:52
 */
class UsageStatesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

//
//private Context mContext;
//private UsageStatsManager mUsageStatsManager;
//private long mCurrentTime;
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
//
//    mContext = this;
//    AppUsageUtil.checkUsageStateAccessPermission(mContext);
//    mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
//}
//
//@Override
//protected void onResume() {
//    super.onResume();
//    mCurrentTime = System.currentTimeMillis();
//    if(mUsageStatsManager != null) {
//        queryUsageStats();
//        queryConfigurations();
//        queryEventStats();
//        AppUsageUtil.getTopActivityPackageName(mContext);
//    }
//
//}
//
//private void queryUsageStats() {
//    List<UsageStats> usageStatsList = mUsageStatsManager.queryUsageStats(
//            UsageStatsManager.INTERVAL_DAILY, mCurrentTime - 60 * 1000, mCurrentTime);
//    for(UsageStats usageStats: usageStatsList) {
//        Log.d(TAG,"usageStats PackageName = " + usageStats.getPackageName() + " , FirstTimeStamp = "
//                + usageStats.getFirstTimeStamp() + " , LastTimeStamp = " + usageStats.getLastTimeStamp()
//                + ", LastTimeUsed = " + usageStats.getLastTimeUsed()
//                + " , TotalTimeInForeground = " + usageStats.getTotalTimeInForeground());
//    }
//}
//
//private void queryConfigurations() {
//    List<ConfigurationStats> configurationStatsList = mUsageStatsManager.queryConfigurations(
//            UsageStatsManager.INTERVAL_DAILY,mCurrentTime - 60 * 1000, mCurrentTime);
//    for (ConfigurationStats configurationStats:configurationStatsList) {
//        Log.d(TAG,"configurationStats Configuration = " + configurationStats.getConfiguration() + " , ActivationCount = " + configurationStats.getActivationCount()
//                + " , FirstTimeStamp = " + configurationStats.getFirstTimeStamp() + " , LastTimeStamp = " + configurationStats.getLastTimeStamp()
//                + " , LastTimeActive = " + configurationStats.getLastTimeActive() + " , TotalTimeActive = " + configurationStats.getTotalTimeActive());
//    }
//}
//
//private void queryEventStats() {
//    if (android.os.Build.VERSION.SDK_INT >= 28) {
//        List<EventStats> eventStatsList = mUsageStatsManager.queryEventStats(
//                UsageStatsManager.INTERVAL_DAILY,mCurrentTime - 60 * 1000,mCurrentTime);
//        for(EventStats eventStats:eventStatsList) {
//            Log.d(TAG,"eventStats EventType" + eventStats.getEventType() + " , Count = " + eventStats.getCount()
//                    + " , FirstTime = " + eventStats.getFirstTimeStamp() + " , LastTime = " + eventStats.getLastTimeStamp()
//                    + " , LastEventTime = " + eventStats.getLastEventTime() + " , TotalTime = " + eventStats.getTotalTime());
//        }
//    }
//}
