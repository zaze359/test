package com.zaze.common.http

import com.zaze.common.http.okhttp.OkHttpDownloadClient
import com.zaze.common.http.proxy.HttpDownloadClientProxy
import com.zaze.common.thread.ThreadPlugins
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-12 - 10:47
 */
class HttpRequest private constructor(
    downloadClient: DownloadClient,
    private val request: ZRequest
) {
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
            return HttpRequest(OkHttpDownloadClient(), request)
        }
    }

    /**
     * 同步请求
     */
    fun request(): ZResponse {
        return ZRealCall(request).execute()
    }

    /**
     * Flow方式返回结果
     */
    fun requestByFlow(): Flow<ZResponse> {
        return flowOf(request())
            .flowOn(ThreadPlugins.requestExecutorStub.coroutineDispatcher)
    }

    /**
     * Rx方式返回结果
     */
    fun requestByRx(): Observable<ZResponse> {
        return Observable.fromCallable { request() }
            .subscribeOn(ThreadPlugins.requestExecutorStub.rxScheduler)
            .observeOn(ThreadPlugins.ioScheduler())
    }

    fun download(callback: DownloadCallback? = null) {
        downloadClient.download(request, callback)
    }
}