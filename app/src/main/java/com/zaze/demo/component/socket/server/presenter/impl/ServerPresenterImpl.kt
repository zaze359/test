package com.zaze.demo.component.socket.server.presenter.impl

import com.zaze.common.base.ZBasePresenter
import com.zaze.demo.component.socket.SocketClient
import com.zaze.demo.component.socket.SocketMessage
import com.zaze.demo.component.socket.UDPSocketClient
import com.zaze.demo.component.socket.server.presenter.ServerPresenter
import com.zaze.demo.component.socket.server.view.ServerView
import com.zaze.utils.ThreadManager
import org.json.JSONObject
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.util.*


/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:53 1.7
 */
open class ServerPresenterImpl(view: ServerView) : ZBasePresenter<ServerView>(view), ServerPresenter {
    val list: ArrayList<SocketMessage> = ArrayList()
    val serverSocket: SocketClient
    val clientSet: HashSet<SocketAddress> = HashSet()

    init {
        serverSocket = UDPSocketClient("", 8004, { message ->
            clientSet.add(message.socketAdress)
            list.add(message)
            ThreadManager.getInstance().runInUIThread({
                view.showReceiverMsg(list)
            })
        })
    }

    override fun startServer() {
        serverSocket.receive()
    }

    override fun stopServer() {
        serverSocket.close()
    }

    override fun sendBroadCast() {
        val jsonObject = JSONObject()
        jsonObject.put("content", "服务端邀请")
        jsonObject.put("time", System.currentTimeMillis())
        serverSocket.send(InetSocketAddress("224.0.0.1", 8003), jsonObject)
        clientSet.map {
            val replay = JSONObject()
            replay.put("content", "服务端回执")
            replay.put("time", System.currentTimeMillis())
            serverSocket.send(it, replay)
        }
    }
}
