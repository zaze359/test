package com.zaze.common.http.okhttp

import com.zaze.common.http.LRequest
import com.zaze.common.http.LRequestBody
import com.zaze.common.http.LResponse
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-12 - 16:29
 */
object OkHttpHelper {

    /**
     * 构建okHttp Request Body
     */
    @JvmStatic
    private fun buildRequestBody(requestBody: LRequestBody?): RequestBody? {
        return requestBody?.let {
            RequestBody.create(MediaType.parse(it.mediaType.mediaType), it.content)
        }
    }


    /**
     * 构建OkHttpRequest
     *
     * @return Request
     */
    @JvmStatic
    fun buildRequest(request: LRequest): Request {
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
    fun copyResponse(from: Response, to: LResponse): LResponse {
        to.code = from.code()
        //
        val body = from.body()
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