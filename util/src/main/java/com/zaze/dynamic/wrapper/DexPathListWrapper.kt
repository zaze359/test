package com.zaze.dynamic.wrapper

import com.zaze.utils.ReflectUtil
import java.lang.reflect.Array

class DexPathListWrapper(private val mBase: Any?) {
    companion object {
        private const val FIELD_DEX_ELEMENTS = "dexElements"
    }

    fun getDexElements(): Any? {
        mBase ?: return null
        return try {
            ReflectUtil.getFieldValue(mBase, FIELD_DEX_ELEMENTS)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 修改dexElements的值
     */
    fun setDexElements(dexElements: Any?) {
        mBase ?: return
        try {
            ReflectUtil.setFieldValue(mBase, FIELD_DEX_ELEMENTS, dexElements)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 将当前的DexElements 和 patchDexElements 合并
     * patchDexElements 在前。
     * @return 合并后的结果。
     */
    fun combineDexElements(patchDex: Any?): Any? {
        val baseDex = getDexElements() ?: return patchDex
        patchDex ?: return baseDex
        val componentType = patchDex.javaClass.componentType ?: return baseDex
        val fixClassLength = Array.getLength(patchDex)
        val resultLength = Array.getLength(baseDex) + fixClassLength

        val newInstance = Array.newInstance(componentType, resultLength)
        // 先插入 修复元素
        for (i in 0 until resultLength) {
            if (i < fixClassLength) {
                Array.set(newInstance, i, Array.get(patchDex, i))
            } else {
                Array.set(newInstance, i, Array.get(baseDex, i - fixClassLength))
            }
        }
        return newInstance
    }
}