package com.zaze.demo.component.webview.ui


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.zaze.common.base.BaseActivity
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
open class WebViewActivity : BaseActivity(), WebViewView {
    var presenter: WebViewPresenter? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        presenter = WebViewPresenterImpl(this)
        val settings = web_view.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        web_view.setWebViewClient(object : WebViewClient() {
        })
        web_view.setWebChromeClient(WebChromeClient())
        web_view.loadUrl("www.baidu.com")
    }

}