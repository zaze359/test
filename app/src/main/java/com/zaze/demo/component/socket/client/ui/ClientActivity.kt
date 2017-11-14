package com.zaze.demo.component.socket.client.ui


import com.zaze.demo.R
import android.os.Bundle
import com.zaze.demo.component.socket.client.view.ClientView
import com.zaze.demo.component.socket.client.presenter.ClientPresenter
import com.zaze.demo.component.socket.client.presenter.impl.ClientPresenterImpl
import com.zaze.common.base.ZBaseActivity
import kotlinx.android.synthetic.main.activity_client.*

/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 10:52 1.0
 */
open class ClientActivity : ZBaseActivity(), ClientView {
    var presenter: ClientPresenter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        presenter = ClientPresenterImpl(this)
    }

}