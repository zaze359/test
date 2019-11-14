package com.zaze.common.http

/**
 * 下载回调
 */
abstract class DownloadCallback {
    /**
     * 开始
     *
     * @param total 文件大小
     */
    open fun onStart(total: Double){}

    /**
     * 进度
     *
     * @param fileTotalSize 文件总大小
     * @param fileDownSize  已下载大小
     * @param speed         下载速度
     */
    open fun onProgress(fileTotalSize: Double, fileDownSize: Double, speed: Double){}

    /**
     * 下载成功回调
     *
     * @param message        code描述信息
     * @param savePath 保存路径
     * @param speed         下载速度
     */
    open fun onSuccess(message: String?, savePath: String, speed: Double){}

    /**
     * 下载失败回调
     *
     * @param errorMessage        描述信息
     * @param savePath 保存路径
     */
    open fun onFailure(errorMessage: String?, savePath: String){}
}