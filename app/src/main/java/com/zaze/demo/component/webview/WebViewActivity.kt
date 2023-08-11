package com.zaze.demo.component.webview


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.webkit.*
import androidx.databinding.DataBindingUtil
import com.zaze.common.base.BaseActivity
import com.zaze.demo.R
import com.zaze.demo.databinding.WebViewActivityBinding
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
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
        val binding =
            DataBindingUtil.setContentView<WebViewActivityBinding>(this, R.layout.web_view_activity)
        EventBus.getDefault().register(this)
        val settings = binding.webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.webView.addJavascriptInterface(JSInterface(), "jsInterface")
        binding.webView.webViewClient = object : MyWebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                ZLog.i(ZTag.TAG, "loadUrl: ${request?.url}")

                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
//                WebViewConsole.addDom(binding.webView)
                WebViewConsole.consoleDoc(binding.webView)
            }
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                binding.webProgressBar.progress = newProgress
                if (newProgress == 100) {
                    binding.webProgressBar.visibility = View.INVISIBLE
                    val animation = AlphaAnimation(1.0f, 0.0f)
                    animation.duration = 500
                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {}

                        override fun onAnimationEnd(animation: Animation) {
                            binding.webProgressBar.visibility = View.INVISIBLE
                        }

                        override fun onAnimationRepeat(animation: Animation) {}
                    })
                    binding.webProgressBar.startAnimation(animation)

                } else {
                    binding.webProgressBar.visibility = View.VISIBLE
                }
            }
        }
//        WebViewConsole.consoleDoc(binding.webView)
        val url = "https://www.baidu.com"
        ZLog.i(ZTag.TAG, "loadUrl: $url")
        binding.webView.loadUrl(url)
//        binding.webView.loadUrl("https://www.baidu.com")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(ss: WebViewEvent) {
        ZLog.i(ZTag.TAG, "onEvent $ss")
//        WebViewConsole.jsConsole(binding.webView, "javascript:window.addDom()")
//        WebViewConsole.addDom(binding.webView)
//        WebViewConsole.consoleDoc(binding.webView)
    }

    internal inner class JSInterface {
        @JavascriptInterface
        fun showSource(html: String) {
            ZLog.i(ZTag.TAG_DEBUG, "====>html=$html")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}