package com.zaze.demo.component.webview


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.webkit.JavascriptInterface
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.zaze.common.base.AbsActivity
import com.zaze.demo.databinding.WebViewActivityBinding
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


/**
 * Description :
 * @author : zaze
 * @version : 2017-08-16 05:48 1.0
 */
open class WebViewActivity : AbsActivity() {

    private lateinit var binding: WebViewActivityBinding
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WebViewActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)
        WebView.setWebContentsDebuggingEnabled(true)

        webView = WebView(this)
        binding.webViewContainer.addView(webView)
        val settings = webView.settings
        settings.domStorageEnabled = true
//        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.javaScriptEnabled = true
        settings.allowContentAccess = true
        //
        settings.builtInZoomControls = true // 缩放
        //
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        //
        settings.allowFileAccess = true
//        settings.allowFileAccessFromFileURLs = true
        //
        settings.displayZoomControls = true
        //
        webView.addJavascriptInterface(JSInterface(), "jsInterface")
        webView.webViewClient = object : MyWebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                ZLog.i(ZTag.TAG, "loadUrl: ${request?.url}")

                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return if (request.getUrl().toString().contains("debugdebug")) {
                    var `in`: InputStream? = null
                    Log.i("AterDebug", "shouldInterceptRequest")
                    try {
                        `in` = FileInputStream(File("/sdcard/1.png"))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    WebResourceResponse("image/*", "utf-8", `in`)
                } else {
                    super.shouldInterceptRequest(webView, request)
                }
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
//                WebViewConsole.addDom(binding.webView)
//                WebViewConsole.consoleDoc(webView)
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            // 加载video 的 默认缺省图
            override fun getDefaultVideoPoster(): Bitmap? {
                return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            }

//            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
//                super.onShowCustomView(view, callback)
//                if(view != null) {
//                    onHideCustomView()
//                    return
//                }
//            }
//
//            override fun onHideCustomView() {
//                super.onHideCustomView()
//            }
//
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
//        val url = "file:///android_asset/test.html"
//        val url = "file:android_asset/index.html"
//        ZLog.i(ZTag.TAG, "loadUrl: $url")
        if(QbSdk.isX5Core()) {
            webView.loadUrl(url)
        } else {
            webView.loadUrl("http://debugtbs.qq.com")
        }
        ZLog.i(ZTag.TAG, "loadUrl: $url")


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
        webView.destroy()
//        MemoryReleaser.releaseWebView(webView = webView)
    }
}