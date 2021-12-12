package com.zaze.demo.usagestats

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.zaze.utils.TraceHelper
import com.zaze.utils.date.DateUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.android.synthetic.main.usage_states_act.*
import java.lang.StringBuilder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Description :
 * @author : zaze
 * @version : 2021-04-07 - 16:52
 */
class UsageStatesActivity : AppCompatActivity() {
    private val TAG = "UsageStatesActivityTag"
    private var run = true
    private val thread by lazy {
        object : Thread() {
            override fun run() {
                while (run) {
//                    if (!AppUsageHelper.checkAppUsagePermission(this@UsageStatesActivity)) {
//                        AppUsageHelper.requestAppUsagePermission(this@UsageStatesActivity)
//                    } else {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                            ZLog.i(
//                                TAG,
//                                "getTopActivityPackageName: ${
//                                    AppUsageHelper.getTopActivityPackageName(this@UsageStatesActivity)
//                                }"
//                            )
//                        }
//                    }

                    test()
                    SystemClock.sleep(10_000L)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.usage_states_act)
        thread.start()
        usageStateBtn.setOnClickListener {

        }
    }

    private fun test() {
        if (!AppUsageHelper.checkAppUsagePermission(this)) {
            AppUsageHelper.requestAppUsagePermission(this)
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
                    ZTag.TAG,
                    "queryEventStats: ${
                        getDailyStats(
                            startTime,
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

    //    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDailyStats(start: Long, end: Long): List<Stat> {
        // The timezones we'll need

//        ZLog.i(ZTag.TAG, "date: $date")
//        val utc = ZoneId.of("UTC")
//        val defaultZone = ZoneId.systemDefault()
//        // Set the starting and ending times to be midnight in UTC time
//        val startDate = date.atStartOfDay(defaultZone).withZoneSameInstant(utc)
//        val start = startDate.toInstant().toEpochMilli()
//        val end = startDate.plusDays(1).toInstant().toEpochMilli()

//        ZLog.i(ZTag.TAG, "abcd ${start - DateUtil.getDayStart(System.currentTimeMillis())}")
//        ZLog.i(ZTag.TAG, "abcd ${end - DateUtil.getDayEnd(System.currentTimeMillis())}")

        // This will keep a map of all of the events per package name
        val sortedEvents = mutableMapOf<String, MutableList<UsageEvents.Event>>()
        val usageStatsManager = AppUsageHelper.getUsageStatsManager(this) ?: return emptyList()
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
        sortedEvents.forEach { packageName, events ->
            // Keep track of the current start and end times
            var startTime = 0L
            var endTime = 0L
            // Keep track of the total usage time for this app
            var totalTime = 0L
            // Keep track of the start times for this app
            var latestStartTime = start
//            var latestEventTime = 0L
            events.forEach {
//                if (latestEventTime == 0L) {
//                    latestEventTime = it.timeStamp
//                }
//                ZLog.i(
//                    ZTag.TAG,
//                    "event: $packageName; ${
//                        DateUtil.timeMillisToString(
//                            it.timeStamp,
//                            "HH:mm:ss"
//                        )
//                    }; ${it.timeStamp - latestEventTime}; ${traceEvent(it.eventType)}"
//                )
                if (it.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    startTime = it.timeStamp
                    latestStartTime = startTime
                } else if (it.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    endTime = it.timeStamp
                    latestStartTime = endTime
                }
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
//            ZLog.i(ZTag.TAG, "totalTime: $totalTime")
            val stat = Stat(packageName, totalTime)
            ZLog.i(ZTag.TAG, "stat: $stat")
            stats.add(stat)
        }
        return stats
    }

    data class Stat(
        val packageName: String,
        val totalTime: Long
    ) {
        override fun toString(): String {
            return "Stat--$packageName: ${totalTime / 1000};"
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
            else -> {
                "($eventType)"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        run = false
    }
}
