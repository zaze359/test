package com.zaze.dynamic

import android.content.Context
import com.zaze.dynamic.android.DynamicContext
import dalvik.system.DexClassLoader
import java.io.File
import java.io.IOException
import java.io.InputStream


class DynamicLoader(val context: DynamicContext, val dynamicApk: DynamicApk) {
    companion object {
        fun build(hostAppContext: Context, apkAbsPath: String): DynamicLoader? {
            val dynamicApk = DynamicApk.createOrNull(hostAppContext, apkAbsPath) ?: return null
            // 包装成 apk 对应的 Context
            val dynamicContext = DynamicContext(hostAppContext.applicationContext, 0).also { dynamicContext ->
                dynamicContext.plugin = dynamicApk
            }
            return DynamicLoader(dynamicContext, dynamicApk)
        }
    }
}