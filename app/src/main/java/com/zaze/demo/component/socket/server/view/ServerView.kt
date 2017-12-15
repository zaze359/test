package com.zaze.demo.component.socket.server.view

import com.zaze.common.base.BaseView
import com.zaze.demo.component.socket.SocketMessage


/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:53 1.0
 */
interface ServerView : BaseView {

    fun showReceiverMsg(list: List<SocketMessage>)

    fun showMessage(message: String)
}
