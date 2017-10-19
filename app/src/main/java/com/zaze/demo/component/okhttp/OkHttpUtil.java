package com.zaze.demo.component.okhttp;


import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZCallback;
import com.zaze.utils.log.ZLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-06-15 - 13:49
 */
public class OkHttpUtil {
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    // --------------------------------------------------

    public static void get(String url, ZCallback<String> callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        enqueue(request, callback);
    }

    public static void post(String url, String postData, ZCallback<String> callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), postData))
                .build();
        enqueue(request, callback);
    }

    private static void enqueue(Request request, final ZCallback<String> callback) {
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                ThreadManager.getInstance().runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ZLog.i("okhttp : ", e.getMessage());
                        if (callback != null) {
                            callback.onError(-1, e.getMessage());
                            callback.onCompleted();
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String body = response.body().string();
                ThreadManager.getInstance().runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ZLog.i("okhttp : ", "" + body);
                        if (callback != null) {
                            callback.onNext(body);
                            callback.onCompleted();
                        }
                    }
                });
            }
        });
    }

    /**
     * 不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }
}