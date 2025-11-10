package com.zaze.demo.component.webview

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.SslErrorHandler
import android.net.http.SslError
import android.widget.Toast
import android.webkit.WebResourceError
import android.net.Uri
import android.os.Build
import android.webkit.CookieManager
import android.webkit.ValueCallback
import androidx.annotation.RequiresApi
import com.zaze.demo.component.webview.settings.CustomWebChromeClient
import com.zaze.demo.BuildConfig
import com.zaze.utils.log.ZLog

/**
 * WebView 管理类，负责 WebView 的配置和管理
 */
class WebViewManager(activity: Activity) {
    private var currFilePathCallback: ValueCallback<Array<Uri>>? = null
    private val fileChooserListener: FileChooserListener = object : FileChooserListener {
        override fun showCamera(filePathCallback: ValueCallback<Array<Uri>>?) {
            currFilePathCallback = filePathCallback
            filePathCallback?.onReceiveValue(emptyArray<Uri>())
        }
    }

    /**
     * 相机退出时没有回调，通过生命周期来主动取消
     */
    fun cancelFileChooser() {
        currFilePathCallback?.onReceiveValue(emptyArray<Uri>())
        currFilePathCallback = null
    }

    /**
     * 设置 WebView 配置
     *
     * @param webView 需要配置的 WebView 实例
     * @param loadingView 加载时显示的视图
     */
    fun setupWebView(webView: WebView, loadingView: View? = null) {
        // 仅在测试环境启用 WebView 远程调试
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        webView.settings.apply {
            mediaPlaybackRequiresUserGesture = false
            javaScriptEnabled = true
            domStorageEnabled = true
            // 禁用缓存确保每次获取最新内容
            cacheMode = WebSettings.LOAD_DEFAULT
            databaseEnabled = true
            // 设置用户代理字符串，使其更像桌面浏览器
            userAgentString =
                "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Tablet Safari/537.36"
            // 允许混合内容（HTTP和HTTPS混合）
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            // 添加更多优化配置以减少渲染问题
            loadsImagesAutomatically = true
            blockNetworkImage = false
            useWideViewPort = false
            loadWithOverviewMode = true
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false

            // 启用硬件加速
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                offscreenPreRaster = true
            }
        }

        // 启用 Cookie 管理
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (BuildConfig.DEBUG) {
                    ZLog.i("WebViewManager", "Page started: $url")
                }
                // 页面开始加载时显示loading界面
                loadingView?.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (BuildConfig.DEBUG) {
                    ZLog.i("WebViewManager", "Page finished: $url")
                }
                // 页面加载完成时隐藏loading界面
                loadingView?.visibility = View.GONE
                // 同步 Cookie
                CookieManager.getInstance().flush()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?,
            ) {
                super.onReceivedError(view, request, error)
                if (BuildConfig.DEBUG) {
                    ZLog.e("WebViewManager", "Error: ${error?.description}, URL: ${request?.url}")
                }
                // 页面加载出错时也隐藏loading界面
                loadingView?.visibility = View.GONE
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?,
            ) {
                if (BuildConfig.DEBUG) {
                    ZLog.e("WebViewManager", "SSL Error: $error")
                    handler?.proceed() // 测试环境继续加载
                    Toast.makeText(view?.context, "测试环境：忽略SSL证书错误", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    handler?.cancel() // 生产环境阻止加载
                    loadingView?.visibility = View.GONE
                    Toast.makeText(view?.context, "SSL证书错误，已阻止加载", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?,
            ): Boolean {
                val url = request?.url.toString() ?: return false
                if (BuildConfig.DEBUG) {
                    ZLog.i("WebViewManager", "Loading URL: $url")
                }
                if (!BuildConfig.DEBUG && !url.startsWith("https://")) {
                    Toast.makeText(view?.context, "安全警告：已阻止非HTTPS请求", Toast.LENGTH_SHORT)
                        .show()
                    return true // 生产环境中阻止非 HTTPS 加载
                }
                return false
            }

        }

        webView.webChromeClient = CustomWebChromeClient(fileChooserListener)
        // 阻止长按选择
        webView.setOnLongClickListener { true }
    }
}