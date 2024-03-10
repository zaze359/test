package com.zaze.demo.component.socket.server.presenter.impl

import com.zaze.common.base.BaseApplication
import com.zaze.common.base.mvp.BaseMvpPresenter
import com.zaze.demo.component.socket.server.presenter.ServerPresenter
import com.zaze.demo.component.socket.server.view.ServerView
import com.zaze.demo.component.socket.BaseSocketClient
import com.zaze.demo.component.socket.MessageType
import com.zaze.demo.component.socket.SocketMessage
import com.zaze.demo.component.socket.UDPSocketClient
import com.zaze.utils.NetUtil
import com.zaze.utils.ThreadManager
import org.json.JSONObject
import java.net.InetSocketAddress
import java.util.*


/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:53 1.7
 */
open class ServerPresenterImpl(view: ServerView) : BaseMvpPresenter<ServerView>(view),
    ServerPresenter {
    val list: ArrayList<SocketMessage> = ArrayList()
    val serverSocket: BaseSocketClient
    val clientSet: HashSet<InetSocketAddress> = HashSet()
    val fromId = 666L
    val toId = 233L
//    private var wakeLock: PowerManager.WakeLock? = null

    init {
        serverSocket = UDPSocketClient(
            "",
            8004,
            object :
                BaseSocketClient.BaseSocketFace() {
                override fun onChat(socketMessage: SocketMessage?) {
                    super.onChat(socketMessage)
                    if (socketMessage != null) {
                        clientSet.add(InetSocketAddress(socketMessage.address, socketMessage.port))
                        list.add(socketMessage)
                        ThreadManager.getInstance().runInUIThread {
                            view.showReceiverMsg(list)
                        }
                        notification(socketMessage)
                        replay()
                    }
                }
            })
    }

    private fun notification(socketMessage: SocketMessage?) {
//        val builder = NotificationCompat.Builder(view.context)
//        builder.setSmallIcon(R.mipmap.ic_launcher)
//        builder.setContentTitle("title")
//        builder.setContentText(JsonUtil.objToJson(socketMessage))
//        builder.setAutoCancel(true)
//        // 设置通知主题的意图
//        //        Intent resultIntent = new Intent(this, TaskActivity.class);
//        //        PendingIntent resultPendingIntent = PendingIntent.getActivity(
//        //                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        //        builder.setContentIntent(resultPendingIntent);
//        val mNotificationManager = view.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
//        mNotificationManager!!.notify(1, builder.build())
    }

    override fun startServer() {
//        val pm = view.context.getSystemService(Context.POWER_SERVICE) as PowerManager?
//        wakeLock = pm!!.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock")
//        wakeLock?.acquire()

        view.showMessage(NetUtil.getIpAddress(BaseApplication.getInstance()))
//        TcpSocketClient.getInstance().connect("", 8003, object : TcpSocketClient.SocketFace {
//            override fun onEvent(id: Int, code: Int, description: String?) {
//                ZLog.d(ZTag.TAG_DEBUG, "TcpSocketClient onEvent:")
//            }
//
//            override fun onReceiver(id: Int, data: ByteArray?, dataLen: Int) {
//                ZLog.d(ZTag.TAG_DEBUG, "TcpSocketClient  onReceiver:")
//            }
//
//        })
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
        return SocketMessage(
            fromId,
            toId,
            message.toString(),
            msgType
        )
    }
}
