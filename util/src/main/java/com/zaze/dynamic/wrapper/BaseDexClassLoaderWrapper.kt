package com.zaze.dynamic.wrapper

import android.content.Context
import com.zaze.utils.ReflectUtil
import dalvik.system.BaseDexClassLoader

class BaseDexClassLoaderWrapper(private val mBase: BaseDexClassLoader) : ClassLoaderWrapper(mBase) {

    companion object {
        private val dexClassLoaderClass by lazy {
            try {
                Class.forName("dalvik.system.BaseDexClassLoader")
            } catch (e: ClassNotFoundException) {
                null
            }
        }

        fun hasDexClassLoader(): Boolean {
            return dexClassLoaderClass != null
        }
    }

    /**
     * pathList: DexPathList
     */
    val patchListWrapper by lazy {
        DexPathListWrapper(
            try {
                ReflectUtil.getFieldValue(mBase, "pathList")
            } catch (e: Throwable) {
                null
            }
        )
    }
}

val Context.baseDexClassLoaderWrapper
    get() = BaseDexClassLoaderWrapper(this.classLoader as BaseDexClassLoader)

fun BaseDexClassLoader.wrapper() = BaseDexClassLoaderWrapper(this)