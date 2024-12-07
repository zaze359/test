package com.zaze.demo.component.p2p

import android.content.Context
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.WifiP2pManager.ActionListener
import android.os.Handler
import android.os.HandlerThread
import com.zaze.common.base.BaseApplication
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

object P2pClient : WifiP2pManager.PeerListListener {

    @JvmStatic
    @Volatile
    private var p2pThread = HandlerThread("p2p_thread").apply { start() }

//    /**
//     * 自定义Handler
//     */
//    @JvmStatic
//    @Volatile
//    private var p2pHandler = Handler(p2pThread.looper)

    private var channel: WifiP2pManager.Channel? = null

    val wifiP2pManager: WifiP2pManager?
        get() {
            return BaseApplication.getInstance()
                .getSystemService(Context.WIFI_P2P_SERVICE) as? WifiP2pManager
        }


    fun init() {
        channel = wifiP2pManager?.initialize(
            BaseApplication.getInstance(),
            p2pThread.looper,
            object : WifiP2pManager.ChannelListener {
                override fun onChannelDisconnected() {
                }

            })
    }

    fun discovery() {
        channel?.let {
            wifiP2pManager?.discoverPeers(it, object : ActionListener {
                override fun onSuccess() {
                    ZLog.d(ZTag.TAG_DEBUG, "discoverPeers onSuccess")
                    requestPeers()
                }

                override fun onFailure(p0: Int) {
                    ZLog.d(ZTag.TAG_DEBUG, "discoverPeers onFailure")
                }

            })
        }
    }

    fun requestPeers() {
        ZLog.d(ZTag.TAG_DEBUG, "requestPeers")
        channel?.let {
            wifiP2pManager?.requestPeers(it, this)
        }
    }

    override fun onPeersAvailable(p0: WifiP2pDeviceList?) {
        ZLog.d(ZTag.TAG_DEBUG, "onPeersAvailable ---------------------- ")
        p0?.deviceList?.forEach {
            ZLog.d(ZTag.TAG_DEBUG, "deviceName: ${it.deviceName}; deviceAddress: ${it.deviceAddress}")
        }
        ZLog.d(ZTag.TAG_DEBUG, "onPeersAvailable ---------------------- ")
    }

}