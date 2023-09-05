package com.zaze.utils.storage

import android.os.Build
import android.os.StatFs
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File

object StorageHelper {
    fun getTotalSpace(file: File): Long {
        ZLog.i(ZTag.TAG_DEBUG, "getTotalSpace : ${file.path}")
        val stat = StatFs(file.path)
        return getBlockSize(stat) * getBlockCount(stat)
    }

    fun getFreeSpace(file: File): Long {
        val stat = StatFs(file.path)
        return getBlockSize(stat) * getAvailableBlocks(stat)
    }

    // --------------------------------------------------
    fun getBlockSize(statFs: StatFs): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            statFs.blockSizeLong
        } else {
            statFs.blockSize.toLong()
        }
    }

    fun getAvailableBlocks(statFs: StatFs): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            statFs.availableBlocksLong
        } else {
            statFs.availableBlocks.toLong()
        }
    }

    fun getBlockCount(statFs: StatFs): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            statFs.blockCountLong
        } else {
            statFs.blockCount.toLong()
        }
    }
}