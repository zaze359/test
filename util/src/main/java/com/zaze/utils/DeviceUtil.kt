package com.zaze.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import java.util.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2017-08-01 - 13:43
 */
@SuppressLint("HardwareIds")
object DeviceUtil {
    /**
     * 设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置
     */
    @JvmStatic
    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getDeviceId(context: Context): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getTelephonyManager(context).imei
            } else {
                getTelephonyManager(context).deviceId
            }
        } catch (e: SecurityException) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getSimSerialNumber(context: Context): String? {
        return try {
            getTelephonyManager(context).simSerialNumber
        } catch (e: SecurityException) {
            // ignore
            null
        }
    }

    @JvmStatic
    fun getTelephonyManager(context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    /**
     * SimSerialNumber -> DeviceId(IMEI) -> AndroidId -> randomId
     * [context] context
     */
    @JvmStatic
    fun getUUID(context: Context): String {
        val key = "getUUID"
        var id = getSerial()
        if (id.isNullOrEmpty() || id == "unknown") {
            id = getSimSerialNumber(context)
        } else return id
        if (id.isNullOrEmpty()) {
            id = getDeviceId(context)
        } else return id
        if (id.isNullOrEmpty()) {
            id = getAndroidId(context)
        } else return id
        if (id.isNullOrEmpty() || "9774d56d682e549c" == id) {
            id = SharedPrefUtil.newInstance(context)[key, ""]
        } else return id
        if (id.isNullOrEmpty()) {
            id = UUID.randomUUID().toString()
            SharedPrefUtil.newInstance(context).commit(key, id)
        }
        return id
    }

    /**
     *
     */
    fun getSerial(): String? {
        return try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    Build.getSerial()
                }
                else -> {
                    Build.SERIAL
                }
            }
        } catch (e: SecurityException) {
            null
        }
    }

// --------------------------------------------------
// --------------------------------------------------
    /**
     * @return sdcard总空间大小
     */
    @JvmStatic
    fun getSdTotalSpace(): Long {
        return FileUtil.getTotalSpace(Environment.getExternalStorageDirectory())
    }

    /**
     * @return sdcard 剩余空间大小
     */
    @JvmStatic
    fun getSdFreeSpace(): Long {
        return FileUtil.getFreeSpace(Environment.getExternalStorageDirectory())
    }
// --------------------------------------------------
    /**
     * Data (内置sd卡 时 同 {@link #getSDTotalSpace()})
     * @return 获取机身总大小
     */
    @JvmStatic
    fun getDataTotalSpace(): Long {
        return FileUtil.getTotalSpace(Environment.getDataDirectory())
    }

    /**
     * Data
     * @return 获取机身剩余
     */
    @JvmStatic
    fun getDataFreeSpace(): Long {
        return FileUtil.getFreeSpace(Environment.getDataDirectory())
    }

// --------------------------------------------------
    /**
     * Runtime Max Memory
     * 单个应用 最大运存
     * @return
     */
    @JvmStatic
    fun getRuntimeMaxMemory(): Long {
        return Runtime.getRuntime().maxMemory()
    }

    /**
     * Runtime Free Memory
     * 当前 从机器内存中取过来的 内存的 中的空闲内存
     * @return
     */
    @JvmStatic
    fun getRuntimeFreeMemory(): Long {
        return Runtime.getRuntime().freeMemory()
    }

    /**
     * Runtime Total Memory
     * 当前 从机器内存中取过来 的 总内存(包括使用了的和 freeMemory)
     * @return
     */
    @JvmStatic
    fun getRuntimeTotalMemory(): Long {
        return Runtime.getRuntime().totalMemory()
    }

    // --------------------------------------------------
    // --------------------------------------------------

    @JvmStatic
    fun getDeviceMemory(context: Context): ActivityManager.MemoryInfo {
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val outInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(outInfo)
        return outInfo
    }

}
