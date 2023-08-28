package com.zaze.core.datastore.di

import android.content.Context
import com.zaze.core.datastore.AppPreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesAppPreferencesDataStore(
        @ApplicationContext context: Context,
    ): AppPreferencesDataStore {
        return AppPreferencesDataStore.create(context)
    }

//    @Provides
//    @Singleton
//    fun providesThemePreferencesDataStore(
//        @ApplicationContext context: Context,
//    ): ThemePreferencesDataStore {
//        return ThemePreferencesDataStore.create(context)
//    }
}
