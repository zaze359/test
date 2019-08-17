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
        val settings = web_view.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        web_view.addJavascriptInterface(JSInterface(), "jsInterface")
        web_view.webViewClient = MyWebViewClient()
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
//        web_view.loadUrl("file:///android_asset/test.html")
        web_view.loadUrl("https://www.baidu.com")
//        web_view.loadUrl("http://debugtbs.qq.com")
//        web_view.loadUrl("http://help.xh.com/faq/CA106002/index.html#/")
//        web_view.loadUrl("https://help.yunzuoye.net/faq/CA101010/index.html#/updates/188")
    }

    internal inner class JSInterface {

        @JavascriptInterface
        fun showSource(html: String) {
            ZLog.i(ZTag.TAG_DEBUG, "====>html=$html")
        }
    }
}