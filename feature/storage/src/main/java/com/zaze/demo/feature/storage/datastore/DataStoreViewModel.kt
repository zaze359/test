package com.zaze.demo.feature.storage.datastore

import android.app.Application
import androidx.datastore.preferences.core.*
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.core.datastore.AppPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    application: Application,
    private val dataStore: AppPreferencesDataStore
) : AbsAndroidViewModel(application) {

    val userPreferencesFlow: Flow<String> = dataStore.map {
        username ?: ""
    }
//    val userPreferencesFlow: Flow<String> = dataStore.data
//        .catch { exception ->
//            // 处理异常
//            if (exception is IOException) {
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
//        }
//        .map { preferences ->
//            preferences[usernamekey]?: "empty"
//        }

    fun update() {
        viewModelScope.launch {
            dataStore.setUsername("zaze")
        }
    }

    fun remove() {
        viewModelScope.launch {
            dataStore.removeUsername()
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