package com.zaze.aarrepo.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import java.util.*

/**
 * Description :

 * @author : ZAZE
 * *
 * @version : 2017-08-01 - 13:43
 */
object ZDeviceUtil {
    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * Description : 设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置
     * @author zaze
     * @version 2017/8/1 - 下午1:52 1.0
     */
    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getDeviceId(context: Context): String? {
        return (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
    }

    fun getMachineCode(context: Context): String {
        val uuid: UUID
        val androidId = getAndroidId(context)
        try {
            if ("9774d56d682e549c" != androidId) {
                uuid = UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
            } else {
                val deviceId = getDeviceId(context)
                uuid = if (deviceId != null) {
                    UUID.nameUUIDFromBytes(deviceId
                            .toByteArray(charset("utf8")))
                } else {
                    UUID.randomUUID()
                }
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return uuid.toString()
    }
    // --------------------------------------------------

    /**
     * @return 机器型号
     */
    fun getDeviceModel(): String {
        return Build.MODEL
    }

    /**
     * @return 系统版本
     */
    fun getSystemVersion(): String {
        return Build.VERSION.RELEASE
    }

    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * @return sdcard总空间大小
     */
    fun getSdTotalSpace(): Long {
        return ZFileUtil.getTotalSpace(Environment.getExternalStorageDirectory())
    }

    /**
     * @return sdcard 剩余空间大小
     */
    fun getSdFreeSpace(): Long {
        return ZFileUtil.getFreeSpace(Environment.getExternalStorageDirectory())
    }
    // --------------------------------------------------
    /**
     * Data (内置sd卡 时 同 {@link #getSDTotalSpace()})
     * @return 获取机身总大小
     */
    fun getDataTotalSpace(): Long {
        return ZFileUtil.getTotalSpace(Environment.getDataDirectory())
    }

    /**
     * Data
     * @return 获取机身剩余
     */
    fun getDataFreeSpace(): Long {
        return ZFileUtil.getFreeSpace(Environment.getDataDirectory())
    }

    // --------------------------------------------------
    /**
     * RAM
     * java 虚拟机 最大内存
     * @return
     */
    fun getVMMaxMemory(): Long {
        return Runtime.getRuntime().maxMemory()
    }

    /**
     * RAM
     * java 虚拟机 当前 从机器内存中取过来的 内存的 中的空闲内存
     * @return
     */
    fun getVMFreeMemory(): Long {
        return Runtime.getRuntime().freeMemory()
    }

    /**
     * RAM
     * java 虚拟机 当前 从机器内存中取过来 的 总内存(包括使用了的和 freeMemory)
     * @return
     */
    fun getVMTotalMemory(): Long {
        return Runtime.getRuntime().totalMemory()
    }

    // --------------------------------------------------
    // --------------------------------------------------

}