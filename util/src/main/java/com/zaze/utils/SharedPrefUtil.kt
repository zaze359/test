package com.zaze.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-08 - 16:02
 */
class SharedPrefUtil private constructor(context: Context, spName: String?) {
    val sharedPreferences: SharedPreferences = if (TextUtils.isEmpty(spName)) {
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    } else {
        context.applicationContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    /**
     * 异步
     *
     * @param key   key
     * @param value value
     * @param <T>   t
     **/
    fun <T> apply(key: String, value: T) {
        put(sharedPreferences.edit(), key, value).apply()
    }

    operator fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    /**
     * 同步
     *
     * @param key   key
     * @param value value
     * @param <T>   t
     * @return boolean
     **/
    fun <T> commit(key: String, value: T): Boolean {
        return put(sharedPreferences.edit(), key, value).commit()
    }

    fun <T> put(key: String, value: T): SharedPreferences.Editor {
        return put(sharedPreferences.edit(), key, value)
    }

    operator fun <T> get(key: String, defaultValue: T): T {
        return get(sharedPreferences, key, defaultValue)
    }

    companion object {
        @JvmOverloads
        fun newInstance(context: Context, spName: String? = null): SharedPrefUtil {
            return SharedPrefUtil(context, spName)
        }

        fun <T> put(editor: SharedPreferences.Editor, key: String, value: T): SharedPreferences.Editor {
            when (value) {
                is String -> {
                    editor.putString(key, value)
                }
                is Int -> {
                    editor.putInt(key, value)
                }
                is Boolean -> {
                    editor.putBoolean(key, value)
                }
                is Float -> {
                    editor.putFloat(key, value)
                }
                is Long -> {
                    editor.putLong(key, value)
                }
                else -> {
                    ZLog.w(ZTag.TAG_ERROR, "put failed not support type")
                }
            }
            return editor
        }

        operator fun <T> get(sharedPreferences: SharedPreferences, key: String, defaultValue: T): T {
            return when (defaultValue) {
                is String -> {
                    sharedPreferences.getString(key, defaultValue) as Any
                }
                is Int -> {
                    sharedPreferences.getInt(key, defaultValue)
                }
                is Boolean -> {
                    sharedPreferences.getBoolean(key, defaultValue)
                }
                is Float -> {
                    sharedPreferences.getFloat(key, defaultValue)
                }
                is Long -> {
                    sharedPreferences.getLong(key, defaultValue)
                }
                else -> {
                    ZLog.w(ZTag.TAG_ERROR, "get failed not support type")
                    defaultValue
                }
            } as T
        }
    }

}