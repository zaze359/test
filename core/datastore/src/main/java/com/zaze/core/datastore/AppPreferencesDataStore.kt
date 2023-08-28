package com.zaze.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

class AppPreferencesDataStore private constructor(preferences: DataStore<Preferences>) :
    BasePreferencesDataStore<AppPreferencesDataStore.AppPreferencesScope>(preferences) {
    companion object {
        // 委托方式获取dataStore
        private val Context.userDataStore by preferencesDataStore(
            name = "app"
        )

        fun create(context: Context): AppPreferencesDataStore {
            return AppPreferencesDataStore(context.userDataStore)
        }
    }

    internal object Key {
        // string 类型
        val username = stringPreferencesKey("username")
        val lastNavDestination = intPreferencesKey("last_nav_destination")
    }

    suspend fun setUsername(username: String) {
        edit {
            it[Key.username] = username
        }
    }

    suspend fun removeUsername() {
        edit {
            it.remove(Key.username)
        }
    }

    suspend fun setLastNavDestination(destinationId: Int) {
        edit {
            it[Key.lastNavDestination] = destinationId
        }
    }

    class AppPreferencesScope(preferences: Preferences) : Scope(preferences) {
        val username: String?
            get() = preferences[Key.username]

        val lastNavDestination: Int
            get() = preferences[Key.lastNavDestination] ?: -1
    }
}