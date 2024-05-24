package com.jitendra.cryptowallet.data

import java.math.BigInteger

data class Token( val contractAddress: String, val name: String, val logoURL: String, val decimal: Int, var balance: BigInteger, var price: Double){
    val usdValue: Double
        get() = balance.toDouble() * price
}
