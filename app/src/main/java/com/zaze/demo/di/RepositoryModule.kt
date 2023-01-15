package com.zaze.demo.di

import com.zaze.demo.data.repository.DemoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-14 - 17:23
 */
//@Module
//@InstallIn(SingletonComponent::class)
//class RepositoryModule {
//
//    @Provides
//    fun providerDemoRepository(): DemoRepository {
//        return DemoRepository(Dispatchers.IO)
//    }
//}