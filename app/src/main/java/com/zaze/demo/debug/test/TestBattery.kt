package com.zaze.demo.debug.test

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.util.Log

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-30 - 15:58
 */
class TestBattery : ITest {
    override fun doTest(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            Log.i(
                "battery",
                "percent : ${batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)}%"
            )
        } else {
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                ?.let { batteryInfoIntent ->
                    val level = batteryInfoIntent.getIntExtra("level", 0)
                    val batterySum = batteryInfoIntent.getIntExtra("scale", 100)
                    val percentBattery = 100 * level / batterySum
                    Log.i("battery", "level = $level")
                    Log.i("battery", "batterySum = $batterySum")
                    Log.i("battery", "percent is $percentBattery%")
                }
        }
        Log.i("battery", "currentTimeMillis: ${System.currentTimeMillis()}")
    }
}