package com.zaze.demo.debug

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PowerManager
import com.zaze.common.base.AbsActivity
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2020-07-23 - 10:17
 */
class SensorActivity : AbsActivity(), SensorEventListener {

    var sensorManager: SensorManager? = null
    var mSensor: Sensor? = null
    var mPowerManager: PowerManager? = null
    var mWakeLock: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        mPowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        sensorManager?.getSensorList(Sensor.TYPE_ALL)?.forEach { it ->
            ZLog.i(ZTag.TAG_DEBUG, " has sensor: $it")
        }
        mWakeLock =
            mPowerManager?.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "test:sensor")
    }

    override fun onResume() {
        super.onResume()
        //注册传感器,先判断有没有传感器
        if (mSensor != null)
            sensorManager?.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    override fun onDestroy() {
        super.onDestroy()
        //传感器取消监听
        sensorManager?.unregisterListener(this);
        //释放息屏
        if (mWakeLock?.isHeld == true)
            mWakeLock?.release();
        mWakeLock = null
        mPowerManager = null
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        ZLog.i(ZTag.TAG_DEBUG, " onSensorChanged: ${sensor}")
    }

    override fun onSensorChanged(event: SensorEvent) {
        ZLog.i(ZTag.TAG_DEBUG, " onSensorChanged: ${event.sensor}")

//        //得到方向的值
//        if (event.sensor.type === Sensor.TYPE_ORIENTATION) {
//            val x = event.values[SensorManager.DATA_X]
//            val y = event.values[SensorManager.DATA_Y]
//            val z = event.values[SensorManager.DATA_Z]
//            orientationView.setText("方向: $x, $y, $z")
//        } else if (event.sensor.type === Sensor.TYPE_MAGNETIC_FIELD) {
//            val x = event.values[SensorManager.DATA_X]
//            val y = event.values[SensorManager.DATA_Y]
//            val z = event.values[SensorManager.DATA_Z]
//            magneticView.setText("合磁场强度X、Y、Z分量: $x, $y, $z")
//            //计算和磁场强度
//            val total = Math.sqrt(Math.pow(x.toDouble(), 2.0) + Math.pow(y.toDouble(), 2.0) + Math.pow(z.toDouble(), 2.0)).toFloat()
//            totalMageticView.setText("合磁场强度: $total")
//        }
//
//        if (0.0 == event.values?.get(0) ?: 0) {
//            //贴近手机
//            ZLog.i(ZTag.TAG_DEBUG, " 贴近手机")
//            if (mWakeLock?.isHeld() == false)
//                mWakeLock?.acquire()
//        } else {
//            //离开手机
//            ZLog.i(ZTag.TAG_DEBUG, " 离开手机")
//            if (mWakeLock?.isHeld() == true)
//                mWakeLock?.release();
//        }
    }
}