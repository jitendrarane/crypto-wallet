package com.jitendra.cryptowallet.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenBalancesResponse(
    val jsonrpc: String,
    val id: Int,
    val result: TokenBalancesResponseResult
)

@JsonClass(generateAdapter = true)
data class TokenBalancesResponseResult(
    val address: String,
    val tokenBalances: List<TokenBalance>
)

@JsonClass(generateAdapter = true)
data class TokenBalance(
    val contractAddress: String,
    val tokenBalance: String
)