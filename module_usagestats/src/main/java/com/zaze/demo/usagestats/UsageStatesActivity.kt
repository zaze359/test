package com.zaze.demo.usagestats

import android.app.usage.UsageStatsManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zaze.utils.date.DateUtil
import com.zaze.utils.log.ZLog
import kotlinx.android.synthetic.main.usage_states_act.*
import java.lang.StringBuilder

/**
 * Description :
 * @author : zaze
 * @version : 2021-04-07 - 16:52
 */
class UsageStatesActivity : AppCompatActivity() {
    private val TAG = "UsageStatesActivityTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.usage_states_act)
        usageStateBtn.setOnClickListener {
            if (!AppUsageHelper.checkAppUsagePermission(this)) {
                AppUsageHelper.requestAppUsagePermission(this)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ZLog.i(TAG, "packageName: ${AppUsageHelper.getTopActivityPackageName(this)}")
                    val time = System.currentTimeMillis()
                    val usageStatsManager = AppUsageHelper.getUsageStatsManager(this)
                    usageStatsManager?.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, DateUtil.getDayStart(time), time)?.forEach {
                        val dateFormat = "yyyy-MM-dd HH:mm:ss:sss"
                        val builder = StringBuilder("\n--------------------------------\n")
                                .append("${it.packageName} UsageStats: totalTimeInForeground:${it.totalTimeInForeground}\n")
                                .append("${it.packageName} UsageStats: firstTimeStamp:${DateUtil.timeMillisToString(it.firstTimeStamp, dateFormat)}\n")
                                .append("${it.packageName} UsageStats: lastTimeStamp:${DateUtil.timeMillisToString(it.lastTimeStamp, dateFormat)}\n")
                        if (it.lastTimeUsed > 0) {
                            builder.append("${it.packageName} UsageStats: lastTimeUsed:${DateUtil.timeMillisToString(it.lastTimeUsed, dateFormat)}\n")
                        }
                        builder.append("--------------------------------\n")
                        ZLog.i(TAG, builder.toString())
                    }
//                    usageStatsManager?.queryConfigurations(UsageStatsManager.INTERVAL_DAILY, DateUtil.getDayStart(time), time)?.forEach {
//                        val dateFormat = "yyyy-MM-dd HH:mm:ss:sss"
//                        val builder = StringBuilder("\n--------------------------------\n")
//                                .append("Configuration: ${it.configuration}}\n")
//                                .append("ActivationCount: ${it.activationCount}\n")
//                                .append("totalTimeActive: ${it.totalTimeActive}\n")
//                                .append("lastTimeActive: ${DateUtil.timeMillisToString(it.lastTimeActive, dateFormat)}\n")
//                                .append("firstTimeStamp: ${DateUtil.timeMillisToString(it.firstTimeStamp, dateFormat)}\n")
//                                .append("lastTimeStamp: ${DateUtil.timeMillisToString(it.lastTimeStamp, dateFormat)}\n")
//                        builder.append("--------------------------------\n")
//                        ZLog.i(TAG, builder.toString())
//                    }

//Log.d(TAG,"eventStats EventType" + eventStats.getEventType() + " , Count = " + eventStats.getCount()
//                    + " , FirstTime = " + eventStats.getFirstTimeStamp() + " , LastTime = " + eventStats.getLastTimeStamp()
//                    + " , LastEventTime = " + eventStats.getLastEventTime() + " , TotalTime = " + eventStats.getTotalTime())
                }
            }
        }
    }
}
