package com.zaze.utils

import android.annotation.SuppressLint
import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
import java.lang.Exception
import java.util.*

/**
 * Description : 加载磁盘相关数据
 * @author : zaze
 * @version : 2021-09-11 - 16:14
 */
object StorageLoader {
    private const val TAG = "${ZTag.TAG}:StorageLoader"

    fun loadStorageStats(context: Context): StorageInfo {
        return (invokeStorageStats(context) ?: getDataStorageInfo()).apply {
            log()
        }
    }

    /**
     * 获取存储卡信息
     * @return 存储卡信息
     */
    @SuppressLint("DiscouragedPrivateApi")
    private fun invokeStorageStats(context: Context): StorageInfo? {
        return try {
            val storageStats = when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> {
                    queryBeforeM(context)
                }
                Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 -> {
                    queryAfterN1(context)
                }
                else -> {
                    null
                }
            }
            storageStats
        } catch (e: Exception) {
            Log.w(TAG, "loadStorageStats", e)
            null
        }
    }


    private fun queryBeforeM(context: Context): StorageInfo {
        Log.i(TAG, "queryBeforeM")
        val storageManager =
            StorageManagerHook(context.getSystemService(Context.STORAGE_SERVICE) as StorageManager)
        // 获取所有卷信息
        val volumeList = storageManager.getVolumeList()
        if (volumeList.isNullOrEmpty()) {
            return StorageInfo()
        }
        val storageInfo = StorageInfo()
        volumeList.filterNotNull().forEach { volume ->
            StorageVolumeHook(volume).getPathFile()?.let {
                storageInfo.addTotalBytes(it.totalSpace)
                storageInfo.addFreeBytes(it.freeSpace)
            }
        }
        return storageInfo
    }


    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun queryAfterN1(context: Context): StorageInfo {
        val storageManager =
            StorageManagerHook(context.getSystemService(Context.STORAGE_SERVICE) as StorageManager)
        val volumeList = storageManager.getVolumes()
        if (volumeList.isNullOrEmpty()) {
            return StorageInfo()
        }
        val storageInfo = StorageInfo()
        volumeList.filterNotNull().forEach { volume ->
            Log.i(TAG, "volume: $volume")
            val volumeInfoHook = VolumeInfoHook(volume)
            var totalSize = 0L
            var freeSize = 0L
            when (volumeInfoHook.getType()) {
                1 -> {
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                            val fsUuid = volumeInfoHook.getFsUuid()
                            getStorageInfo(context, fsUuid).let {
                                totalSize = it.totalBytes
                                freeSize = it.freeBytes
                            }
                        }
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 -> {
                            //5.0 6.0 7.0没有
                            totalSize = storageManager.getPrimaryStorageSize()
                        }
                        else -> {
                        }
                    }
                }
                0 -> {
                    //
                }
                else -> {
                    return@forEach
                }
            }
//            var systemSize = 0L
            val isMountedReadable = volumeInfoHook.isMountedReadable()
            if (isMountedReadable) {
                volumeInfoHook.getPath()?.let {
                    if (totalSize == 0L) {
                        totalSize = it.totalSpace
                    }
                    freeSize = it.freeSpace
//                    systemSize = totalSize - f.totalSpace
                }
            }
            storageInfo.addFreeBytes(freeSize)
            storageInfo.addTotalBytes(totalSize)
        }
        return storageInfo
    }

    // --------------------------------------------------

    /**
     * 获取内部存储信息
     * @return 内部存储信息
     */
    private fun getDataStorageInfo(): StorageInfo {
        return getStorageInfo(Environment.getDataDirectory())
    }

    /**
     *
     * 获取指定fsUuid的存储空间情况
     * API 26 android O
     * @param fsUuid fsUuid
     * @return Pair<total, free>
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getStorageInfo(context: Context, fsUuid: String?): StorageInfo {
        return try {
            val ssm = context.getSystemService(StorageStatsManager::class.java)
            val uuid = getFsUUID(fsUuid)
            return StorageInfo(ssm.getTotalBytes(uuid), ssm.getFreeBytes(uuid))
        } catch (e: Exception) {
            e.printStackTrace()
            StorageInfo()
        }
    }

    /**
     * 获取存储情况
     */
    fun getStorageInfo(file: File): StorageInfo {
        val stat = StatFs(file.path)
        val blockSize = FileUtil.getBlockSize(stat)
        ZLog.i(TAG, "getStorageInfo: ${file.path}; blockSize: $blockSize")
        val totalSpace = FileUtil.getBlockCount(stat) * blockSize
        val freeSpace = FileUtil.getAvailableBlocks(stat) * blockSize
        return StorageInfo(totalBytes = totalSpace, freeBytes = freeSpace)
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getFsUUID(fsUuid: String?): UUID {
        return when {
            fsUuid.isNullOrEmpty() -> {
                StorageManager.UUID_DEFAULT
            }
            else -> {
                UUID.fromString(fsUuid)
            }
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------

    class StorageManagerHook(private val storageManager: StorageManager) {


        @SuppressLint("DiscouragedPrivateApi")
        fun getVolumeList(): Array<*>? {
            return executeMethod(storageManager, "getVolumeList") as Array<*>?

        }

        @SuppressLint("DiscouragedPrivateApi")
        fun getVolumes(): MutableList<*>? {
            return executeMethod(storageManager, "getVolumes") as MutableList<*>?
        }

        @RequiresApi(Build.VERSION_CODES.N_MR1)
        fun getPrimaryStorageSize(): Long {
            return executeMethod(storageManager, "getPrimaryStorageSize") as Long? ?: 0L
        }

    }

    class StorageVolumeHook(private val volume: Any) {

        @SuppressLint("DiscouragedPrivateApi")
        fun getPathFile(): File? {
            return executeMethod(volume, "getPathFile") as File?
        }

    }

    class VolumeInfoHook(private val volume: Any) {

        /**
         * 0: public 外置
         * 1: private 内置
         */
        fun getType(): Int {
            return getField(volume, "type") as Int? ?: -1
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getFsUuid(): String? {
            return executeMethod(volume, "getFsUuid") as String?
        }

        fun isMountedReadable(): Boolean {
            return executeMethod(volume, "isMountedReadable") as Boolean? ?: false
        }

        fun getPath(): File? {
            return executeMethod(volume, "getPath") as File?
        }
    }

    private fun getField(self: Any, field: String): Any? {
        return try {
            return ReflectUtil.getField(self, field)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun executeMethod(self: Any, method: String): Any? {
        return try {
            return ReflectUtil.executeMethod(self, method)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

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
}