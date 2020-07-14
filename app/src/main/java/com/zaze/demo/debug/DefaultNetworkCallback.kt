package com.zaze.demo.debug

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import com.zaze.utils.network.NetworkManger
import net.contrapunctus.lzma.Version.context

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-11-07 - 14:04
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DefaultNetworkCallback(private val connectivityManager: ConnectivityManager) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        ZLog.d(ZTag.TAG, "onAvailable network: ${connectivityManager.getNetworkInfo(network)})")
        NetworkManger.onAvailable(connectivityManager, network)
    }

    override fun onUnavailable() {
        super.onUnavailable()
        ZLog.w(ZTag.TAG, "onUnavailable ")
        NetworkManger.onUnavailable()
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        ZLog.d(ZTag.TAG, "onLinkPropertiesChanged network: ${connectivityManager.getNetworkInfo(network)})")
        ZLog.d(ZTag.TAG, "onLinkPropertiesChanged linkProperties: $linkProperties ")
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        ZLog.d(ZTag.TAG, "onLosing network: ${connectivityManager.getNetworkInfo(network)})   maxMsToLive : $maxMsToLive")
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        ZLog.w(ZTag.TAG, "onLost network: ${connectivityManager.getNetworkInfo(network)})")
        NetworkManger.onLost()
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        ZLog.d(ZTag.TAG, "onCapabilitiesChanged network: ${connectivityManager.getNetworkInfo(network).isConnected}")
        ZLog.d(ZTag.TAG, "onCapabilitiesChanged networkCapabilities: $networkCapabilities")
    }


}