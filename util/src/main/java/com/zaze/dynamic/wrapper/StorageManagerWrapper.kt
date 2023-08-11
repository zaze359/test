package com.zaze.dynamic.wrapper

import android.annotation.SuppressLint
import android.os.Build
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import com.zaze.utils.ReflectUtil
import java.lang.Exception

class StorageManagerWrapper(private val mBase: StorageManager) {
    @SuppressLint("DiscouragedPrivateApi")
    fun getVolumeList(): Array<*>? {
        return ReflectUtil.executeMethodNoExp(mBase, "getVolumeList") as Array<*>?

    }

    @SuppressLint("DiscouragedPrivateApi")
    fun getVolumes(): MutableList<*>? {
        return ReflectUtil.executeMethodNoExp(mBase, "getVolumes") as MutableList<*>?
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun getPrimaryStorageSize(): Long {
        return ReflectUtil.executeMethodNoExp(mBase, "getPrimaryStorageSize") as Long? ?: 0L
    }
}

fun StorageManager.wrapper() = StorageManagerWrapper(this)