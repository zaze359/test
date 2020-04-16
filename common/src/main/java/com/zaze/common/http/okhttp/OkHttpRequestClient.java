package com.zaze.common.http.okhttp;

import com.zaze.common.http.RequestClient;
import com.zaze.common.http.ZRequest;
import com.zaze.common.http.ZResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-06-15 - 13:49
 */
public class OkHttpRequestClient implements RequestClient {
    private static OkHttpClient client;

    private static OkHttpClient getRequestClient(@NotNull ZRequest request) {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS);
            client = builder.build();
        }
        if (request.getConnectTimeout() + request.getReadTimeout() + request.getWriteTimeout() > 0) {
            OkHttpClient.Builder builder = client.newBuilder();
            if (request.getConnectTimeout() > 0) {
                builder.connectTimeout(request.getConnectTimeout(), TimeUnit.MILLISECONDS);
            }
            if (request.getReadTimeout() > 0) {
                builder.connectTimeout(request.getReadTimeout(), TimeUnit.MILLISECONDS);
            }
            if (request.getWriteTimeout() > 0) {
                builder.connectTimeout(request.getWriteTimeout(), TimeUnit.MILLISECONDS);
            }
            return builder.build();
        }
        return client;
    }

    // --------------------------------------------------

    @NotNull
    @Override
    public ZResponse request(@NotNull ZRequest request) {
        ZResponse response = new ZResponse(request);
        try {
            return OkHttpHelper.copyResponse(getRequestClient(request).newCall(OkHttpHelper.buildRequest(request)).execute(), response);
        } catch (IOException e) {
            e.printStackTrace();
            response.copyFrom(e);
        }
        return response;
    }

//    public static void upload(String filePath) {
//        File file = new File(filePath);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//    }
}