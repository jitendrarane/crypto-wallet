package com.jitendra.cryptowallet.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenMetaDataResponse(
    val jsonrpc: String,
    val id: Int,
    val result: Result
)

@JsonClass(generateAdapter = true)
data class Result(
    val decimals: Int,
    val logo: String,
    val name: String,
    val symbol: String
)