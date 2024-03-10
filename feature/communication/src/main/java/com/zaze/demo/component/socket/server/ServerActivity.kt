package com.zaze.demo.component.socket.server


import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.BaseActivity
import com.zaze.demo.component.socket.SocketMessage
import com.zaze.demo.component.socket.adapter.SocketAdapter
import com.zaze.demo.component.socket.server.presenter.ServerPresenter
import com.zaze.demo.component.socket.server.presenter.impl.ServerPresenterImpl
import com.zaze.demo.component.socket.server.view.ServerView
import com.zaze.demo.feature.communication.databinding.ServerActivityBinding

/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:53 1.0
 */
open class ServerActivity : BaseActivity(), ServerView {

    var presenter: ServerPresenter? = null
    var adapter: SocketAdapter? = null
    private lateinit var binding: ServerActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ServerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = ServerPresenterImpl(this)
        binding.serverStartBt.setOnClickListener {
            presenter?.startServer()
        }
        binding.serverSendBroadcastBt.setOnClickListener {
            presenter?.sendBroadCast()
        }
        presenter?.startServer()
    }

    override fun showReceiverMsg(list: List<SocketMessage>) {
        if (adapter == null) {
            adapter = SocketAdapter(this, list)
            binding.serverRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.serverRecyclerView.adapter = adapter
        } else {
            adapter?.setDataList(list)
        }
    }

    override fun showMessage(message: String) {
        binding.serverMessageTv.text = message
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.stopServer()
    }

}