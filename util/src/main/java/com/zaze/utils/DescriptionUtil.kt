package com.zaze.utils

import com.zaze.utils.ZStringUtil

/**
 * Description : 用于转换生成一些描述的工具类
 *
 * @author : ZAZE
 * @version : 2017-12-28 - 10:32
 */
object DescriptionUtil {
    /**
     * 转换成字节单位
     *
     * @param byteLength 字节长度
     * @return 格式化后输出
     */
    @JvmStatic
    fun toByteUnit(byteLength: Long, unit: Int = 1024): String {
        val kb = byteLength / unit
        val mb = byteLength / unit / unit
        val gb = byteLength / unit / unit / unit
//        val kb = byteLength shr 10
//        val mb = byteLength shr 20
//        val gb = byteLength shr 30
        return when {
            gb > 0 -> {
                ZStringUtil.format("%.2fGB", mb * 1.0f / unit)
            }
            mb > 0 -> {
                ZStringUtil.format("%.2fMB", kb * 1.0f / unit)
            }
            kb > 0 -> {
                ZStringUtil.format("%dKB", kb)
            }
            else -> ZStringUtil.format("%dB", byteLength)
        }
    }
}