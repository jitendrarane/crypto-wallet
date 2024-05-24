package com.jitendra.cryptowallet.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AssetTransferRequest(
    val id: Int = 1,
    val jsonrpc: String = "2.0",
    val method: String = "alchemy_getAssetTransfers",
    val params: List<AssetTransferParams>,
)

@JsonClass(generateAdapter = true)
data class AssetTransferParams(
    val fromBlock: String = "0x0",
    val toBlock: String = "latest",
    val toAddress: String = "0x0",
    val withMetadata: Boolean = true,
    val excludeZeroValue: Boolean = true,
    val maxCount: String = "0x14", // ToDo Hardcoded to 20. Make it configurable
    val contractAddresses: List<String>,
    val category: List<String> = arrayListOf("erc20"),
    val order: String = "desc",
)
