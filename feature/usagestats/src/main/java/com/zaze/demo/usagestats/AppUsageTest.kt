package com.zaze.demo.usagestats

import android.app.usage.UsageEvents
import android.content.Context
import android.os.Build
import android.os.SystemClock
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.zaze.utils.TraceHelper
import com.zaze.utils.date.DateUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2021-12-13 - 15:30
 */
class AppUsageTest(private val context: Context) {

    private val TAG = "AppUsageTest"
    private var run = false
    private val thread by lazy {
        object : Thread() {
            override fun run() {
                while (run) {
                    if (!AppUsageHelper.checkAppUsagePermission(context)) {
//                        AppUsageHelper.requestAppUsagePermission(context)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            ZLog.i(
                                TAG,
                                "getTopActivityPackageName: ${
                                    AppUsageHelper.getTopActivityPackageName(context)
                                }"
                            )
                        }
                    }
//                    test()
                    SystemClock.sleep(1_000L)
                }
            }
        }
    }

    @Synchronized
    fun run() {
        if (!run) {
            run = true
            thread.start()
        }
    }

    private fun test() {
        if (!AppUsageHelper.checkAppUsagePermission(context)) {
            ZLog.i(TAG, "checkAppUsagePermission false")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    val time = System.currentTimeMillis()
//                    val eventStatsList = AppUsageHelper.queryEventStats(
//                        this,
//                        UsageStatsManager.INTERVAL_DAILY,
//                        DateUtil.getDayStart(time),
//                        time
//                    )
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                        eventStatsList?.forEach {
//                            ZLog.i(ZTag.TAG, "queryEventStats eventType: ${it.eventType}")
//                            ZLog.i(
//                                ZTag.TAG,
//                                "queryEventStats lastEventTime: ${
//                                    DateUtil.timeMillisToString(
//                                        it.lastEventTime,
//                                        "yyyy-MM-dd HH:mm:ss"
//                                    )
//                                }"
//                            )
//                            ZLog.i(
//                                ZTag.TAG,
//                                "queryEventStats ${DateUtil.timeMillisToString(
//                                    it.firstTimeStamp,
//                                    "yyyy-MM-dd HH:mm:ss"
//                                )}/${DateUtil.timeMillisToString(
//                                    it.lastTimeStamp,
//                                    "yyyy-MM-dd HH:mm:ss"
//                                )}: ${it.lastTimeStamp - it.firstTimeStamp}"
//                            )
//                        }
//                    }
                var totalTime = 0L
//                    TraceHelper.beginSection("getDailyStats")
//                    getDailyStats().forEach {
//                        totalTime += it.totalTime
//                    }
//                    TraceHelper.endSection("getDailyStats")

                TraceHelper.beginSection("getDailyStats")
                val startTime = DateUtil.getDayStart(System.currentTimeMillis())
                ZLog.i(
                    TAG,
                    "getDailyStats: ${
                        getDailyStats(
                            startTime - DateUtil.DAY,
                            startTime + DateUtil.DAY
                        ).joinToString()
                    }"
                )
                TraceHelper.endSection("getDailyStats")

//                    val usageStatsManager = AppUsageHelper.getUsageStatsManager(this)
//                    usageStatsManager?.queryUsageStats(
//                        UsageStatsManager.INTERVAL_DAILY,
//                        DateUtil.getDayStart(time),
//                        time
//                    )?.forEach {
//                        val dateFormat = "yyyy-MM-dd HH:mm:ss:sss"
//                        val builder = StringBuilder("\n--------------------------------\n")
//                            .append("${it.packageName} UsageStats: totalTimeInForeground:${it.totalTimeInForeground};\n")
//                            .append(
//                                "firstTimeStamp:${
//                                    DateUtil.timeMillisToString(
//                                        it.firstTimeStamp,
//                                        dateFormat
//                                    )
//                                }; "
//                            )
//                            .append(
//                                "lastTimeStamp:${
//                                    DateUtil.timeMillisToString(
//                                        it.lastTimeStamp,
//                                        dateFormat
//                                    )
//                                }; "
//                            ).append(
//                                "lastTimeStamp:${
//                                    DateUtil.timeMillisToString(
//                                        it.lastTimeStamp,
//                                        dateFormat
//                                    )
//                                }; "
//                            )
//                        if (it.lastTimeUsed > 0) {
//                            builder.append(
//                                "lastTimeUsed:${
//                                    DateUtil.timeMillisToString(
//                                        it.lastTimeUsed,
//                                        dateFormat
//                                    )
//                                }; "
//                            )
//                        }
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            builder.append(
//                                "totalTimeVisible:${
//                                    DateUtil.timeMillisToString(
//                                        it.totalTimeVisible,
//                                        dateFormat
//                                    )
//                                }; "
//                            )
//                        }
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            builder.append(
//                                "totalTimeForegroundServiceUsed:${
//                                    DateUtil.timeMillisToString(
//                                        it.totalTimeForegroundServiceUsed,
//                                        dateFormat
//                                    )
//                                }; "
//                            )
//                        }
//                        builder.append("--------------------------------\n")
//                        ZLog.i(TAG, builder.toString())
//                    }
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDailyStats(start: Long, end: Long): List<Stat> {
        val sortedEvents = mutableMapOf<String, MutableList<UsageEvents.Event>>()
        val usageStatsManager = AppUsageHelper.getUsageStatsManager(context) ?: return emptyList()
        val systemEvents = usageStatsManager.queryEvents(start, end)
        while (systemEvents.hasNextEvent()) {
            val event = UsageEvents.Event()
            systemEvents.getNextEvent(event)
            // Get the list of events for the package name, create one if it doesn't exist
            val packageEvents = sortedEvents[event.packageName] ?: mutableListOf()
            packageEvents.add(event)
            sortedEvents[event.packageName] = packageEvents
        }
        val stats = mutableListOf<Stat>()
        ZLog.i(TAG, "appStats ------------------- start")
        sortedEvents.forEach { packageName, events ->
            // Keep track of the total usage time for this app
            var totalTime = 0L
            val appStats = mutableListOf<Stat>()
            // 分页面统计
            events.groupBy {
                it.className
            }.values.forEach {
                appStats.addAll(calculate(start, it))
            }
            appStats.forEach {
                totalTime += it.totalTime
            }
            val stat = Stat(packageName, totalTime)
            ZLog.i(ZTag.TAG, "appStats: $stat")
            stats.add(stat)
        }
        ZLog.i(ZTag.TAG, "appStats ------------------- end")
        return stats
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun calculate(
        start: Long,
        events: List<UsageEvents.Event>
    ): List<Stat> {
        val stats = mutableListOf<Stat>()
        // Keep track of the current start and end times
        var startTime = 0L
        var endTime = 0L
        // Keep track of the total usage time for this app
        var totalTime = 0L
        // Keep track of the start times for this app
        var latestStartTime = 0L
        var latestEventTime = 0L

        var packageName = ""
        var className: String? = null
        events.forEach {
            if (latestEventTime == 0L) {
                latestEventTime = it.timeStamp
            }
            packageName = it.packageName
            if (className == null) {
                className = it.className
            }
            ZLog.i(
                TAG,
                "pageEvent: ${it.packageName}(${it.className}); ${
                    DateUtil.timeMillisToString(
                        it.timeStamp,
                        "yyyy-MM-dd HH:mm:ss"
                    )
                }; ${it.timeStamp - latestEventTime}; ${
                    traceEvent(
                        it.eventType
                    )
                }"
            )
            latestEventTime = it.timeStamp
            if (it.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                startTime = it.timeStamp
                latestStartTime = startTime
            } else if (it.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                endTime = it.timeStamp
//                latestStartTime = endTime
            } else if (it.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                // 应用直接kill, 将导致ACTIVITY_PAUSED丢失
                if (startTime > 0 && endTime == 0L) {
                    endTime = it.timeStamp
//                    latestStartTime = endTime
                }
            }
            // 是否需要补?
            if (startTime == 0L && endTime != 0L) {
                startTime = latestStartTime
            }
            if (startTime != 0L && endTime != 0L) {
                totalTime += endTime - startTime
                startTime = 0L
                endTime = 0L
            }
        }
        if (startTime != 0L && endTime == 0L) {
            totalTime += System.currentTimeMillis() - 1000 - startTime
        }
        val stat = Stat(packageName, totalTime)
        ZLog.i(TAG, "pageStat: ${packageName}($className), $stat")
        stats.add(stat)
        return stats
    }

    data class Stat(
        val packageName: String,
        val totalTime: Long
    ) {
        override fun toString(): String {
            return "Stat--$packageName: ${totalTime}ms;"
        }
    }

    private fun traceEvent(eventType: Int): String {
        return when (eventType) {
            UsageEvents.Event.ACTIVITY_RESUMED -> {
                "($eventType)ACTIVITY_RESUMED(MOVE_TO_FOREGROUND)"
            }

            UsageEvents.Event.ACTIVITY_PAUSED -> {
                "($eventType)ACTIVITY_PAUSED(MOVE_TO_BACKGROUND)"
            }

            UsageEvents.Event.ACTIVITY_STOPPED -> {
                "($eventType)ACTIVITY_STOPPED"
            }

            UsageEvents.Event.STANDBY_BUCKET_CHANGED -> {
                "($eventType)STANDBY_BUCKET_CHANGED"
            }

            10 -> {
                "($eventType)NOTIFICATION_SEEN"
            }

            else -> {
                "($eventType)"
            }
        }
    }
}