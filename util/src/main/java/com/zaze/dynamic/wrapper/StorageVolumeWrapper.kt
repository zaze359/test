package com.zaze.dynamic.wrapper

import android.annotation.SuppressLint
import android.os.storage.StorageVolume
import com.zaze.utils.ReflectUtil
import java.io.File

class StorageVolumeWrapper(private val mBase: Any) {
    @SuppressLint("DiscouragedPrivateApi")
    fun getPathFile(): File? {
        return ReflectUtil.executeMethod(mBase, "getPathFile") as File?
    }
}

fun StorageVolume.wrapper() = StorageVolumeWrapper(this)