package com.zaze.common.http

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-12 - 15:40
 */
interface DownloadClient {
    fun download(request: ZRequest, callback: DownloadCallback?)
}