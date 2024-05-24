package com.jitendra.cryptowallet
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jitendra.cryptowallet.data.CryptoTransacation
import com.jitendra.cryptowallet.data.Token
import com.jitendra.cryptowallet.data.TokenType
import com.jitendra.cryptowallet.data.TokenType.TOKEN_DAI
import com.jitendra.cryptowallet.data.TokenType.TOKEN_PEPE
import com.jitendra.cryptowallet.data.TokenType.TOKEN_USD_COIN
import com.jitendra.cryptowallet.data.TokenType.TOKEN_WETH
import com.jitendra.cryptowallet.data.network.AlchemyApiService
import com.jitendra.cryptowallet.data.network.AssetTransferParams
import com.jitendra.cryptowallet.data.network.AssetTransferRequest
import com.jitendra.cryptowallet.data.network.BackendService
import com.jitendra.cryptowallet.data.network.Prices
import com.jitendra.cryptowallet.data.network.TokenBalance
import com.jitendra.cryptowallet.data.network.TokenBalancesRequest
import com.jitendra.cryptowallet.data.network.TokenMetaDataRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class WalletViewModel
    @Inject
    constructor(
        private val backendService: BackendService,
        private val alchemyApiService: AlchemyApiService,
    ) : ViewModel() {
        private val _walletState = MutableStateFlow<UIState>(UIState.Initializing)
        val walletState: StateFlow<UIState> = _walletState
        var tokens = ArrayList<Token>()
        var transactions = ArrayList<CryptoTransacation>()

        fun searchWallet(wallet: String) {
            viewModelScope.launch {
                try {
                    if (tokens.isEmpty()) {
                        updateTokenMetaData()
                        updateTokenPrices()
                    }
                    val response =
                        alchemyApiService.getTokenBalances(
                            TokenBalancesRequest(
                                params =
                                    listOf(
                                        wallet,
                                        @Suppress("ktlint:standard:max-line-length")
                                        TokenType.TokenContractAddresses.values,
                                    ),
                            ),
                        )

                    if (response.isSuccessful) {
                        // Handle the successful response here
                        val tokenBalancesResponse = response.body()
                        tokenBalancesResponse?.result?.tokenBalances?.let {
                            tokens = tokens.updateBalance(it).toCollection(ArrayList())
                        }
                        _walletState.value = UIState.Success(tokens)
                    } else {
                        // Handle the error here
                        val errorBody = response.errorBody()
                        // Use the errorBody
                        _walletState.value = UIState.Error(errorBody?.string() ?: "An error occurred")
                    }
                } catch (e: Exception) {
                    _walletState.value = UIState.Error(e.message ?: "An error occurred")
                }
            }
        }

        private suspend fun updateTokenPrices() {
            val result = backendService.prices()
            tokens = tokens.updatePrice(result).toCollection(ArrayList())
        }

        private fun List<Token>.updatePrice(result: Prices): List<Token> =
            this.map { token ->
                when (token.contractAddress) {
                    TokenType.TokenContractAddresses[TOKEN_DAI] -> token.copy(price = result.dai.usd)
                    TokenType.TokenContractAddresses[TOKEN_PEPE] -> token.copy(price = result.pepe.usd)
                    TokenType.TokenContractAddresses[TOKEN_USD_COIN] -> token.copy(price = result.usdCoin.usd)
                    TokenType.TokenContractAddresses[TOKEN_WETH] -> token.copy(price = result.weth.usd)
                    else -> token
                }
            }

        private fun List<Token>.updateBalance(tokenBalances: List<TokenBalance>): List<Token> =
            this.map { token ->
                when (token.contractAddress) {
                    TokenType.TokenContractAddresses[TOKEN_DAI] -> {
                        tokenBalances.find { it.contractAddress == TokenType.TokenContractAddresses[TOKEN_DAI] }?.let {
                            token.copy(balance = parseHexBalance(it.tokenBalance))
                        } ?: token
                    }
                    TokenType.TokenContractAddresses[TOKEN_PEPE] -> {
                        tokenBalances.find { it.contractAddress == TokenType.TokenContractAddresses[TOKEN_PEPE] }?.let {
                            token.copy(balance = parseHexBalance(it.tokenBalance))
                        } ?: token
                    }
                    TokenType.TokenContractAddresses[TOKEN_USD_COIN] -> {
                        tokenBalances.find { it.contractAddress == TokenType.TokenContractAddresses[TOKEN_USD_COIN] }?.let {
                            token.copy(balance = parseHexBalance(it.tokenBalance))
                        } ?: token
                    }
                    TokenType.TokenContractAddresses[TOKEN_WETH] -> {
                        tokenBalances.find { it.contractAddress == TokenType.TokenContractAddresses[TOKEN_WETH] }?.let {
                            token.copy(balance = parseHexBalance(it.tokenBalance))
                        } ?: token
                    }
                    else -> token
                }
            }

        private suspend fun updateTokenMetaData() {
            for (token in TokenType.TokenContractAddresses) {
                val result = alchemyApiService.getTokenMetadata(TokenMetaDataRequest(params = listOf(token.value)))
                if (result.result.name.isNotEmpty()) {
                    tokens.add(Token(token.value, result.result.name, result.result.logo, result.result.decimals, BigInteger.ZERO, 0.0))
                }
            }
        }

        private fun parseHexBalance(hexBalance: String): BigInteger {
            val cleanHex = hexBalance.removePrefix("0x")
            return BigInteger(cleanHex, 16)
        }

        fun formatNumber(number: Double): String {
            var processNumber: Double = number
            var postfix = ""
            if (number > 1_000_000_000_000) {
                processNumber /= 1_000_000_000_000
                postfix = "T"
            } else if (number > 1_000_000_000) {
                processNumber /= 1_000_000_000
                postfix = "B"
            } else if (number > 1_000_000) {
                processNumber /= 1_000_000
                postfix = "M"
            } else if (number > 1_000) {
                processNumber /= 1_000
                postfix = "K"
            }
            val decimalFormat = DecimalFormat("#,##0.00")
            return decimalFormat.format(processNumber) + postfix
        }

        fun getTransactions(wallet: String) {
            viewModelScope.launch {
                try {
                    // Refresh Token Balances
                    searchWallet(wallet)
                    // Fetch Transacations
                    val params =
                        arrayListOf(
                            AssetTransferParams(toAddress = wallet, contractAddresses = TokenType.TokenContractAddresses.values.toList()),
                        )
                    // ToDo: Implement Send transactions
                    // arrayListOf(AssetTransferParams( fromAddress = wallet, contractAddresses = TokenType.TokenContractAddresses.values.toList()))

                    val response = alchemyApiService.getAssetTransfers(AssetTransferRequest(params = params))

                    if (response.isSuccessful) {
                        // Handle the successful response here
                        val transactionResponse = response.body()
                        // Use the transactions
                        transactionResponse?.result?.transfers?.let {
                            it.forEach { assetTransferResponseTransfer ->
                                transactions.add(
                                    CryptoTransacation(
                                        "Receive", // Move out to String resource
                                        parseHexBalance(assetTransferResponseTransfer.rawContract.value),
                                        assetTransferResponseTransfer.asset,
                                        assetTransferResponseTransfer.metadata.blockTimestamp,
                                    ),
                                )
                            }
                        }
                        _walletState.value = UIState.SuccessTransactions(tokens, transactions)
                        // Use the transactions
                    } else {
                        // Handle the error here
                        val errorBody = response.errorBody()
                        // Use the errorBody
                    }
                } catch (e: Exception) {
                    // Handle the exception here
                }
            }
        }

        sealed class UIState {
            data object Initializing : UIState()

            data object Loading : UIState()

            data class Success(val tokens: List<Token>) : UIState()

            data class SuccessTransactions(val tokens: List<Token>, val transactions: List<CryptoTransacation>) : UIState()

            data class Error(val message: String) : UIState()
        }
    }
