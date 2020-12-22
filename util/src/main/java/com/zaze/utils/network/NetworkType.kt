package com.zaze.utils.network

import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * 当连接上指定网络时: isAvailable = true,isConnected = true,
 *  Doze模式下 isConnected = false
 *  App Standby 模式下 切后台时 isConnected = false
 * @author : ZAZE
 * @version : 2019-11-07 - 14:30
 */
class NetworkType(networkInfo: NetworkInfo? = null) {
    val type: Int
    var isAvailable: Boolean
    var isConnected: Boolean

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

    init {
        type = when (networkInfo) {
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
        ZLog.i(ZTag.TAG_NET, "networkInfo: $networkInfo")
        isAvailable = networkInfo?.isAvailable ?: false
        isConnected = networkInfo?.isConnected ?: false
    }

    /**
     * 当前无网络
     */
    fun isNull(): Boolean {
        return type == NULL
    }

    /**
     * 是可用的 WLAN
     */
    fun isWlanAvailable(): Boolean {
        return type == WIFI && isAvailable
    }

    /**
     * 是可用的 Mobile
     */
    fun isMobileAvailable(): Boolean {
        return type == MOBILE && isAvailable
    }

    fun isEnable(): Boolean {
        return isAvailable && isConnected
    }

    override fun toString(): String {
        return "NetworkType(type=$type, isAvailable=$isAvailable, isConnected=$isConnected)"
    }
}