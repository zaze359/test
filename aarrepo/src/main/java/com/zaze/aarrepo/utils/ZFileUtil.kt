package com.zaze.aarrepo.utils

import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.Log
import com.zaze.aarrepo.commons.log.ZLog
import com.zaze.aarrepo.utils.FileUtil.createParentDir
import com.zaze.aarrepo.utils.FileUtil.isFileExist
import java.io.*
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Description :
 * @author : ZAZE
 * @version : 2017-07-13 - 10:08
 */
object ZFileUtil {
    var showLog = true
    private val TAG = "FileUtil"
    private val needLock = true
    private val lock = ReentrantReadWriteLock()
    private fun writeLock(lock: ReadWriteLock) {
        if (needLock) lock.writeLock().lock()
    }

    private fun writeUnlock(lock: ReadWriteLock) {
        if (needLock) lock.writeLock().unlock()
    }

    private fun readLock(lock: ReadWriteLock) {
        if (needLock) lock.readLock().lock()
    }

    private fun readUnlock(lock: ReadWriteLock) {
        if (needLock) lock.readLock().unlock()
    }
    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * Description : SD卡是否可用
     * @author zaze
     * @version 2017/7/13 - 上午10:08 1.0
     */
    fun isSdcardEnable(): Boolean {
        return Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
    }

    fun getSDCardRoot(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

    fun isFile(path: String): Boolean {
        return File(path).isFile
    }

    fun isDirectory(path: String): Boolean {
        return File(path).isDirectory
    }
    // --------------------------------------------------
    /**
     * @param filePath filePath
     * @return
     */
    fun createFile(filePath: String): File {
        val file = File(filePath)
        var result = false
        if (!isFileExist(filePath)) {
            if (createParentDir(filePath)) {
                result = file.createNewFile()
            }
        } else {
            result = true
        }
        if (showLog) {
            Log.v(TAG, "createFile filePath : " + filePath)
            Log.v(TAG, "createFile result : " + result)
        }
        return file
    }

    // --------------------------------------------------
    // --------------------------------------------------
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.blockSizeLong
        } else {
            return statFs.blockSize.toLong()
        }
    }

    fun getAvailableBlocks(statFs: StatFs): Long {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.availableBlocksLong
        } else {
            return statFs.availableBlocks.toLong()
        }
    }

    fun getBlockCount(statFs: StatFs): Long {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.blockCountLong
        } else {
            return statFs.blockCount.toLong()
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * 将数据写入sd卡

     * @param filePath
     * *
     * @param dataStr
     * *
     * @return
     */
    fun writeToFile(filePath: String, dataStr: String, append: Boolean): File? {
        val input = ByteArrayInputStream(dataStr.toByteArray())
        return writeToFile(filePath, input, append)
    }

    /**
     * 将数据写入sd卡

     * @param filePath
     * *
     * @param input
     * *
     * @return
     */
    fun writeToFile(filePath: String, input: InputStream): File? {
        return writeToFile(filePath, input, false)
    }

    /**
     * 将数据写入sd卡

     * @param filePath
     * *
     * @param input
     * *
     * @return
     */
    fun writeToFile(filePath: String, input: InputStream, append: Boolean): File? {
        writeLock(lock)
        var file: File? = null
        var output: OutputStream? = null
        try {
            file = createFile(filePath)
            output = FileOutputStream(file, append)
            val buffer = ByteArray(4 * 1024)
            var temp = input.read(buffer)
            while (temp != -1) {
                output.write(buffer, 0, temp)
                temp = input.read(buffer)
            }
            //            String split = "\n-------------------------\n";
            //            output.write(split.getBytes(), 0, split.length());
            output.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                input.close()
                if (output != null) {
                    output.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        writeUnlock(lock)
        return file
    }

}