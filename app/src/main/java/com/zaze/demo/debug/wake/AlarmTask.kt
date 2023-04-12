package com.zaze.demo.debug.wake

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.zaze.demo.feature.communication.broadcast.MessageReceiver
import com.zaze.utils.date.DateUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2020-11-09 - 17:00
 */
class AlarmTask(private val context: Context) : NormalTask() {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    init {
        once = true
    }

    override fun doTask() {
        super.doTask()
        alarmManager?.let { it ->
            val time = System.currentTimeMillis() + 6000
            ZLog.i(ZTag.TAG, "send Alarm: ${DateUtil.timeMillisToString(time, "yyyy-MM-dd HH:mm:ss")} ($time)")
            val newIntent = Intent(MessageReceiver.ACTION_MESSAGE)
            newIntent.putExtra(MessageReceiver.ID, 11)
            val pi = PendingIntent.getBroadcast(context, 11, newIntent, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pi)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                it.setExact(AlarmManager.RTC_WAKEUP, time, pi)
            } else {
                it[AlarmManager.RTC_WAKEUP, time] = pi
            }
        }
    }

    override fun mode(): String {
        return "AlarmTask"
    }
}