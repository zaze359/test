package com.zaze.common.util

import android.view.ViewGroup
import android.webkit.WebView

/**
 * 内存释放器
 */
object MemoryReleaser {

    fun releaseWebView(webView: WebView) {
        // 先从父容器中移除
        (webView.parent as? ViewGroup)?.removeView(webView)
        //
        webView.stopLoading()
        webView.settings.javaScriptEnabled = false
        webView.clearHistory()
        webView.clearCache(true)
        webView.removeAllViews()
        webView.removeAllViewsInLayout()
        webView.webChromeClient = null
        webView.destroy()
    }

}