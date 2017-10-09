package com.zaze.utils

import android.os.Build
import android.os.Environment
import android.os.StatFs
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.*
import java.util.*
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Description :
 * @author : ZAZE
 * @version : 2017-07-13 - 10:08
 */
object ZFileUtil {
    var showLog = false
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

    fun isFileExist(filePath: String?): Boolean {
        return File(filePath).exists()
    }

    // --------------------------------------------------
    /**
     * [filePath] 文件路径
     * [newFileName] 新文件名
     */
    fun rename(filePath: String?, newFileName: String): Boolean {
        val file = File(filePath)
        if (file.exists()) {
            return File(filePath).renameTo(File(file.parentFile.absolutePath + File.separator + newFileName))
        } else {
            return false
        }
    }
    // --------------------------------------------------
    /**
     * [filePath] filePath
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
            ZLog.v(ZTag.TAG_FILE, "createFile filePath : " + filePath)
            ZLog.v(ZTag.TAG_FILE, "createFile code : " + result)
        }
        return file
    }

    fun createParentDir(savePath: String): Boolean {
        val parentFile = File(savePath).parentFile
        if (parentFile.exists()) {
            return true
        } else {
            return parentFile.mkdirs()
        }
    }

    fun createDir(path: String): Boolean {
        val file = File(path)
        if (file.exists()) {
            return file.isDirectory
        } else {
            return file.mkdirs()
        }
    }

    // --------------------------------------------------

    /**
     * 递归删除文件和文件夹
     * [filePath]  要删除的路径
     */
    fun deleteFile(filePath: String) {
        deleteFile(File(filePath))
    }

    /**
     * 递归删除文件和文件夹
     * [destFile] 目标文件或文件夹
     */
    fun deleteFile(destFile: File): Boolean {
        if (destFile.isFile) {
            return destFile.delete()
        }
        if (destFile.isDirectory) {
            val childFile = destFile.listFiles()
            if (childFile != null && !childFile.isEmpty()) {
                for (file in childFile) {
                    deleteFile(file)
                }
            }
        }
        return destFile.delete()
    }

    /**
     * 调用命令删除文件
     * [file] 删除的文件
     */
    fun deleteFileByCmd(file: File): Boolean {
        return deleteFileByCmd(file.absolutePath)
    }

    /**
     * 调用命令删除文件
     * [filePath] 删除的文件绝对路径
     */
    fun deleteFileByCmd(filePath: String): Boolean {
        return ZCommand.isSuccess(ZCommand.execCmdForRes("rm -r $filePath"))
    }

    // --------------------------------------------------

    /**
     * 将数据写入文件
     * [filePath]
     * [dataStr]
     * @return
     */
    fun writeToFile(filePath: String, dataStr: String, append: Boolean = false): File? {
        val input = ByteArrayInputStream(dataStr.toByteArray())
        return writeToFile(filePath, input, append)
    }

    /**
     * 将数据写入文件
     * [filePath]
     * [inputStream]
     * @return
     */
    fun writeToFile(filePath: String, inputStream: InputStream, append: Boolean = false): File? {
        writeLock(lock)
        var file: File? = null
        var output: OutputStream? = null
        try {
            file = createFile(filePath)
            output = FileOutputStream(file, append)
            val buffer = ByteArray(4 * 1024)
            var temp = inputStream.read(buffer)
            while (temp != -1) {
                output.write(buffer, 0, temp)
                temp = inputStream.read(buffer)
            }
            output.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
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

    // --------------------------------------------------
    /**
     * @param filePath
     */
    fun readFromFile(filePath: String): StringBuffer {
        val file = File(filePath)
        var result = StringBuffer()
        if (isFileExist(filePath)) {
            try {
                result = readLine(FileReader(file))
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        }
        return result
    }

    fun readLine(reader: Reader): StringBuffer {
        readLock(lock)
        var bfReader: BufferedReader? = null
        val results = StringBuffer()
        try {
            bfReader = BufferedReader(reader)
            var line = bfReader.readLine()
            while (line != null) {
                results.append(line)
                line = bfReader.readLine()
            }
            bfReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bfReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        readUnlock(lock)
        return results
    }
    // --------------------------------------------------
    /**
     * [dir] 目标目录下 循环查询文件
     */
    fun searchFileLoop(dir: File?, fileName: String): HashSet<File> {
        val loopSearchedFile = HashSet<File>()
        if (dir != null && dir.exists() && dir.isDirectory) {
            for (file in dir.listFiles()) {
                if (file.name == fileName) {
                    loopSearchedFile.add(file)
                }
                if (file.isDirectory) {
                    loopSearchedFile.addAll(searchFileLoop(file, fileName))
                }
            }
        }
        return loopSearchedFile
    }

    /**
     *  查询指定目录下的文件
     */
    private fun searchFile(dir: File, fileName: String): HashSet<File> {
        val searchedFile = HashSet<File>()
        if (dir.exists() && dir.isDirectory) {
            val childFileList = dir.listFiles()
            for (childFile in childFileList) {
                if (childFile.name == fileName) {
                    searchedFile.add(childFile)
                }
            }
        }
        return searchedFile
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


}