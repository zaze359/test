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
        ZLog.d(ZTag.TAG_NET, "onAvailable network: ${connectivityManager.getNetworkInfo(network)})")
        NetworkManger.onAvailable(connectivityManager, network)
    }

    override fun onUnavailable() {
        super.onUnavailable()
        ZLog.w(ZTag.TAG_NET, "onUnavailable ")
        NetworkManger.onUnavailable()
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        ZLog.d(ZTag.TAG_NET, "onLinkPropertiesChanged network: ${connectivityManager.getNetworkInfo(network)})")
//        ZLog.d(ZTag.TAG_NET, "onLinkPropertiesChanged linkProperties: $linkProperties ")
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
//        ZLog.d(ZTag.TAG_NET, "onLosing network: ${connectivityManager.getNetworkInfo(network)})   maxMsToLive : $maxMsToLive")
//        ZLog.d(ZTag.TAG_NET, "onLosing network: ${connectivityManager.activeNetworkInfo})")
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        ZLog.w(ZTag.TAG_NET, "onLost network: ${connectivityManager.getNetworkInfo(network)})")
        ZLog.d(ZTag.TAG_NET, "onLost network: ${connectivityManager.activeNetworkInfo})")
        NetworkManger.onLost()
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        ZLog.d(ZTag.TAG_NET, "onCapabilitiesChanged network: ${connectivityManager.getNetworkInfo(network)?.isConnected}")
//        ZLog.d(ZTag.TAG_NET, "onCapabilitiesChanged networkCapabilities: $networkCapabilities")
    }


}