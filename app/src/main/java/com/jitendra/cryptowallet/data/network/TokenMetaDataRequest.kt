package com.jitendra.cryptowallet.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenMetaDataRequest(
    val id: Int = 1,
    val jsonrpc: String = "2.0",
    val method: String = "alchemy_getTokenMetadata",
    val params: List<String>
)