package com.zaze.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.reflect.ParameterizedType

abstract class BasePreferencesDataStore<T : BasePreferencesDataStore.Scope> constructor(preferences: DataStore<Preferences>) :
    DataStore<Preferences> by preferences {

    fun <R> map(block: T.(Preferences) -> R): Flow<R> {
        return data
            .catch { exception ->
                // 处理异常
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                block(newScope(preferences), preferences)
            }
    }


    suspend fun getScope(): T {
        return newScope(data.first())
    }

    private fun newScope(preferences: Preferences): T {
        // 获取子类
        val superclass = this::class.java.genericSuperclass
        // 获取泛型参数类型
        val parameterized = superclass as ParameterizedType
        val type = parameterized.actualTypeArguments[0]
        return (type as Class<*>).getDeclaredConstructor(Preferences::class.java)
            .newInstance(preferences) as T
    }

    abstract class Scope(val preferences: Preferences)
}

suspend fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>): T? {
    return data.first()[key]
}