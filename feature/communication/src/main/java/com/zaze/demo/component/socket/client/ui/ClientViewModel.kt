package com.zaze.demo.component.socket.client.ui

import com.zaze.common.base.AbsViewModel
import com.zaze.demo.component.socket.BaseSocketClient
import com.zaze.demo.component.socket.BaseSocketClient.BaseSocketFace
import com.zaze.demo.component.socket.MessageType
import com.zaze.demo.component.socket.SocketMessage
import com.zaze.demo.component.socket.UDPSocketClient
import org.json.JSONException
import org.json.JSONObject
import java.net.InetSocketAddress
import java.util.HashMap

class ClientViewModel : AbsViewModel() {
    val fromId: Long = 32142
    val toId: Long = 666L

    private val targetClients = HashMap<String, Pair<String, Int>>()

    private val GROUP_HOST = "224.0.0.1"
    private val GROUP_PORT = 8003

    /**
     * 监听组播地址
     */
    private val inviteSocket: BaseSocketClient =
        UDPSocketClient(GROUP_HOST, GROUP_PORT, object : BaseSocketFace() {
            override fun onPresence(socketMessage: SocketMessage?) {
                super.onPresence(socketMessage)
                if (socketMessage == null) return
                targetClients["${socketMessage.address}:${socketMessage.port}"] =
                    Pair(socketMessage.address, socketMessage.port)
                //                EventBus.getDefault().post(JsonUtil.objToJson(socketMessage));
//                ThreadManager.getInstance().runInUIThread { showServerInviteList(list) }
            }
        })

    private val clientSocket = UDPSocketClient(8004, object : BaseSocketFace() {
        override fun onReceiver(socketMessage: SocketMessage?) {
            super.onReceiver(socketMessage)
//            messageList.add(socketMessage)
//            ThreadManager.getInstance().runInUIThread { showReceiverMsg(messageList) }
        }
    })


    fun startClient() {
        inviteSocket.receive()
        clientSocket.receive()
    }

    fun stopClient() {
        inviteSocket.close()
        clientSocket.close()
    }

    fun sendGroupBroadCast() {
        val jsonObject = JSONObject()
        jsonObject.put("content", "组播邀请")
        clientSocket.send(
            GROUP_HOST, GROUP_PORT, SocketMessage(
                fromId,
                toId,
                jsonObject.toString(),
                MessageType.PRESENCE
            )
        )
    }

    fun send() {
        sendGroupBroadCast()
        try {
            targetClients.values.forEach {
                val jsonObject = JSONObject()
                jsonObject.put("content", "客户端回执")
                clientSocket.send(
                    it.first, it.second,
                    SocketMessage(
                        fromId,
                        toId,
                        jsonObject.toString(),
                        MessageType.CHAT
                    )
                )
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

}