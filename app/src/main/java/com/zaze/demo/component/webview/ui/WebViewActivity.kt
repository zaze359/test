package com.zaze.demo.component.webview.ui


import android.os.Bundle
import com.zaze.aarrepo.commons.base.ZBaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.webview.presenter.WebViewPresenter
import com.zaze.demo.component.webview.presenter.impl.WebViewPresenterImpl
import com.zaze.demo.component.webview.view.WebViewView
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * Description :
 * @author : zaze
 * @version : 2017-08-16 05:48 1.0
 */
open class WebViewActivity : ZBaseActivity(), WebViewView {
    var presenter: WebViewPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        presenter = WebViewPresenterImpl(this)
        web_view.loadUrl("www.baidu.com")
    }

}