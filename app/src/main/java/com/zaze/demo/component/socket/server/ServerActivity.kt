package com.zaze.demo.component.socket.server


import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.BaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.socket.SocketMessage
import com.zaze.demo.component.socket.adapter.SocketAdapter
import com.zaze.demo.component.socket.server.presenter.ServerPresenter
import com.zaze.demo.component.socket.server.presenter.impl.ServerPresenterImpl
import com.zaze.demo.component.socket.server.view.ServerView
import kotlinx.android.synthetic.main.server_activity.*

/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:53 1.0
 */
open class ServerActivity : BaseActivity(), ServerView {

    var presenter: ServerPresenter? = null
    var adapter: SocketAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.server_activity)
        presenter = ServerPresenterImpl(this)
        server_start_bt.setOnClickListener {
            presenter?.startServer()
        }
        server_send_broadcast_bt.setOnClickListener {
            presenter?.sendBroadCast()
        }
        presenter?.startServer()
    }

    override fun showReceiverMsg(list: List<SocketMessage>) {
        if (adapter == null) {
            adapter = SocketAdapter(this, list)
            server_recycler_view.layoutManager = LinearLayoutManager(this)
            server_recycler_view.adapter = adapter
        } else {
            adapter?.setDataList(list)
        }
    }

    override fun showMessage(message: String) {
        server_message_tv.text = message
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.stopServer()
    }

}