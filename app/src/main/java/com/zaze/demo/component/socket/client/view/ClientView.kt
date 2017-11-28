package com.zaze.demo.component.socket.client.view

import com.zaze.common.base.ZBaseView
import com.zaze.demo.component.socket.SocketMessage


/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:52 1.0
 */
interface ClientView : ZBaseView {

    fun showReceiverMsg(list: List<SocketMessage>)

}
