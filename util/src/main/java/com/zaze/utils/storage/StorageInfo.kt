package com.zaze.utils.storage

import com.zaze.utils.DescriptionUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

data class StorageInfo(var totalBytes: Long = 0, var freeBytes: Long = 0) {
    companion object {
        const val UNIT = 1024

        fun roundStorageSize(size: Long): Long {
            var roundSize: Long = 1
            var pow: Long = 1
            while (roundSize * pow < size) {
                roundSize = roundSize shl 1
                if (roundSize > 512) {
                    roundSize = 1
                    pow *= UNIT
                }
            }
            return roundSize * pow
        }

        fun showBytes(size: Long): String {
            return DescriptionUtil.toByteUnit(size, UNIT)
        }
    }

    fun addTotalBytes(bytes: Long) {
        totalBytes += bytes
    }

    fun addFreeBytes(bytes: Long) {
        freeBytes += bytes
    }

    fun showTotalBytes(): String {
        return showBytes(totalBytes)
    }

    fun showFreeBytes(): String {
        return showBytes(freeBytes)
    }

    fun merge(storageInfo: StorageInfo): StorageInfo {
        addTotalBytes(storageInfo.totalBytes)
        addFreeBytes(storageInfo.freeBytes)
        return this
    }

    /**
     * 矫正磁盘显示大小
     * copy from {@link android.os.FileUtils.roundStorageSize(long size)}
     */
    fun roundStorageSize(): StorageInfo {
        totalBytes = roundStorageSize(totalBytes)
        return this
    }

    /**
     * log打印总大小和可用大小
     */
    fun log(tag: String = ZTag.TAG_DEBUG) {
        ZLog.v(
            tag,
            "totalSize:${showTotalBytes()}; freeSpace:${
                showFreeBytes()
            }"
        )
    }
}