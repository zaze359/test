package com.zaze.dynamic.wrapper

import android.content.Context
import android.content.res.AssetManager
import java.io.IOException
import java.io.InputStream

class AssetManagerWrapper(val mBase: AssetManager) {

    companion object {
        private val assetManagerClass by lazy { AssetManager::class.java }

        // 反射调用方法addAssetPath(String path)
        private val addAssetPathMethod by lazy {
            assetManagerClass.getMethod(
                "addAssetPath",
                String::class.java
            )
        }

        /**
         * 创建指定 apk的 AssetManager
         */
        fun build(apkPath: String?): AssetManagerWrapper? {
            apkPath ?: return null
            return try {
                val assetManager = AssetManagerWrapper(AssetManager::class.java.newInstance())
                assetManager.addAssetPath(apkPath)
                assetManager
            } catch (e: Throwable) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * 将 Apk文件的添加进AssetManager中
     */
    fun addAssetPath(path: String?) {
        try {
            addAssetPathMethod.invoke(mBase, path)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun open(fileName: String, accessMode: Int = AssetManager.ACCESS_STREAMING): InputStream {
        return mBase.open(fileName, accessMode)
    }
}

fun AssetManager.wrapper() = AssetManagerWrapper(this)

val Context.assetManagerWrapper
    get() = AssetManagerWrapper(this.assets)

fun String?.toAssetManagerWrapper(): AssetManagerWrapper? {
    return AssetManagerWrapper.build(this)
}