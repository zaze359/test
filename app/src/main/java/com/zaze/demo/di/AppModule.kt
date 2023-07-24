package com.zaze.demo.di

import com.zaze.demo.component.okhttp.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun retrofitService(retrofit: Retrofit): RetrofitService {
        // 通过 Retrofit 创建一个 RetrofitService 的动态代理对象
        return retrofit.create(RetrofitService::class.java)
    }

}