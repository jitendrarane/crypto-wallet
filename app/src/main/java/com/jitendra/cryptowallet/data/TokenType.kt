package com.jitendra.cryptowallet.data

object TokenType {

    val TOKEN_USD_COIN = "UsdCoin"
    val TOKEN_WETH = "Weth"
    val TOKEN_PEPE = "Pepe"
    val TOKEN_DAI = "Dai"

    val TokenContractAddresses = mapOf(TOKEN_USD_COIN to "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48"
        , TOKEN_WETH  to "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2"
        , TOKEN_PEPE  to "0x6982508145454ce325ddbe47a25d4ec3d2311933"
        , TOKEN_DAI to "0x6b175474e89094c44da98b954eedeac495271d0f")
    }