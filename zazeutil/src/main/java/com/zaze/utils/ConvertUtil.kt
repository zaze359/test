package com.zaze.utils

import java.io.*

/**
 * Description : 转换工具类

 * @author zaze
 * *
 * @version 2017/8/26 - 下午4:53 1.0
 */
object ConvertUtil {
    // --------------------------------------------------
    // --------------------------------------------------
    @JvmStatic
    fun objectToByte(obj: Serializable?): ByteArray? {
        var bytes: ByteArray? = null
        if (obj != null) {
            try {
                val bo = ByteArrayOutputStream()
                val oo = ObjectOutputStream(bo)
                oo.writeObject(obj)
                bytes = bo.toByteArray()

                bo.close()
                oo.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bytes
    }

    @JvmStatic
    fun byteToObject(bytes: ByteArray?): Any? {
        if (bytes == null) {
            return null
        }
        var obj: Any? = null
        try {
            val bi = ByteArrayInputStream(bytes)
            val oi = ObjectInputStream(bi)
            obj = oi.readObject()
            bi.close()
            oi.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj
    }
    // --------------------------------------------------
}
