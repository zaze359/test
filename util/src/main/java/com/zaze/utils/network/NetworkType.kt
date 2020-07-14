package com.zaze.utils.network

import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-11-07 - 14:30
 *
 * not connect, available:          显示连接已保存, 但标题栏没有, 即没有连接上
 * not connect, available:          选择不保存后
 * not connect, not available:      选择连接，在正在获取IP地址时
 * connect, available:              连接上后
 * connect, available:              显示连接已保存，标题栏也有已连接上的图标
 */
class NetworkType(networkInfo: NetworkInfo? = null) {
    val netType: Int
    var isAvailable: Boolean
    var isConnected: Boolean

    init {
        netType = when (networkInfo) {
            null -> {
                NULL
            }
            else -> when (networkInfo.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    WIFI
                }
                ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_MOBILE_HIPRI,
                ConnectivityManager.TYPE_MOBILE_DUN,
                ConnectivityManager.TYPE_MOBILE_SUPL,
                ConnectivityManager.TYPE_MOBILE_MMS -> {
                    MOBILE
                }
                else -> {
                    NULL
                }
            }
        }
        ZLog.i(ZTag.TAG, "networkInfo: $networkInfo")
        isAvailable = networkInfo?.isAvailable ?: false
        isConnected = networkInfo?.isConnected ?: false

    }

    companion object {
        const val NULL = 0

        /**
         * WIFI
         */
        const val WIFI = 10

        /**
         *  数据流量
         */
        const val MOBILE = 20
    }

    /**
     * 当前无网络
     */
    fun isNull(): Boolean {
        return netType == NULL
    }

    /**
     * 是可用的 WLAN
     */
    fun isWlanAvailable(): Boolean {
        return netType == WIFI && isEnable()
    }

    /**
     * 是可用的 Mobile
     */
    fun isMobileAvailable(): Boolean {
        return netType == MOBILE && isEnable()
    }

    /**
     * 是否可用
     */
    fun isEnable(): Boolean {
        return isAvailable
    }

    override fun toString(): String {
        return "NetworkType(netType=$netType, isAvailable=$isAvailable, isConnected=$isConnected)"
    }
}