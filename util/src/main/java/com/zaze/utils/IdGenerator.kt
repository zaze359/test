package com.zaze.utils

import java.util.concurrent.atomic.AtomicInteger

/**
 * id生成器
 */
object IdGenerator {
    private val sNextGeneratedId = AtomicInteger(1)
    fun generateId(): Int {
        while (true) {
            val result = sNextGeneratedId.get()
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result
            }
        }
    }

}