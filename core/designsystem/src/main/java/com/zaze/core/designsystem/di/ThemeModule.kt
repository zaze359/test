package com.zaze.core.designsystem.di

import android.content.Context
import com.zaze.core.designsystem.util.ThemeStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Provides
    @Singleton
    fun providerThemeStore(@ApplicationContext context: Context): ThemeStore {
        return ThemeStore(context)
    }

}