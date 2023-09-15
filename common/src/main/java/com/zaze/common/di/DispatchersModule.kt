package com.zaze.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {
    @Provides
    @Dispatcher(CustomDispatchers.Default)
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Dispatcher(CustomDispatchers.IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
//@Retention(AnnotationRetention.RUNTIME)
@Retention(AnnotationRetention.BINARY)
annotation class Dispatcher(val customDispatchers: CustomDispatchers)

enum class CustomDispatchers {
    Default,
    IO,
}

//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class DefaultDispatcher
//
//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class IODispatcher
