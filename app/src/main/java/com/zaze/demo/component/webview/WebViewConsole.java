package com.zaze.demo.component.webview;

import android.webkit.WebView;

/**
 * Description :WebView 控制台
 *
 * @author : ZAZE
 * @version : 2019-08-05 - 15:33
 */
public class WebViewConsole {

    public static void consoleDoc(WebView webView) {
        jsConsole(webView, "JavaScript:function printDocument(){var test=document.getElementsByTagName('html')[0].innerHTML;console.info(test);jsInterface.showSource(test)}printDocument();");
    }

    public static void removeAllImage(WebView webView) {
//        jsInterface.showSource("" + imgs[i]);
        jsConsole(webView, "JavaScript:function printDocument(){var imgs= document.querySelectorAll('img');for(var i = 0; i < imgs.length; ++i) {imgs[i].src='https://admin.yunzuoye.net/frontend/img/logo.png';}}printDocument();");
    }


    public static void jsConsole(WebView webView, String js) {
        webView.loadUrl(js);
    }
}
