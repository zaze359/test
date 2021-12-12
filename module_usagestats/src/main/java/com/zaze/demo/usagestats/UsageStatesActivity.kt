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
                    if (!AppUsageHelper.checkAppUsagePermission(this@UsageStatesActivity)) {
                        AppUsageHelper.requestAppUsagePermission(this@UsageStatesActivity)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            ZLog.i(
                                TAG,
                                "getTopActivityPackageName: ${
                                    AppUsageHelper.getTopActivityPackageName(this@UsageStatesActivity)
                                }"
                            )
                        }
                    }
                    SystemClock.sleep(1000L)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.usage_states_act)
//        thread.start()
        usageStateBtn.setOnClickListener {
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
                    ZLog.i(ZTag.TAG, "queryEventStats: ${getDailyStats().joinToString()}")

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
    }

    //    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDailyStats(date: LocalDate = LocalDate.now()): List<Stat> {
        // The timezones we'll need

        ZLog.i(ZTag.TAG, "date: $date")
        val utc = ZoneId.of("UTC")
        val defaultZone = ZoneId.systemDefault()

        // Set the starting and ending times to be midnight in UTC time
        val startDate = date.atStartOfDay(defaultZone).withZoneSameInstant(utc)
        val start = startDate.toInstant().toEpochMilli()
        val end = startDate.plusDays(1).toInstant().toEpochMilli()

        ZLog.i(ZTag.TAG, "${start - DateUtil.getDayStart(System.currentTimeMillis())}")
        ZLog.i(ZTag.TAG, "${end - DateUtil.getDayEnd(System.currentTimeMillis())}")

        // This will keep a map of all of the events per package name
        val sortedEvents = mutableMapOf<String, MutableList<UsageEvents.Event>>()

        // Query the list of events that has happened within that time frame
        val usageStatsManager = AppUsageHelper.getUsageStatsManager(this) ?: return emptyList()
        TraceHelper.beginSection("queryEvents")
        val systemEvents = usageStatsManager.queryEvents(start, end)
        TraceHelper.endSection("queryEvents")
        var i = 0;
        while (systemEvents.hasNextEvent()) {
            i++
            val event = UsageEvents.Event()
            systemEvents.getNextEvent(event)

            // Get the list of events for the package name, create one if it doesn't exist
            val packageEvents = sortedEvents[event.packageName] ?: mutableListOf()
            packageEvents.add(event)
            sortedEvents[event.packageName] = packageEvents
        }
        ZLog.i(ZTag.TAG, "systemEvents: $i")
        // This will keep a list of our final stats
        val stats = mutableListOf<Stat>()

        // Go through the events by package name
//        val events = sortedEvents.get("com.zaze.demo")

        TraceHelper.beginSection("sortedEvents")
        sortedEvents.forEach { packageName, events ->
            // Keep track of the current start and end times
            var startTime = 0L
            var endTime = 0L
            // Keep track of the total usage time for this app
            var totalTime = 0L
            // Keep track of the start times for this app
            val startTimes = mutableListOf<ZonedDateTime>()
            var latestStartTime = start

            var latestEventTime = 0L
            events.forEach {
                if (latestEventTime == 0L) {
                    latestEventTime = it.timeStamp
                }
                ZLog.i(
                    ZTag.TAG,
                    "event: $packageName; ${
                        DateUtil.timeMillisToString(
                            it.timeStamp,
                            "HH:mm:ss"
                        )
                    }; ${it.timeStamp - latestEventTime}; ${traceEvent(it.eventType)}"
                )
                latestEventTime = it.timeStamp
                if (it.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    // App was moved to the foreground: set the start time
                    startTime = it.timeStamp
                    // Add the start time within this timezone to the list
                    latestStartTime = startTime
                } else if (it.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    // App was moved to background: set the end time
                    endTime = it.timeStamp
                    latestStartTime = endTime
                }

                // If there's an end time with no start time, this might mean that
                //  The app was started on the previous day, so take midnight
                //  As the start time
                if (startTime == 0L && endTime != 0L) {
                    startTime = latestStartTime
                }
                // If both start and end are defined, we have a session
                if (startTime != 0L && endTime != 0L) {
                    // Add the session time to the total time
                    totalTime += endTime - startTime
                    ZLog.i(ZTag.TAG, "$packageName; totalTime: $totalTime")
                    // Reset the start/end times to 0
                    startTime = 0L
                    endTime = 0L
                }
            }
            // If there is a start time without an end time, this might mean that
            //  the app was used past midnight, so take (midnight - 1 second)
            //  as the end time
            if (startTime != 0L && endTime == 0L) {
                totalTime += System.currentTimeMillis() - 1000 - startTime
            }
//            ZLog.i(ZTag.TAG, "totalTime: $totalTime")
            val stat = Stat(packageName, totalTime)
            ZLog.i(ZTag.TAG, "stat: $stat")
            stats.add(stat)
        }
        TraceHelper.endSection("sortedEvents")
        return stats
    }

    data class Stat(
        val packageName: String,
        val totalTime: Long
    ) {
        override fun toString(): String {
            return "packageName: $packageName; totalTime: ${totalTime / 1000}"
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
