package com.zaze.dynamic

import android.content.Context
import android.util.Log
import com.zaze.dynamic.wrapper.BaseDexClassLoaderWrapper
import com.zaze.dynamic.wrapper.baseDexClassLoaderWrapper
import com.zaze.dynamic.wrapper.wrapper
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.Array

object HotFix {
    private const val TAG = "HotFix"

    fun addPatch(context: Context, patchDexFile: String, patchClassName: String) {
        if (File(patchDexFile).exists()) {
            try {
                if (BaseDexClassLoaderWrapper.hasDexClassLoader()) {
                    inject(context, patchDexFile, patchClassName)
                } else {
                    Log.i(TAG, "Could not found dex class loader !!")
                }
            } catch (th: Throwable) {
                th.printStackTrace()
            }
        }
    }

    @Throws(
        ClassNotFoundException::class,
        NoSuchFieldException::class,
        IllegalAccessException::class
    )
    private fun inject(
        context: Context,
        patchDexFile: String,
        patchClassName: String
    ) {
        Log.i(TAG, "inject")
        Log.i(TAG, "patchDexFile : $patchDexFile")
        Log.i(TAG, "patchClassName : $patchClassName")
        // 获取当前应用classLoader
        val appPathClassLoader = context.baseDexClassLoaderWrapper
        val appPatchList = appPathClassLoader.patchListWrapper
        //
        // 获取补丁包 classLoader
        val patchDexClassLoader = createPatchDexClassLoader(context, patchDexFile).wrapper()
        // 合并 DexElements，patch的放在前面
        val combineDexElements =
            appPatchList.combineDexElements(patchDexClassLoader.patchListWrapper.getDexElements())
        //
        // 修改 应用的 dexElements
        appPatchList.setDexElements(combineDexElements)
        // 加载类
        appPathClassLoader.loadClass(patchClassName)
    }

    private fun createPatchDexClassLoader(context: Context, patchDexFile: String): DexClassLoader {
        return DexClassLoader(
            patchDexFile,
            context.getDir("dex", 0).absolutePath,
            patchDexFile,
            context.classLoader
        )
    }

    // ---------------------------
    private fun combineDexElements(baseDex: Any?, fixDex: Any?): Any? {
        if (baseDex == null) {
            return fixDex
        }
        if (fixDex == null) {
            return baseDex
        }
        val componentType = fixDex.javaClass.componentType
        val fixClassLength = Array.getLength(fixDex)
        val resultLength = Array.getLength(baseDex) + fixClassLength

        val newInstance = Array.newInstance(componentType, resultLength)
        // 先插入 修复元素
        for (i in 0 until resultLength) {
            if (i < fixClassLength) {
                Array.set(newInstance, i, Array.get(fixDex, i))
            } else {
                Array.set(newInstance, i, Array.get(baseDex, i - fixClassLength))
            }
        }
        return newInstance
    }
}