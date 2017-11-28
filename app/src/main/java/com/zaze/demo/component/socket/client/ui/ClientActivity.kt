package com.zaze.demo.component.socket.client.ui


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.zaze.common.base.ZBaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.socket.SocketMessage
import com.zaze.demo.component.socket.adapter.SocketAdapter
import com.zaze.demo.component.socket.client.presenter.ClientPresenter
import com.zaze.demo.component.socket.client.presenter.impl.ClientPresenterImpl
import com.zaze.demo.component.socket.client.view.ClientView
import kotlinx.android.synthetic.main.activity_client.*

/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:52 1.0
 */
open class ClientActivity : ZBaseActivity(), ClientView {
    var presenter: ClientPresenter? = null;
    var adapter: SocketAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        headWidget.setBackClickListener(this)
        client_join_bt.setOnClickListener {
            presenter?.joinGroup()
        }
        client_send_broadcast_bt.setOnClickListener {
            presenter?.send()
        }

        presenter = ClientPresenterImpl(this)
    }

    override fun showReceiverMsg(list: List<SocketMessage>) {
        if (adapter == null) {
            adapter = SocketAdapter(this, list)
            client_invite_recycler_view.layoutManager = LinearLayoutManager(this)
            client_invite_recycler_view.adapter = adapter
        } else {
            adapter?.setDataList(list)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter?.stop()
    }

}