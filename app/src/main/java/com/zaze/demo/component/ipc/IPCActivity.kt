package com.zaze.demo.component.ipc

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import com.zaze.common.base.AbsActivity
import com.zaze.demo.debug.MessengerService
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 14:38
 */
class IPCActivity : AbsActivity() {
    val write = Parcel.obtain().apply {
        writeInt(2233)
    }
    val reply = Parcel.obtain()

    private var messengerThread = HandlerThread("messenger_thread").apply { start() }
    private val messenger = Messenger(object : Handler(messengerThread.looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            ZLog.i(ZTag.TAG_DEBUG, "receiver messenger reply message")
            try {
                val serviceManager = Class.forName("android.os.ServiceManager");
                val method = serviceManager.getMethod("getService", String::class.java)
                val testBinder = method.invoke(null, "testBinder") as Binder
                ZLog.v(ZTag.TAG_DEBUG, "get testBinder success :$testBinder");
                testBinder.transact(0, write, reply, 0)
                val replayMessage = reply.readInt()
                ZLog.v(ZTag.TAG_DEBUG, "receiver testBinder message :$replayMessage");
            } catch (e: Exception) {
                ZLog.e(ZTag.TAG_DEBUG, "get testBinder fail");
            }
        }
    })
    private var sendMessenger: Messenger? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            sendMessenger = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            sendMessenger = Messenger(service)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindService(
            Intent(this, MessengerService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}