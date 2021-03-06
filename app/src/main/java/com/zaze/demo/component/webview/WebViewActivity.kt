package com.zaze.demo.component.webview


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.zaze.common.base.BaseActivity
import com.zaze.demo.R
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.android.synthetic.main.web_view_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Description :
 * @author : zaze
 * @version : 2017-08-16 05:48 1.0
 */
open class WebViewActivity : BaseActivity() {

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view_activity)
        EventBus.getDefault().register(this)
        val settings = web_view.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        web_view.addJavascriptInterface(JSInterface(), "jsInterface")
        web_view.webViewClient = object : MyWebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                WebViewConsole.addDom(web_view)
                WebViewConsole.consoleDoc(web_view)
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
//        WebViewConsole.consoleDoc(web_view)
        ZLog.i(ZTag.TAG, "loadUrl ")
        web_view.loadUrl("file:///android_asset/index.html")

//        web_view.loadUrl("https://www.baidu.com")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(ss: WebViewEvent) {
        ZLog.i(ZTag.TAG, "onEvent $ss")
        WebViewConsole.jsConsole(web_view, "javascript:window.addDom()")
//        WebViewConsole.addDom(web_view)
        WebViewConsole.consoleDoc(web_view)
    }

    internal inner class JSInterface {

        @JavascriptInterface
        fun showSource(html: String) {
            ZLog.i(ZTag.TAG_DEBUG, "====>html=$html")
        }
    }
}