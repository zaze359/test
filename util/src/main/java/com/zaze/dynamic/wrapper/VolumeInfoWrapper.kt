package com.zaze.dynamic.wrapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.zaze.utils.ReflectUtil
import java.io.File

class VolumeInfoWrapper(private val mBase: Any) {

    /**
     * 0: public 外置
     * 1: private 内置
     */
    fun getType(): Int {
        return ReflectUtil.getFieldValueNoExp(mBase, "type") as Int? ?: -1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFsUuid(): String? {
        return ReflectUtil.executeMethodNoExp(mBase, "getFsUuid") as String?
    }

    fun isMountedReadable(): Boolean {
        return ReflectUtil.executeMethodNoExp(mBase, "isMountedReadable") as Boolean? ?: false
    }

    fun getPath(): File? {
        return ReflectUtil.executeMethodNoExp(mBase, "getPath") as File?
    }
}