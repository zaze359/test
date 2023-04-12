package com.zaze.demo.feature.storage.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


// 文件名
private const val USER_PREFERENCES_NAME = "user_preferences"
private object PreferencesKeys {
    // 表示 key 对应的数据是 string 类型
    val USERNAME = stringPreferencesKey("username")
}
// 委托方式获取dataStore
private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

@HiltViewModel
class DataStoreViewModel @Inject constructor(application: Application) : AbsAndroidViewModel(application) {
    private val dataStore = application.dataStore

    val userPreferencesFlow: Flow<String> = dataStore.data
        .catch { exception ->
            // 处理异常
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.USERNAME]?: "empty"
        }

    fun update() {
        viewModelScope.launch {
            // 插入/更新数据
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.USERNAME] = "zaze"
            }
        }
    }

    fun remove() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                // 删除指定数据
                preferences.remove(PreferencesKeys.USERNAME)
            }
        }
    }

    fun clear() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                // 清空
                preferences.clear()
            }
        }
    }
}