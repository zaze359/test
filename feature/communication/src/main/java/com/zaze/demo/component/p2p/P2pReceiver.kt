package com.zaze.demo.component.p2p

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager

class P2pReceiver : BroadcastReceiver() {

    companion object {
        fun register(context: Context) {
            val filter = IntentFilter();
            filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            context.registerReceiver(P2pReceiver(), filter)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                P2pClient.requestPeers()
            }
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
            }
        }
    }
}