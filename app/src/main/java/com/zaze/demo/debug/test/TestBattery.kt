package com.zaze.demo.debug.test

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import com.zaze.utils.log.ZLog

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-30 - 15:58
 */
class TestBattery : ITest {
    override fun doTest(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

            ZLog.i(
                "battery",
                "BATTERY_PROPERTY_CHARGE_COUNTER 获取电池剩余容量，单位是微安时（µAh）: ${batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)}"
            )
            ZLog.i(
                "battery",
                "BATTERY_PROPERTY_CURRENT_NOW 获取瞬时电池电流，单位是微安（µA）。正值充电，负值放电: ${batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)}"
            )
            ZLog.i(
                "battery",
                "BATTERY_PROPERTY_CURRENT_AVERAGE 获取平均电池电流，单位是微安（µA）正值充电，负值放电: ${batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)}"
            )
            ZLog.i(
                "battery",
                "BATTERY_PROPERTY_ENERGY_COUNTER 获取剩余电量的百分比: ${batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)}%"
            )
            ZLog.i(
                "battery",
                "BATTERY_PROPERTY_ENERGY_COUNTER 获取剩余能量，单位是纳瓦时（nWh）: ${batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER)}"
            )
            ZLog.i(
                "battery",
                "BATTERY_PROPERTY_STATUS 获取电池当前的充电状态: ${batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)}"
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
        ZLog.i("battery", "currentTimeMillis: ${System.currentTimeMillis()}")
    }
}