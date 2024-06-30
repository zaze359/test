package com.zaze.demo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-03 - 10:20
 */
class BatteryReceiver : BroadcastReceiver() {

    companion object {
        fun register(context: Context) {
            val filter = IntentFilter();
            filter.addAction(Intent.ACTION_BATTERY_CHANGED)
            context.registerReceiver(BatteryReceiver(), filter)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BATTERY_CHANGED -> {
                //获取当前电量
                val level = intent.getIntExtra("level", 0)
                //电量的总刻度
                val scale = intent.getIntExtra("scale", 100)
                //把它转成百分比
//                ZLog.i(ZTag.TAG, "电池电量为" + level * 100 / scale + "%")
            }
        }
    }
}