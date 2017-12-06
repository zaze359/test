package com.zaze.demo.component.socket.server.presenter.impl

import android.content.Context
import android.os.PowerManager
import com.zaze.common.base.ZBasePresenter
import com.zaze.demo.component.socket.BaseSocketClient
import com.zaze.demo.component.socket.MessageType
import com.zaze.demo.component.socket.SocketMessage
import com.zaze.demo.component.socket.UDPSocketClient
import com.zaze.demo.component.socket.server.presenter.ServerPresenter
import com.zaze.demo.component.socket.server.view.ServerView
import com.zaze.utils.ThreadManager
import org.json.JSONObject
import java.net.InetSocketAddress
import java.util.*


/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:53 1.7
 */
open class ServerPresenterImpl(view: ServerView) : ZBasePresenter<ServerView>(view), ServerPresenter {
    val list: ArrayList<SocketMessage> = ArrayList()
    val serverSocket: BaseSocketClient
    val clientSet: HashSet<InetSocketAddress> = HashSet()
    val fromId = 666L
    val toId = 233L
//    private var wakeLock: PowerManager.WakeLock? = null

    init {
        serverSocket = UDPSocketClient("", 8004, object : BaseSocketClient.BaseSocketFace() {
            override fun onChat(socketMessage: SocketMessage?) {
                super.onChat(socketMessage)
                if (socketMessage != null) {
                    clientSet.add(InetSocketAddress(socketMessage.address, socketMessage.port))
                    list.add(socketMessage)
                    ThreadManager.getInstance().runInUIThread({
                        view.showReceiverMsg(list)
                    })
                    replay()
                }
            }
        })
    }

    override fun startServer() {
        val pm = view.context.getSystemService(Context.POWER_SERVICE) as PowerManager?
//        wakeLock = pm!!.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock")
//        wakeLock?.acquire()
        serverSocket.receive()
    }

    override fun stopServer() {
        serverSocket.close()
//        wakeLock?.release()
    }

    override fun sendBroadCast() {
        val jsonObject = JSONObject()
        jsonObject.put("content", "服务端邀请")
        serverSocket.send("224.0.0.1", 8003, buildMessage(toId, jsonObject, MessageType.PRESENCE))
        replay()
    }

    fun replay() {
        clientSet.map {
            val replay = JSONObject()
            replay.put("content", "服务端回执")
            serverSocket.send(it.address.hostAddress, it.port, buildMessage(toId, replay, MessageType.CHAT))
        }
    }

    private fun buildMessage(toId: Long, message: JSONObject, msgType: Int): SocketMessage {
        return SocketMessage(fromId, toId, message.toString(), msgType)
    }
}
