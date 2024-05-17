package com.jitendra.cryptowallet.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Prices(
    val dai: Dai,
    val pepe: Pepe,
    @Json(name = "usd-coin")
    val usdCoin: UsdCoin,
    val weth: Weth,
)

@JsonClass(generateAdapter = true)
data class Dai(
    val usd: Double,
)

@JsonClass(generateAdapter = true)
data class Pepe(
    val usd: Double,
)

@JsonClass(generateAdapter = true)
data class UsdCoin(
    val usd: Double,
)

@JsonClass(generateAdapter = true)
data class Weth(
    val usd: Double,
)

