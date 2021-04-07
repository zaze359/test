package com.zaze.demo.usagestats

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi

object AppUsageHelper {
    private const val TAG = "AppUsageHelper"
    private const val PACKAGE_NAME_UNKNOWN = "unknown"


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getUsageStatsManager(context: Context): UsageStatsManager? {
        return context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager?
    }

    /**
     * 检测是否有获取应用使用量权限
     */
    fun checkAppUsagePermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true
        }
        val time = System.currentTimeMillis()
        val usageStatsList = getUsageStatsList(context, UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time)
        return !usageStatsList.isNullOrEmpty()
    }

    /**
     * 请求获取应用使用量权限
     */
    fun requestAppUsagePermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Log.e(TAG, "ACTION_USAGE_ACCESS_SETTINGS FAIL!", e)
            }
        }
    }

    fun getTopActivityPackageName(context: Context): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return PACKAGE_NAME_UNKNOWN
        }
        val time = System.currentTimeMillis()
        val usageStatsList = getUsageStatsList(context, UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time)
        return if (usageStatsList.isNullOrEmpty()) {
            PACKAGE_NAME_UNKNOWN
        } else {
            usageStatsList.sortedByDescending {
                it.lastTimeUsed
            }[0].packageName
        }
    }


    /**
     * INTERVAL_DAILY 天存储级别的数据
     * INTERVAL_WEEKLY 星期存储级别的数据
     * INTERVAL_MONTHLY 月存储级别的数据
     * INTERVAL_YEARLY 年存储级别的数据
     * INTERVAL_BEST 根据提供的时间间隔（根据与第二个参数和第三个参数获取），自动搭配最好的级别
     */
    fun getUsageStatsList(context: Context, intervalType: Int, beginTime: Long, endTime: Long): List<UsageStats>? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val usageStatsManager = getUsageStatsManager(context) ?: return null
            return usageStatsManager.queryUsageStats(intervalType, beginTime, endTime)
        }
        return null
    }
}