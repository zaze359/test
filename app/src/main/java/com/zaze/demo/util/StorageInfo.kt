package com.zaze.demo.util

import com.zaze.utils.DescriptionUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

data class StorageInfo(var totalBytes: Long = 0, var freeBytes: Long = 0) {

    fun addTotalBytes(bytes: Long) {
        totalBytes += bytes
    }

    fun addFreeBytes(bytes: Long) {
        freeBytes += bytes
    }


    /**
     * log打印总大小和可用大小
     */
    fun log() {
        ZLog.v(
            ZTag.TAG_DEBUG,
            "totalSize:${DescriptionUtil.toByteUnit(totalBytes)}; freeSpace:${
                DescriptionUtil.toByteUnit(freeBytes)
            }"
        )
    }
}