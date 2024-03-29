package com.zaze.core.network.http.okhttp

import com.zaze.core.network.http.ZRequest
import com.zaze.core.network.http.ZRequestBody
import com.zaze.core.network.http.ZResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-12 - 16:29
 */
internal object OkHttpHelper {

    /**
     * 构建okHttp Request Body
     */
    @JvmStatic
    private fun buildRequestBody(requestBody: ZRequestBody?): RequestBody? {
        return requestBody?.let {
            it.content.toRequestBody(it.mediaType.mediaType.toMediaTypeOrNull())
        }
    }


    /**
     * 构建OkHttpRequest
     *
     * @return Request
     */
    @JvmStatic
    fun buildRequest(request: ZRequest): Request {
        val builder = Request.Builder()
        builder.url(request.url)
                .method(
                        request.method, buildRequestBody(request.requestBody)
                )
        request.headers.forEach { entity ->
            builder.addHeader(entity.key, entity.value)
        }
        return builder.build()
    }


    /**
     * 从OkHttp的Response中拷贝数据
     *
     * @param from okHttp
     * @return 变更后的LResponse
     */
    @JvmStatic
    fun copyResponse(from: Response, to: ZResponse): ZResponse {
        to.code = from.code
        //
        val body = from.body
        var bodyStr: String? = null
        if (body != null) {
            try {
                bodyStr = body.string()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        to.responseBody = bodyStr
        return to
    }

}