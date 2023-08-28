package com.zaze.common.base.ext

import android.content.Context
import android.util.Log
import com.zaze.common.base.BaseApplication
import com.zaze.utils.JsonUtil
import com.zaze.utils.log.ZTag
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 解析json列表字符串（去除了内部空对象）
 * @param json json
 * @param <T>  T
 * @return List
 **/
fun <T> parseJsonToList(json: String, clazz: Class<T>): List<T>? {
    return JsonUtil.parseJsonToList(json, object : ParameterizedType {
        override fun getActualTypeArguments(): Array<Type> {
            return arrayOf(clazz)
        }

        override fun getRawType(): Type {
            return List::class.java
        }

        override fun getOwnerType(): Type? {
            return null
        }
    })
}

fun Any?.toJsonString(): String? {
    return JsonUtil.objToJson(this)
}

/// -------------------------------

fun printInfo(message: String) = Log.i(ZTag.TAG_DEBUG, message)

fun printError(error: String) = Log.e(ZTag.TAG_DEBUG, error)