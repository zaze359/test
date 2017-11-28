package com.zaze.demo.component.socket.server.view

import com.zaze.common.base.ZBaseView
import com.zaze.demo.component.socket.SocketMessage
import org.json.JSONObject


/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:53 1.0
 */
interface ServerView : ZBaseView {

    fun showReceiverMsg(list: List<SocketMessage>)

    fun showMessage(message: String)
}
