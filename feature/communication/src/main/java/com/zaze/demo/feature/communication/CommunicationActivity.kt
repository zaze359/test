package com.zaze.demo.feature.communication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.myViewModels
import com.zaze.demo.feature.communication.aidl.RemoteService
import com.zaze.demo.feature.communication.messenger.MessengerService

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 14:38
 */
class CommunicationActivity : AbsActivity() {

    private val viewModel: CommunicationViewModel by myViewModels()

    private val messengerServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.serviceMessenger = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            viewModel.serviceMessenger = Messenger(service)
        }
    }

    private val remoteServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.remoteService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            viewModel.remoteService = IRemoteService.Stub.asInterface(service)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindService(
            Intent(this, MessengerService::class.java),
            messengerServiceConnection,
            Context.BIND_AUTO_CREATE
        )
        bindService(
            Intent(this, RemoteService::class.java),
            remoteServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(messengerServiceConnection)
        unbindService(remoteServiceConnection)
    }
}