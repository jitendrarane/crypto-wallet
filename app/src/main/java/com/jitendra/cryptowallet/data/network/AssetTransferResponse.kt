package com.jitendra.cryptowallet.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AssetTransferResponse(
    val jsonrpc: String,
    val id: Int,
    @Json(name = "result") val result: AssetTransferResponseResult
)

@JsonClass(generateAdapter = true)
data class AssetTransferResponseResult(
    val transfers: List<AssetTransferResponseTransfer>
)

@JsonClass(generateAdapter = true)
data class AssetTransferResponseTransfer(
    val blockNum: String,
    val uniqueId: String,
    val hash: String,
    @Json(name = "from") val fromAddress: String,
    @Json(name = "to") val toAddress: String,
    val value: Double,
    val erc721TokenId: String?,
    val erc1155Metadata: String?,
    val tokenId: String?,
    val asset: String,
    val category: String,
    val rawContract: AssetTransferResponseRawContract,
    val metadata: AssetTransferResponseMetadata
)

@JsonClass(generateAdapter = true)
data class AssetTransferResponseRawContract(
    val value: String,
    val address: String,
    val decimal: String
)

@JsonClass(generateAdapter = true)
data class AssetTransferResponseMetadata(
    val blockTimestamp: String
)