package com.zaze.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import com.zaze.utils.NetUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2020-06-05 - 10:21
 */
object NetworkManger {
    /**
     * 网络状态
     */
    var currentNetwork = NetworkType()
        private set
        get

    /**
     * 更新网络状态
     * @return  网络状态 NetworkType
     */
    fun refreshNetwork(connectivityManager: ConnectivityManager, network: Network? = null): NetworkType {
        return getNetworkType(connectivityManager, network).apply {
            networkChanged(this)
        }
    }

    fun onAvailable(connectivityManager: ConnectivityManager, network: Network? = null) {
        refreshNetwork(connectivityManager, network)
    }

    fun onUnavailable() {
        currentNetwork.isAvailable = false
        networkChanged(currentNetwork)
    }

    fun onLost() {
        networkChanged(NetworkType())
    }

    private fun networkChanged(networkType: NetworkType) {
        currentNetwork = networkType
        ZLog.i(ZTag.TAG_NET, "networkChanged: $networkType")
    }

    // --------------------------------------------------
    /**
     * 获取当前网络连接状态
     *
     * @param context context
     * @return 连接状态字符串
     */
    fun getNetworkType(connectivityManager: ConnectivityManager, network: Network? = null): NetworkType {
        return NetworkType(when {
            network == null -> connectivityManager.activeNetworkInfo
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> connectivityManager.getNetworkInfo(network)
            else -> connectivityManager.activeNetworkInfo
        })
    }
}