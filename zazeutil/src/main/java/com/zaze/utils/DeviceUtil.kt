package com.zaze.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.util.*


/**
 * Description :

 * @author : ZAZE
 * *
 * @version : 2017-08-01 - 13:43
 */
object DeviceUtil {
    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * Description : 设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置
     * @author zaze
     * @version 2017/8/1 - 下午1:52 1.0
     */
    @JvmStatic
    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getDeviceId(context: Context): String? {
        return (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getSimSerialNumber(context: Context): String? {
        return (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simSerialNumber
    }

    /**
     * SimSerialNumber -> DeviceId(IMEI) -> AndroidId -> randomId
     * [context] context
     */
    @JvmStatic
    fun getUUID(context: Context): String {
        val key = "getUUID"
        var id = Build.SERIAL
        if (TextUtils.isEmpty(id)) {
            id = getSimSerialNumber(context)
            if (TextUtils.isEmpty(id)) {
                id = getDeviceId(context)
                if (TextUtils.isEmpty(id)) {
                    id = getAndroidId(context)
                    if ("9774d56d682e549c" == id) {
                        val sharedPrefUtil = SharedPrefUtil.newInstance(context)
                        id = sharedPrefUtil.get(key, "")
                        if (TextUtils.isEmpty(id)) {
                            id = UUID.randomUUID().toString()
                            sharedPrefUtil.commit(key, id)
                        }
                    }
                }
            }
        }
        return id
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
