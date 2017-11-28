package com.zaze.demo.component.socket.client.presenter.impl

import com.zaze.common.base.ZBasePresenter
import com.zaze.demo.component.socket.SocketHelper
import com.zaze.demo.component.socket.SocketMessage
import com.zaze.demo.component.socket.client.presenter.ClientPresenter
import com.zaze.demo.component.socket.client.view.ClientView
import com.zaze.utils.ThreadManager
import com.zaze.utils.ZStringUtil
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

    val inviteSocket: SocketHelper
    val clientSocket: SocketHelper
    val list: ArrayList<SocketMessage> = ArrayList()
    val inviteSet: HashSet<String> = HashSet()
    val serviceSet: HashSet<SocketAddress> = HashSet()

    init {
        inviteSet.add("172.22.0.94:8004")
        inviteSocket = SocketHelper({ message ->
            serviceSet.add(message.socketAdress)
            ZLog.i(ZTag.TAG_DEBUG, "收到邀请 ： " + message)
        })
        clientSocket = SocketHelper({ message ->
            list.add(message)
            ThreadManager.getInstance().runInUIThread({
                view.showReceiverMsg(list)
            })
            ZLog.i(ZTag.TAG_DEBUG, "收到消息 ： " + message)
        })
    }

    override fun joinGroup() {
        inviteSocket.joinGroup("224.0.0.1", 8003)
        clientSocket.joinGroup(8004)
    }

    override fun stop() {
        inviteSocket.stop()
        clientSocket.stop()
    }

    override fun send() {
        val jsonObject = JSONObject()
        jsonObject.put("content", "客户端回执")
        jsonObject.put("time", System.currentTimeMillis())
        inviteSet.map {
            val address = it.split(":")
            if (address.size == 2) {
                clientSocket.send(address[0], ZStringUtil.parseInt(address[1]), jsonObject)
            }
        }
        serviceSet.map {
            clientSocket.send(it, jsonObject)
        }
    }

}
