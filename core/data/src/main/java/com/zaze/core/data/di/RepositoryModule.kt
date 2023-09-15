package com.zaze.core.data.di

import com.zaze.core.data.service.AdService
import com.zaze.core.data.service.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-14 - 17:23
 */
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

//    @Provides
//    @Singleton
//    fun providerDeviceRepository(@ApplicationContext context: Context): DeviceRepository {
//        return DeviceRepository(context)
//    }

    @Provides
    @Singleton
    fun retrofitService(retrofit: Retrofit): RetrofitService {
        // 通过 Retrofit 创建一个 RetrofitService 的动态代理对象
        return retrofit.create(RetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun adService(retrofit: Retrofit): AdService {
        return retrofit.create(AdService::class.java)
    }

}