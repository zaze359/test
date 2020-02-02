package com.zaze.common.http.proxy

import android.text.TextUtils
import com.zaze.common.http.DownloadCallback
import com.zaze.common.http.DownloadClient
import com.zaze.common.http.LRequest
import com.zaze.common.widget.CustomToast
import com.zaze.utils.EncryptionUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-12 - 15:42
 */
class HttpDownloadClientProxy(private val client: DownloadClient) : DownloadClient {
    companion object {
        @JvmStatic
        internal val URL_MAP = HashSet<String>()

        /**
         * 任务是否存在
         * @return true 存在, false 不存在
         */
        internal fun isTaskExists(url: String): Boolean {
            return URL_MAP.contains(url)
        }

        /**
         * 缓存下载任务
         * [url] url
         */
        internal fun addDownloadTask(url: String) {
            ZLog.d(ZTag.TAG_DEBUG, "addDownloadTask : $url")
            URL_MAP.add(url)
        }

        /**
         * 清除下载任务缓存
         * [url] url
         */
        internal fun removeDownloadTask(url: String) {
            ZLog.d(ZTag.TAG_DEBUG, "removeDownloadTask : $url")
            URL_MAP.remove(url)
        }
    }


    override fun download(request: LRequest, callback: DownloadCallback?) {
        val url = request.url
        val savePath = request.savePath
        val md5 = request.md5
        ZLog.i(ZTag.TAG_DEBUG, "准备下载 url=$url; savePath=$savePath; md5=$md5")
        if (HttpDownloadClientProxy.isTaskExists(url)) {
            ZLog.d(ZTag.TAG_DEBUG, "当前文件正在下载中: $url")
            callback?.onFailure("当前文件正在下载中", savePath)
            return
        }
        ZLog.i(ZTag.TAG_DEBUG, "检测是否已下载 url=$url; savePath=$savePath;")
        if (FileUtil.exists(savePath) && checkDownloadFile(savePath, md5)) {
            ZLog.i(ZTag.TAG_DEBUG, "下载文件已存在: $savePath")
            callback?.onSuccess("下载文件已存在", savePath, 0.0)
            return
        }
        ZLog.i(ZTag.TAG_DEBUG, "开始下载 : $url")
        HttpDownloadClientProxy.addDownloadTask(url)
        client.download(request, object : DownloadCallback() {
            override fun onStart(total: Double) {
                super.onStart(total)
                callback?.onStart(total)
            }

            override fun onProgress(fileTotalSize: Double, fileDownSize: Double, speed: Double) {
                super.onProgress(fileTotalSize, fileDownSize, speed)
                callback?.onProgress(fileTotalSize, fileDownSize, speed)
            }

            override fun onSuccess(message: String?, savePath: String, speed: Double) {
                super.onSuccess(message, savePath, speed)
                HttpDownloadClientProxy.removeDownloadTask(url)
                if (checkDownloadFile(savePath, md5)) {
                    ZLog.i(ZTag.TAG_DEBUG, "下载完成 url=$url; savePath=$savePath;")
                    callback?.onSuccess(message, savePath, speed)
                } else {
                    ZLog.w(ZTag.TAG_DEBUG, "md5校验失败($message) url=$url; savePath=$savePath;")
                    CustomToast.postShowToast("md5校验失败")
                    callback?.onFailure("md5校验失败", savePath)
                }
            }

            override fun onFailure(errorMessage: String?, savePath: String) {
                super.onFailure(errorMessage, savePath)
                HttpDownloadClientProxy.removeDownloadTask(url)
                ZLog.e(ZTag.TAG_DEBUG, "下载失败($errorMessage) url=$url; savePath=$savePath;")
                callback?.onFailure(errorMessage, savePath)
            }
        })
    }

    /**
     * 校验md5
     *
     * @param savePath 保存文件
     * @param md5      服务端md5
     * @return true md5匹配
     */
    internal fun checkDownloadFile(savePath: String, md5: String?): Boolean {
        if (TextUtils.isEmpty(md5)) {
            ZLog.i(ZTag.TAG_DEBUG, "没有给定md5, 不校验下载文件")
            return true
        }
        if (TextUtils.isEmpty(savePath)) {
            ZLog.e(ZTag.TAG_DEBUG, "找不到保存路径记录，更新失败")
            return false
        } else {
            val file = File(savePath)
            return if (file.exists()) {
                val downloadFileMd5 = EncryptionUtil.getMD5(file)
                if (md5.equals(downloadFileMd5, ignoreCase = true)) {
                    ZLog.i(ZTag.TAG_DEBUG, "下载文件md5校验成功($savePath)")
                    true
                } else {
                    ZLog.e(ZTag.TAG_DEBUG, "下载文件md5校验失败(L/S)($downloadFileMd5/$md5)")
                    false
                }
            } else {
                ZLog.e(ZTag.TAG_DEBUG, "找不到下载文件，更新失败")
                false
            }
        }
    }
}