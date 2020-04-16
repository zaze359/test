package com.zaze.common.http

import com.zaze.common.http.okhttp.OkHttpDownloadClient
import com.zaze.common.http.okhttp.OkHttpRequestClient
import com.zaze.common.http.proxy.HttpDownloadClientProxy
import com.zaze.common.http.proxy.HttpRequestClientProxy
import com.zaze.common.thread.ThreadPlugins
import io.reactivex.Observable

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-12 - 10:47
 */
class HttpRequest private constructor(
    client: RequestClient,
    downloadClient: DownloadClient,
    private val request: ZRequest
) {
    private val requestClient = HttpRequestClientProxy(client)
    private val downloadClient = HttpDownloadClientProxy(downloadClient)

    companion object {
        /**
         * 构建默认Http请求方式
         */
        @JvmStatic
        fun newCall(request: ZRequest): HttpRequest {
            return newOkHttpCall(request)
        }

        /**
         * 构建okHttp请求方式
         */
        @JvmStatic
        fun newOkHttpCall(request: ZRequest): HttpRequest {
            return HttpRequest(OkHttpRequestClient(), OkHttpDownloadClient(), request)
        }
    }

    fun request(): ZResponse {
        return requestClient.request(request)
    }

    /**
     * Rx方式请求
     */
    fun requestByRx(): Observable<ZResponse> {
        return Observable.fromCallable { request() }.subscribeOn(ThreadPlugins.requestScheduler())
            .observeOn(ThreadPlugins.ioScheduler())
    }

    fun download(callback: DownloadCallback? = null) {
        downloadClient.download(request, callback)
    }
}