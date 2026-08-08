package com.zaze.logic.core.util

/**
 * 纯 Kotlin 工具函数，无任何 android.* 依赖，可在所有 KMP target 上运行与测试。
 */
fun String?.isNotNullOrBlank(): Boolean = this != null && this.isNotBlank()

fun formatBytes(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var value = bytes.toDouble()
    var i = 0
    while (value >= 1024 && i < units.lastIndex) {
        value /= 1024
        i++
    }
    val text = if (value >= 10 || value % 1.0 == 0.0) {
        value.toLong().toString()
    } else {
        val scaled = (value * 10).toLong().toString()
        "${scaled[0]}.${scaled[1]}"
    }
    return "$text ${units[i]}"
}
