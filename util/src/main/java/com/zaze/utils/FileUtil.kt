package com.zaze.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.text.TextUtils
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.*
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Description :
 * @author : ZAZE
 * @version : 2017-07-13 - 10:08
 */
object FileUtil {
    var showLog = false
    var needLock = false
    private val lock = ReentrantReadWriteLock()
    private fun writeLock() {
        if (needLock) {
            lock.writeLock().lock()
        }
    }

    private fun writeUnlock() {
        if (needLock) lock.writeLock().unlock()
    }

    private fun readLock() {
        if (needLock) {
            lock.readLock().lock()
        }
    }

    private fun readUnlock() {
        if (needLock) {
            lock.readLock().unlock()
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------
    @JvmStatic
    fun isSdcardEnable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    @JvmStatic
    fun getSDCardRoot(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

    @JvmStatic
    fun isFile(path: String): Boolean {
        return File(path).isFile
    }

    @JvmStatic
    fun isDirectory(path: String): Boolean {
        return File(path).isDirectory
    }

    @JvmStatic
    fun exists(filePath: String?): Boolean {
        if (filePath.isNullOrEmpty()) {
            return false
        }
        return File(filePath).exists()
    }

    @JvmStatic
    fun isCanRead(filePath: String?): Boolean {
        if (filePath.isNullOrEmpty()) {
            return false
        }
        return exists(filePath) && File(filePath).canRead()
    }

    @JvmStatic
    fun isCanWrite(filePath: String?): Boolean {
        if (filePath.isNullOrEmpty()) {
            return false
        }
        return exists(filePath) && File(filePath).canWrite()
    }
    // --------------------------------------------------
    /**
     * [from] 从此文件拷贝
     * [to] 拷贝到此文件
     */
    @JvmStatic
    fun copy(from: File, to: File) {
        writeToFile(to, from.inputStream())
    }

    /**
     * [filePath] 文件路径
     * [newFileName] 新文件名
     */
    @JvmStatic
    fun rename(filePath: String, newFileName: String): Boolean {
        val file = File(filePath)
        if (file.exists()) {
            return renameFile(filePath, file.parentFile.absolutePath + File.separator + newFileName)
        }
        return false
    }

    @JvmStatic
    fun move(source: String, target: String): Boolean {
        return renameFile(source, target)
    }

    /**
     * [source] 文件路径
     * [target] 新文件名
     */
    @JvmStatic
    private fun renameFile(source: String, target: String): Boolean {
        val sourceFile = File(source)
        return if (sourceFile.exists()) {
            reCreateFile(target)
            createParentDir(target)
            sourceFile.renameTo(File(target))
        } else {
            false
        }
    }

    // --------------------------------------------------
    /**
     * 创建文件
     * [filePath] filePath
     * @return
     */
    @JvmStatic
    fun createFileNotExists(filePath: String): Boolean {
        return createFileNotExists(File(filePath))
    }

    /**
     * 创建文件
     * [filePath] filePath
     * @return
     */
    @JvmStatic
    fun createFileNotExists(file: File): Boolean {
        var result = false
        if (!file.exists()) {
            if (createParentDir(file)) {
                result = try {
                    file.createNewFile()
                } catch (e: Exception) {
                    false
                }
            }
        } else {
            result = true
        }
        if (showLog) {
            ZLog.v(ZTag.TAG_FILE, "createFileNotExists filePath : ${file.absolutePath}")
            ZLog.v(ZTag.TAG_FILE, "createFileNotExists code : $result")
        }
        return result
    }
    // --------------------------------------------------

    /**
     * 创建目录
     * [path] dir
     */
    @JvmStatic
    fun createDirNotExists(file: File): Boolean {
        if (file.exists()) {
            return file.isDirectory
        } else {
            return file.mkdirs()
        }
    }

    // --------------------------------------------------
    /**
     * 创建指定文件的父目录
     * [savePath] 绝对路径
     */
    @JvmStatic
    fun createParentDir(savePath: String): Boolean {
        return createParentDir(File(savePath))
    }

    /**
     * 创建指定文件的父目录
     * [savePath] 绝对路径
     */
    @JvmStatic
    fun createParentDir(file: File): Boolean {
        val parentFile = file.parentFile
        return if (parentFile != null && parentFile.exists()) {
            true
        } else {
            parentFile.mkdirs()
        }
    }

    // --------------------------------------------------
    /**
     * 强制重新创建文件(如果存在则删除创建)
     * [filePath] 绝对路径
     */
    @JvmStatic
    fun reCreateFile(filePath: String): Boolean {
        return reCreateFile(File(filePath))
    }

    @JvmStatic
    fun reCreateFile(file: File): Boolean {
        if (file.exists()) {
            deleteFile(file)
        }
        return createFileNotExists(file)
    }

    /**
     * 强制重新创建目录(如果存在则删除创建)
     * [filePath] 绝对路径
     */
    @JvmStatic
    fun reCreateDir(filePath: String): Boolean {
        return reCreateDir(File(filePath))
    }

    fun reCreateDir(file: File): Boolean {
        if (file.exists()) {
            deleteFile(file)
        }
        return createDirNotExists(file)
    }
    // --------------------------------------------------

    /**
     * 递归删除文件和文件夹
     * [filePath]  要删除的路径
     */
    @JvmStatic
    fun deleteFile(filePath: String) {
        deleteFile(File(filePath))
    }

    /**
     * 递归删除文件和文件夹
     * [destFile] 目标文件或文件夹
     */
    @JvmStatic
    fun deleteFile(destFile: File): Boolean {
        if (TextUtils.equals(getSDCardRoot(), destFile.absolutePath)) {
            return false
        }
        if (destFile.isFile) {
            return destFile.delete()
        }
        if (destFile.isDirectory) {
            val childFile = destFile.listFiles()
            if (childFile != null && childFile.isNotEmpty()) {
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
    @JvmStatic
    fun deleteFileByCmd(file: File): Boolean {
        return deleteFileByCmd(file.absolutePath)
    }

    /**
     * 调用命令删除文件
     * [filePath] 删除的文件绝对路径
     * @return
     */
    @JvmStatic
    fun deleteFileByCmd(filePath: String): Boolean {
        return ZCommand.execCmdForRes("rm -r $filePath").isSuccess
    }

    // --------------------------------------------------

    @JvmStatic
    fun copyAssetsFileToSdcard(context: Context, assetsFile: String, outPath: String) {
        ZLog.i(ZTag.TAG_DEBUG, "copyAssetsFileToSdcard $outPath")
        reCreateFile(outPath)
        writeToFile(File(outPath), context.assets.open(assetsFile), false)
    }
    // --------------------------------------------------
    /**
     * 将数据写入文件
     * [destFilePath]
     * [dataStr]
     * @return
     */
    @JvmStatic
    @JvmOverloads
    fun writeToFile(destFilePath: String, dataStr: String, append: Boolean = false): Boolean {
        return writeToFile(File(destFilePath), dataStr, append)
    }

    /**
     * 将数据写入文件
     * [destFile]
     * [dataStr]
     * @return
     */
    @JvmStatic
    @JvmOverloads
    fun writeToFile(destFile: File, dataStr: String, append: Boolean = false): Boolean {
        return writeToFile(destFile, ByteArrayInputStream(dataStr.toByteArray()), append)
    }

    /**
     * 将数据写入文件
     * [destFile] 写入目标文件
     * [inputStream] 读取的数据流
     * [append] is append
     * @return
     */
    @JvmStatic
    @JvmOverloads
    fun writeToFile(destFile: File, inputStream: InputStream, append: Boolean = false): Boolean {
        writeLock()
        var result = true
        var output: OutputStream? = null
        try {
            createFileNotExists(destFile)
            output = FileOutputStream(destFile, append)
            val buffer = ByteArray(4 * 1024)
            var temp = inputStream.read(buffer)
            while (temp != -1) {
                output.write(buffer, 0, temp)
                temp = inputStream.read(buffer)
            }
            output.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            result = false
        } finally {
            try {
                inputStream.close()
                output?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        writeUnlock()
        return result
    }

    /**
     * [filePath] filePath
     * [dataStr] dataStr
     * [maxSize] maxSize
     * @return  Boolean
     */
    @JvmStatic
    fun writeToFile(file: File, dataStr: String, maxSize: Long): Boolean {
        return writeToFile(file, ByteArrayInputStream(dataStr.toByteArray()), maxSize)
    }

    /**
     * [filePath] filePath
     * [inputStream] inputStream
     * [maxSize] maxSize
     * @return  Boolean
     */
    @JvmStatic
    fun writeToFile(file: File, inputStream: InputStream, maxSize: Long): Boolean {
        createFileNotExists(file)
        if (maxSize > 0 && file.length() > maxSize) {
            // 备份文件到  xxx.1中
            val tempFile = File("${file.absolutePath}.1")
            reCreateFile(tempFile)
            writeToFile(tempFile, FileInputStream(file), true)
            //
            deleteFile(file)
        }
        writeToFile(file, inputStream, true)
        return true
    }
    // --------------------------------------------------
    /**
     * @param filePath
     */
    @JvmStatic
    fun readFromFile(filePath: String, charset: Charset = Charset.defaultCharset()): StringBuffer {
        val file = File(filePath)
        var result = StringBuffer()
        if (exists(filePath)) {
            try {
                result = readByBytes(FileInputStream(file), charset)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        return result
    }


    @JvmStatic
    fun readByBytes(
        inputStream: InputStream,
        charset: Charset = Charset.defaultCharset()
    ): StringBuffer {
//        readLock()
//        val results = StringBuilder()
//        try {
//            val bytes = ByteArray(4096)
//            var byteLength = inputStream.read(bytes)
//            while (byteLength != -1) {
//                results.append(String(bytes, 0, byteLength, charset))
//                byteLength = inputStream.read(bytes)
//            }
//            inputStream.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            try {
//                inputStream.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//            readUnlock()
//        }
        return StringBuffer().append(readBytes(inputStream)?.let {
            String(it, charset)
        } ?: "")
    }

    @JvmStatic
    fun readBytes(inputStream: InputStream): ByteArray? {
        readLock()
        try {
            val onceSize = 4096
            val byteBuf = ByteBuf(onceSize)
            var byteLength = 0
            while (byteLength != -1) {
                byteLength = byteBuf.read(inputStream, onceSize)
            }
            inputStream.close()
            return byteBuf.get()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            readUnlock()
        }
        return null
    }


    @JvmStatic
    fun readLine(reader: Reader): StringBuffer {
        readLock()
        var bfReader: BufferedReader? = null
        val results = StringBuffer()
        try {
            bfReader = BufferedReader(reader)
            var line = bfReader.readLine()
            while (line != null) {
                results.append(line)
                line = "\n${bfReader.readLine()}"
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
            readUnlock()
        }
        return results
    }
    // --------------------------------------------------
    /**
     * [dir] 查询目标目录下符合fileName的文件和文件夹
     * [fileName] fileName
     * [isDeep] 是否深度查询
     */
    @JvmStatic
    @JvmOverloads
    fun searchFileAndDir(dir: File, fileName: String, isDeep: Boolean = false): ArrayList<File> {
        val searchedFileList = ArrayList<File>()
        if (dir.exists() && dir.isDirectory) {
            val childFileList = dir.listFiles()
            for (childFile in childFileList) {
                if (childFile.name == fileName) {
                    searchedFileList.add(childFile)
                }
                if (isDeep && childFile.isDirectory) {
                    searchedFileList.addAll(searchFileAndDir(childFile, fileName, isDeep))
                }
            }
        }
        return searchedFileList
    }


    /**
     * [dirPath] 目标目录下查询文件
     * [suffix] suffix
     * [isDeep] 是否深度查询
     */
    @JvmStatic
    @JvmOverloads
    fun searchFileBySuffix(
        dirPath: String,
        suffix: String,
        isDeep: Boolean = false
    ): ArrayList<File> {
        return searchFileBySuffix(File(dirPath), suffix, isDeep)
    }

    /**
     * [dirFile] 目标目录下查询文件
     * [suffix] suffix
     * [isDeep] 是否深度查询
     */
    @JvmStatic
    @JvmOverloads
    fun searchFileBySuffix(
        dirFile: File,
        suffix: String,
        isDeep: Boolean = false
    ): ArrayList<File> {
        val searchedFileList = ArrayList<File>()
        if (dirFile.exists() && dirFile.isDirectory) {
            val childFileList = dirFile.listFiles()
            if (childFileList.isNullOrEmpty()) {
                return searchedFileList
            }
            for (childFile in childFileList) {
//                ZLog.i(ZTag.TAG_FILE, "fileName : ${childFile.name}")
                if (childFile.isFile && childFile.name.endsWith(".$suffix", true)) {
                    searchedFileList.add(childFile)
                }
                if (isDeep && childFile.isDirectory) {
                    searchedFileList.addAll(searchFileBySuffix(childFile, suffix, isDeep))
                }
            }
        }
        return searchedFileList
    }

    // --------------------------------------------------
    // --------------------------------------------------
    @JvmStatic
    fun getTotalSpace(file: File): Long {
        ZLog.i(ZTag.TAG_DEBUG, "getTotalSpace : ${file.path}")
        val stat = StatFs(file.path)
        return getBlockSize(stat) * getBlockCount(stat)
    }

    @JvmStatic
    fun getFreeSpace(file: File): Long {
        val stat = StatFs(file.path)
        return getBlockSize(stat) * getAvailableBlocks(stat)
    }

    // --------------------------------------------------
    @JvmStatic
    fun getBlockSize(statFs: StatFs): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            statFs.blockSizeLong
        } else {
            statFs.blockSize.toLong()
        }
    }

    @JvmStatic
    fun getAvailableBlocks(statFs: StatFs): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            statFs.availableBlocksLong
        } else {
            statFs.availableBlocks.toLong()
        }
    }

    @JvmStatic
    fun getBlockCount(statFs: StatFs): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            statFs.blockCountLong
        } else {
            statFs.blockCount.toLong()
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------
}