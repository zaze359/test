package com.zaze.demo.component.webview.ui


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zaze.common.base.BaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.webview.presenter.WebViewPresenter
import com.zaze.demo.component.webview.presenter.impl.WebViewPresenterImpl
import com.zaze.demo.component.webview.view.WebViewView
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.android.synthetic.main.web_view_activity.*



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
        setContentView(R.layout.web_view_activity)
        presenter = WebViewPresenterImpl(this)
        val settings = web_view.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        web_view.addJavascriptInterface(InJavaScriptLocalObj(), "local_obj")
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                ZLog.i(ZTag.TAG_DEBUG, "onPageStarted ：$url")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                ZLog.i(ZTag.TAG_DEBUG, "onPageFinished ：$url")
//                view?.loadUrl("javascript:window.local_obj.showSource('<head>'+"
//                        + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                ZLog.i(ZTag.TAG_DEBUG, "发生跳转 ：$url")
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        web_view.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                web_progress_bar.progress = newProgress
                if (newProgress == 100) {
                    web_progress_bar.visibility = View.INVISIBLE
                    val animation = AlphaAnimation(1.0f, 0.0f)
                    animation.duration = 500
                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {}

                        override fun onAnimationEnd(animation: Animation) {
                            web_progress_bar.visibility = View.INVISIBLE
                        }
                        override fun onAnimationRepeat(animation: Animation) {}
                    })
                    web_progress_bar.startAnimation(animation)
                } else {
                    web_progress_bar.visibility = View.VISIBLE
                }
            }
        }
//        web_view.loadUrl("http://www.baidu.com")
        web_view.loadUrl("http://debugtbs.qq.com")
//        web_view.loadUrl("file:///android_asset/ppt/5281a115ce50f8fd676e56b295aee5e4/index.html")
//        web_view.loadUrl("http://help.xh.com/faq/CA106002/index.html#/")
//        web_view.loadUrl("https://help.yunzuoye.net/faq/CA101010/index.html#/updates/188")
    }

    internal inner class InJavaScriptLocalObj {
        fun showSource(html: String) {
            ZLog.i(ZTag.TAG_DEBUG, "====>html=$html")
        }
    }


}