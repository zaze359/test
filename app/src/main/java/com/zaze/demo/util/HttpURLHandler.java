package com.zaze.demo.util;

import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-05-22 - 15:53
 */
public class HttpURLHandler extends URLStreamHandler {
    public static final String PROTOCOL = "http";

    private URLStreamHandler handler;
    private Class httpHandlerClass;

    public HttpURLHandler() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 4.4及以上
                httpHandlerClass = Class.forName("com.android.okhttp.HttpHandler");
            } else {
                httpHandlerClass = Class.forName("libcore.net.http.HttpHandler");
            }
            handler = (URLStreamHandler) httpHandlerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        try {
            Method method = handler.getClass().getDeclaredMethod("openConnection", URL.class);
            method.setAccessible(true);
            return (URLConnection) method.invoke(handler, dealURL(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        try {
            Method method = handler.getClass().getDeclaredMethod("openConnection", URL.class, Proxy.class);
            method.setAccessible(true);
            return (URLConnection) method.invoke(handler, dealURL(u), p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private URL dealURL(URL url) {
        //soft.tbs.imtt.qq.com/17421/tbs_res_imtt_tbs_release_tbs_core_4.7.0.18_044704_20190510_181602_nolg_fs_obfs.tbs"
        Log.i("zaze", "URLConnection url : " + url.toString());
        Log.i("zaze", "URLConnection thread : " + Thread.currentThread().getName());
        return url;
    }
}
