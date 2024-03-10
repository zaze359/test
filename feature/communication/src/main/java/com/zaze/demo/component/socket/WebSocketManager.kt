package com.zaze.demo.component.socket

import com.zaze.utils.log.ZLog
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

/**
 * Description :
 * @author : zaze
 * @version : 2024-01-29 21:11
 */
class WebSocketManager {
    private val client = OkHttpClient()
    private var myWebSocket: WebSocket? = null

    fun connect() {
        if (myWebSocket == null) {
            val request = Request.Builder().url("ws://192.168.0.103:8080/ws/11")
            myWebSocket = client.newWebSocket(request.build(), object : WebSocketListener() {

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                    ZLog.w("websocket", "onClosed: $reason($code)")
                    myWebSocket = null
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                    ZLog.w("websocket", "onClosing: $reason($code)")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    ZLog.e("websocket", "onFailure: ${response?.message}", t)
                    myWebSocket = null
                }

                /**
                 * 接受消息
                 */
                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    ZLog.d("websocket", "onMessage: $text")
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    super.onMessage(webSocket, bytes)
                    ZLog.d("websocket", "onMessage bytes: ${bytes.size}")
                }

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    ZLog.d("websocket", "onOpen: ${response.message}")
                }
            })
        }
    }

    fun send(message: String) {
        val ret = myWebSocket?.send(message)
        ZLog.d("websocket", "$ret; $message")
    }

    fun close() {
        myWebSocket?.close(1001, "web socket connection closed")
        myWebSocket = null
    }
}