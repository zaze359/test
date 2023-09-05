package com.zaze.utils.storage

import android.annotation.SuppressLint
import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.zaze.dynamic.wrapper.StorageManagerWrapper
import com.zaze.dynamic.wrapper.StorageVolumeWrapper
import com.zaze.dynamic.wrapper.VolumeInfoWrapper
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
        return queryStorageStats(context).roundStorageSize().apply {
            log()
        }
    }

//    private fun testQuery1(context: Context) {
//        val storageManager =
//            (context.getSystemService(Context.STORAGE_SERVICE)) as StorageManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val storageStatsManager =
//                context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                storageManager.recentStorageVolumes.forEach {
//                    val uuid = getFsUUID(it.uuid)
//                    val temp = StorageInfo(
//                        storageStatsManager.getTotalBytes(uuid),
//                        storageStatsManager.getFreeBytes(uuid)
//                    )
//                    temp.log("recentStorageVolumes")
//                }
//            }
//            val uuid1 = getFsUUID(storageManager.primaryStorageVolume.uuid)
//            StorageInfo(
//                storageStatsManager.getTotalBytes(uuid1),
//                storageStatsManager.getFreeBytes(uuid1)
//            ).log("primaryStorageVolume")
//            val storageInfo = StorageInfo()
//            storageManager.storageVolumes.forEach {
//                val uuid = getFsUUID(it.uuid)
//                val temp = StorageInfo(
//                    storageStatsManager.getTotalBytes(uuid),
//                    storageStatsManager.getFreeBytes(uuid)
//                )
//                storageInfo.merge(temp)
//            }
//            storageInfo.log("storageVolumes")
//        } else {
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            storageManager.storageVolumes.forEachIndexed { i, v ->
//                ZLog.i(
//                    ZTag.TAG,
//                    "query1-$i: ${v.getDescription(context)}; isPrimary:${v.isPrimary}; isRemovable:${v.isRemovable}; isEmulated:${v.isEmulated}"
//                )
//                ZLog.i(ZTag.TAG, "query1-$i: uuid:${v.uuid};")
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    ZLog.i(ZTag.TAG, "query1-$i: ${v.mediaStoreVolumeName};")
//                    ZLog.i(ZTag.TAG, "query1-$i: ${v.directory};")
//                }
//            }
//        }
//    }

    /**
     * 获取存储卡信息
     * @return 存储卡信息
     */
    @SuppressLint("DiscouragedPrivateApi")
    private fun queryStorageStats(context: Context): StorageInfo {
        return try {
            val storageStats = when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> {
                    queryBeforeM(context)
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    val storageManager =
                        (context.getSystemService(Context.STORAGE_SERVICE)) as StorageManager
                    val storageStatsManager =
                        context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
                    val uuid = getFsUUID(storageManager.primaryStorageVolume.uuid)
                    StorageInfo(
                        storageStatsManager.getTotalBytes(uuid),
                        storageStatsManager.getFreeBytes(uuid)
                    )
//                    getInnerStorageInfo()
                }

                Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 -> {
                    queryAfterNMR1(context)
                }

                else -> {
                    getInnerStorageInfo()
                }
            }
            storageStats
        } catch (e: Exception) {
            Log.w(TAG, "loadStorageStats", e)
            getInnerStorageInfo()
        }
    }

    private fun queryBeforeM(context: Context): StorageInfo {
        Log.i(TAG, "queryBeforeM")
        val storageManager =
            StorageManagerWrapper(context.getSystemService(Context.STORAGE_SERVICE) as StorageManager)
        // 获取所有卷信息
        val volumeList = storageManager.getVolumeList()
        if (volumeList.isNullOrEmpty()) {
            return StorageInfo()
        }
        val storageInfo = StorageInfo()
        volumeList.filterNotNull().forEach { volume ->
            StorageVolumeWrapper(volume).getPathFile()?.let {
                storageInfo.addTotalBytes(it.totalSpace)
                storageInfo.addFreeBytes(it.freeSpace)
            }
        }
        return storageInfo
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun queryAfterNMR1(context: Context): StorageInfo {
        val storageManager =
            StorageManagerWrapper(context.getSystemService(Context.STORAGE_SERVICE) as StorageManager)
        val volumeList = storageManager.getVolumes()
        if (volumeList.isNullOrEmpty()) {
            return StorageInfo()
        }
        val storageInfo = StorageInfo()
        volumeList.filterNotNull().forEach { volume ->
            Log.i(TAG, "volume: $volume")
            val volumeInfoHook = VolumeInfoWrapper(volume)
            var totalSize = 0L
            var freeSize = 0L
            when (volumeInfoHook.getType()) {
                1 -> {
                    totalSize = storageManager.getPrimaryStorageSize()
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
    private fun getInnerStorageInfo(): StorageInfo {
        return getStorageInfo(Environment.getRootDirectory()).merge(
            getStorageInfo(Environment.getDataDirectory())
        )
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
        val blockSize = StorageHelper.getBlockSize(stat)
        ZLog.i(TAG, "getStorageInfo: ${file.path}; blockSize: $blockSize")
        val totalSpace = StorageHelper.getBlockCount(stat) * blockSize
        val freeSpace = StorageHelper.getAvailableBlocks(stat) * blockSize
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
//
//    class StorageVolumeHook(private val volume: Any) {
//
//        @SuppressLint("DiscouragedPrivateApi")
//        fun getPathFile(): File? {
//            return executeMethod(volume, "getPathFile") as File?
//        }
//
//    }

//
//    private fun getField(self: Any, field: String): Any? {
//        return try {
//            return ReflectUtil.getFieldValue(self, field)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
//    }
//
//    private fun executeMethod(self: Any, method: String): Any? {
//        return try {
//            return ReflectUtil.executeMethod(self, method)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
}