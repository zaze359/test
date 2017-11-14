package com.zaze.demo.component.socket.server.presenter.impl

import com.zaze.common.base.ZBasePresenter
import com.zaze.demo.app.MyApplication
import com.zaze.demo.component.socket.server.presenter.ServerPresenter
import com.zaze.demo.component.socket.server.view.ServerView
import com.zaze.utils.ThreadManager
import com.zaze.utils.ZNetUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import org.json.JSONObject
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.MulticastSocket
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:53 1.0
 */
open class ServerPresenterImpl(view: ServerView) : ZBasePresenter<ServerView>(view), ServerPresenter {
    var clientSocket: MulticastSocket? = null
    var serverSocker: MulticastSocket? = null
    var multicastHost = "224.0.0.1"
    val port = 8003
    val list: ArrayList<JSONObject> = ArrayList()

    var receiveAddress: InetAddress? = null

    private val serverExecutor: ThreadPoolExecutor
    var isRunning = false

    init {
        serverExecutor = ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue(),
                ThreadFactory { r ->
                    val thread = Thread(r, "socket server thread")
                    if (thread.isDaemon) {
                        thread.isDaemon = false
                    }
                    thread
                })
        try {
            // 创建socket实例
            clientSocket = MulticastSocket()
            serverSocker = MulticastSocket()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun startServer() {
        if (!isRunning) {
            isRunning = true
            serverExecutor.execute({
                //                func1()
                receiveAddress = InetAddress.getByName(multicastHost)
                serverSocker = MulticastSocket(port)
                serverSocker!!.joinGroup(receiveAddress)
                dealMessage(serverSocker!!)
                isRunning = false
            })
        }
    }

    private fun dealMessage(server: DatagramSocket) {
        val buffer = ByteArray(1024)
        val packet = DatagramPacket(buffer, buffer.size)
        while (true) {
            try {
                server.receive(packet)
                val json = JSONObject()
                json.put("address", packet.address)
                json.put("port", packet.port)
                json.put("message", String(packet.data, 0, packet.length, Charset.defaultCharset()))
                list.add(json)
                ThreadManager.getInstance().runInUIThread({
                    view.showReceiverMsg(list)
                })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
//
//    fun func1() {
//        var server: DatagramSocket? = null
//        try {
//            server = DatagramSocket(port)
//            dealMessage(server)
//        } catch (e: SocketException) {
//            e.printStackTrace()
//        } finally {
//            if (server != null) {
//                server.close()
//            }
//        }
//    }

    override fun sendBroadCast() {
        ThreadManager.getInstance().runInMultiThread({
            // 发送的数据包，局网内的所有地址都可以收到该数据包
            try {
                clientSocket?.setTimeToLive(4)
                // 将本机的IP（这里可以写动态获取的IP）地址放到数据包里，其实server端接收到数据包后也能获取到发包方的IP的
                val data = "测试广播数据".toByteArray()
                // 224.0.0.1
                val wifiInfo = ZNetUtil.getConnectionInfo(MyApplication.getInstance())
                if (wifiInfo != null) {
                    val server = InetAddress.getByName("224.0.0.1")
                    ZLog.i(ZTag.TAG_DEBUG, "" + server)
                    ZLog.i(ZTag.TAG_DEBUG, "" + server.isMulticastAddress)
                    val dataPacket = DatagramPacket(data, data.size, server, port)
                    clientSocket?.send(dataPacket)
                    // socket?.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }
}
