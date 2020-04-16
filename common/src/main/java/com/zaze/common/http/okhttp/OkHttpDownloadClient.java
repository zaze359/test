package com.zaze.common.http.okhttp;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import com.zaze.common.http.DownloadCallback;
import com.zaze.common.http.DownloadClient;
import com.zaze.common.http.ZRequest;
import com.zaze.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-06-15 - 13:49
 */
public class OkHttpDownloadClient implements DownloadClient {
    private static OkHttpClient downloadClient;

    private static OkHttpClient getDownloadClient() {
        if (downloadClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS);
            downloadClient = builder.build();
        }
        return downloadClient;
    }
    // --------------------------------------------------

    @Override
    public void download(@NonNull ZRequest request, final DownloadCallback callback) {
        final String destPath = request.getSavePath();
        getDownloadClient().newCall(OkHttpHelper.buildRequest(request)).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                callback.onFailure(e.getMessage(), destPath);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    File file = new File(destPath);
                    FileUtil.createFileNotExists(destPath);
                    InputStream is = null;
                    FileOutputStream fos = null;
                    byte[] buf = new byte[2048];
                    double total = responseBody.contentLength();
                    if (callback != null) {
                        callback.onStart(total);
                    }
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
                                if (callback != null) {
                                    callback.onProgress(total, current, current / consumeTime);
                                }
                            }
                        }
                        fos.flush();
                        if (callback != null) {
                            callback.onSuccess(file.getAbsolutePath(), destPath, total / consumeTime);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onFailure(e.getMessage(), destPath);
                        }
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
}