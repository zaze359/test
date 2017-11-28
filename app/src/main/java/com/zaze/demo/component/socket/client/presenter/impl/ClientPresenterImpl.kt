package com.zaze.demo.component.socket.client.presenter.impl

import com.zaze.common.base.ZBasePresenter
import com.zaze.demo.component.socket.SocketClient
import com.zaze.demo.component.socket.SocketMessage
import com.zaze.demo.component.socket.UDPSocketClient
import com.zaze.demo.component.socket.client.presenter.ClientPresenter
import com.zaze.demo.component.socket.client.view.ClientView
import com.zaze.utils.ThreadManager
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import org.json.JSONObject
import java.net.SocketAddress
import java.util.*


/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:52 1.0
 */
open class ClientPresenterImpl(view: ClientView) : ZBasePresenter<ClientView>(view), ClientPresenter {

    val inviteSocket: SocketClient
    val clientSocket: SocketClient
    val list: ArrayList<SocketMessage> = ArrayList()
    val serviceSet: HashSet<SocketAddress> = HashSet()

    init {
        inviteSocket = UDPSocketClient("224.0.0.1", 8003, { message ->
            serviceSet.add(message.socketAdress)
            ZLog.i(ZTag.TAG_DEBUG, "收到邀请 ： " + message)
        })
        clientSocket = UDPSocketClient("", 8004, { message ->
            list.add(message)
            ThreadManager.getInstance().runInUIThread({
                view.showReceiverMsg(list)
            })
            ZLog.i(ZTag.TAG_DEBUG, "收到消息 ： " + message)
        })
    }

    override fun joinGroup() {
        inviteSocket.receive()
        clientSocket.receive()
    }

    override fun stop() {
        inviteSocket.close()
        clientSocket.close()
    }

    override fun send() {
        val jsonObject = JSONObject()
        jsonObject.put("content", "客户端回执")
        jsonObject.put("time", System.currentTimeMillis())
        serviceSet.map {
            clientSocket.send(it, jsonObject)
        }
    }

}
