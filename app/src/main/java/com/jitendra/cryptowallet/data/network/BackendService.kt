package com.jitendra.cryptowallet.data.network

import com.jitendra.cryptowallet.data.network.Prices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface BackendService {
    @GET("simple/price?ids=weth,pepe,dai,usd-coin&vs_currencies=usd")
    suspend fun prices(): Prices

    companion object {
        private const val BASE_URL = "https://api.coingecko.com/api/v3/"

        fun create(): BackendService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(BackendService::class.java)
        }
    }
}