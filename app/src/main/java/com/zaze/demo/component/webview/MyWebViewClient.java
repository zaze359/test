package com.zaze.demo.component.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.zaze.utils.log.ZLog;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-08-05 - 15:44
 */
public class MyWebViewClient extends WebViewClient {

    public static final String TAG = "MyWebViewClient";

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        ZLog.i(TAG, "onPageStarted : " + url);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        ZLog.i(TAG, "onPageFinished : " + url);
//        WebViewConsole.consoleDoc(view);
//        WebViewConsole.removeAllImage(view);
//        view.loadUrl("file:///android_asset/filterAd.js");
//        try {
//            String js = FileUtil.readByBytes(view.getContext().getAssets().open("filterAd.js")).toString();
//            view.loadUrl(js);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
//
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        return super.shouldOverrideUrlLoading(view, request);
//    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        ZLog.i(TAG, "发生跳转 : " + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return super.shouldInterceptRequest(view, url);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            for (String key : request.getRequestHeaders().keySet()) {
//                Log.i(TAG, "shouldInterceptRequest >> key: " + key + " value :" + request.getRequestHeaders().get(key));
//
//            }
            // request 过滤 url
//            if (!request.getUrl().getHost().contains("admin") && !request.getUrl().getHost().contains("www.baidu.com")) {
//                return new WebResourceResponse("", "", null);
//            }
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onLoadResource(final WebView view, String url) {
        super.onLoadResource(view, url);
        Log.i(TAG, "onLoadResource >> url: " + url);
//        WebViewConsole.removeAllImage(view);
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (view != null) {
//                    WebViewConsole.removeAllImage(view);
//                }
//            }
//        }, 1000);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

}
