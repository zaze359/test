package com.zaze.demo.feature.communication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.zaze.utils.log.ZLog

object RemoteServiceManager {

    private var remoteService: IRemoteService? = null

    private val deathRecipient  = IBinder.DeathRecipient {
        ZLog.i("CommunicationViewModel", "remoteService binderDied")
        onServiceDisconnected()
        // 重连？
    }

    private val remoteServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            onServiceDisconnected()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            onConnected(service)
        }
    }

    private fun onServiceDisconnected() {
        ZLog.i("CommunicationViewModel", "onRemoteServiceDisconnected")
        remoteService?.asBinder()?.unlinkToDeath(deathRecipient, 0)
        remoteService = null
    }

    private fun onConnected(service: IBinder?) {
        remoteService = IRemoteService.Stub.asInterface(service)
        service?.unlinkToDeath(deathRecipient, 0)
        service?.linkToDeath(deathRecipient, 0)
    }

    fun connectService(context: Context) {
        val serviceIntent = Intent("com.zaze.export.remoteService")
        serviceIntent.setPackage("com.zaze.demo")
//        serviceIntent.setComponent(ComponentName("com.zaze.demo", "com.zaze.demo.feature.communication.aidl.RemoteService"))
        context.startService(serviceIntent)
        context.bindService(serviceIntent, remoteServiceConnection, Context.BIND_AUTO_CREATE)
    }

    fun disconnectService(context: Context) {
        context.unbindService(remoteServiceConnection)
    }
}