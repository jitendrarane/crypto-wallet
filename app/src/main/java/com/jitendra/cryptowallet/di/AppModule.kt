package com.jitendra.cryptowallet.di

import com.jitendra.cryptowallet.data.network.AlchemyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.jitendra.cryptowallet.data.network.BackendService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBackendService(): BackendService {
        return BackendService.create()
    }

    @Singleton
    @Provides
    fun provideAlchemyService(): AlchemyApiService {
        return AlchemyApiService.create()
    }

}