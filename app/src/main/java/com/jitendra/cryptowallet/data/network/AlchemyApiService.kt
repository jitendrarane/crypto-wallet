package com.jitendra.cryptowallet.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AlchemyApiService {
    //Todo Move API key in gradle.properties or other secure location
    @Headers("accept: application/json", "content-type: application/json")
    @POST("v2/5k4MV3dK58Pze4ArYJ8WoClg_iIgy1cU")
    suspend fun getTokenMetadata(@Body requestBody: TokenMetaDataRequest): TokenMetaDataResponse

    @Headers("accept: application/json", "content-type: application/json")
    @POST("v2/5k4MV3dK58Pze4ArYJ8WoClg_iIgy1cU")
    suspend fun getTokenBalances(@Body requestBody: TokenBalancesRequest): Response<TokenBalancesResponse>
    @Headers("accept: application/json", "content-type: application/json")
    @POST("v2/5k4MV3dK58Pze4ArYJ8WoClg_iIgy1cU")
    suspend fun getAssetTransfers(@Body requestBody: AssetTransferRequest): Response<AssetTransferResponse>


    companion object {
            private const val BASE_URL = "https://eth-mainnet.g.alchemy.com/"

            fun create(): AlchemyApiService {
                val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

                val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                    .create(AlchemyApiService::class.java)
            }
        }
    }
