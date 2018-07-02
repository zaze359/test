package com.zaze.demo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v4.net.ConnectivityManagerCompat
import com.zaze.utils.ThreadManager
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description : 监听网络变化
 * @author : ZAZE
 * @version : 2017-06-07 - 13:24
 */
class WifiReceiver : BroadcastReceiver() {
    companion object {
        private val callbackList = emptyList<WifiCallBack>()
        fun register(callback: WifiCallBack) {
            callbackList.plus(callback)
        }

        fun unRegister(callback: WifiCallBack) {
            callbackList.minus(callback)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val activeInfo = connectivityManager.activeNetworkInfo
            val networkInfo = ConnectivityManagerCompat.getNetworkInfoFromBroadcast(connectivityManager, intent);
            ZLog.i(ZTag.TAG_DEBUG, "mobileInfo : $mobileInfo")
            ZLog.i(ZTag.TAG_DEBUG, "wifiInfo : $wifiInfo")
            ZLog.i(ZTag.TAG_DEBUG, "activeInfo : $activeInfo")
            ZLog.i(ZTag.TAG_DEBUG, "getNetworkInfoFromBroadcast : " + "$networkInfo")
            if (activeInfo == null) {
                ZLog.e(ZTag.TAG_DEBUG, "无网络连接")
            } else {
                if (wifiInfo != null && wifiInfo.isConnected) {
                    ZLog.i(ZTag.TAG_DEBUG, "当前连接wifi")
                } else if (mobileInfo != null && mobileInfo.isConnected) {
                    ZLog.i(ZTag.TAG_DEBUG, "当前使用数据流量")
                } else {
                    ZLog.e(ZTag.TAG_DEBUG, "有网但是获取不到网络状态")
                }
            }
            ThreadManager.getInstance().runInUIThread({
                callbackList.map { callback ->
                    callback.onReceive(networkInfo)
                }
            })
        }
    }

    interface WifiCallBack {
        fun onReceive(networkInfo: NetworkInfo?)
    }
}