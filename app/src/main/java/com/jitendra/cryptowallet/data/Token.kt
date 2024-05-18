package com.jitendra.cryptowallet.data

import java.math.BigInteger

//ToDo I am converting balance from double to string as I am unable to parse double value from hex encoded string
//sample balance value: "tokenBalance":"0x0000000000000000000000000000000000000000000000071c1e48cc274c4000
data class Token( val contractAddress: String, val name: String, val logoURL: String, val decimal: Int, var balance: BigInteger, var price: Double){
    val usdValue: Double
        get() = balance.toDouble() * price
}
