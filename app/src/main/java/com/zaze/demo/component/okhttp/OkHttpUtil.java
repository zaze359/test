package com.zaze.demo.component.okhttp;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import com.zaze.utils.FileUtil;
import com.zaze.utils.ZCallback;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-06-15 - 13:49
 */
@Deprecated
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

    private static void enqueue(final Request request, final ZCallback<String> callback) {
        enqueue(request, new Callback() {

            @Override
            public void onFailure(@NonNull final Call call, final IOException e) {
                if (callback != null) {
                    ZLog.d(ZTag.TAG_HTTP, "onResponse onFailure: " + e.toString());
                    callback.onError(-1, e.toString());
                    callback.onCompleted();
                }
            }

            @Override
            public void onResponse(@NonNull final Call call, @NonNull final Response response) throws IOException {
                if (callback != null) {
                    ResponseBody body = response.body();
                    String bodyStr = "";
                    if (body != null) {
                        try {
                            bodyStr = body.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ZLog.d(ZTag.TAG_HTTP, "onResponse body: " + bodyStr);
                    }
                    ZLog.d(ZTag.TAG_HTTP, "onResponse message: " + response.message());
                    if (response.isSuccessful()) {
                        callback.onNext(bodyStr);
                    } else {
                        callback.onError(response.code(), bodyStr + "  " + response.message());
                    }
                    callback.onCompleted();
                }
            }
        });
    }


    // --------------------------------------------------

    public static void download(String url, final String destPath, final DownloadCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Range", "bytes=0-")
                .build();
        enqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    File file = new File(destPath);
                    FileUtil.createFileNotExists(destPath);
                    InputStream is = null;
                    FileOutputStream fos = null;
                    byte[] buf = new byte[2048];
                    double total = responseBody.contentLength();
                    callback.startDownload(total);
                    int len;
                    double current = 0;
                    long timeMillis = SystemClock.uptimeMillis();
                    long consumeTime = 0;
                    try {
                        is = responseBody.byteStream();
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            current += len;
                            consumeTime = (SystemClock.uptimeMillis() - timeMillis) / 1000;
                            if (consumeTime > 0) {
                                callback.onProgress(total, current, current / consumeTime);
                            }
                        }
                        fos.flush();
                        callback.onSuccess(file.getAbsolutePath(), total / consumeTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError(e.getMessage());
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    }
                }
            }
        });
    }


    // --------------------------------------------------


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


    // --------------------------------------------------

    public static void upload(String filePath) {
        File file = new File(filePath);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

    }


    // --------------------------------------------------

    public interface DownloadCallback {
        void startDownload(double totalCount);

        void onProgress(double totalCount, double currentCount, double speed);

        void onSuccess(String filePath, double speed);

        void onError(String errorMsg);
    }
}