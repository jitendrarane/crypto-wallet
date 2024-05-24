package com.jitendra.cryptowallet

import com.jitendra.cryptowallet.data.network.AlchemyApiService
import com.jitendra.cryptowallet.data.network.BackendService
import com.jitendra.cryptowallet.data.network.TokenBalance
import com.jitendra.cryptowallet.data.network.TokenBalancesResponse
import com.jitendra.cryptowallet.data.network.TokenBalancesResponseResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import retrofit2.Response

class WalletViewModelTest {

    @Mock
    private lateinit var mockBackendService: BackendService

    @Mock
    private lateinit var mockAlchemyApiService: AlchemyApiService

    private lateinit var walletViewModel: WalletViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        walletViewModel = WalletViewModel(mockBackendService, mockAlchemyApiService)
    }

    @Test
    fun testSearchWallet() = runTest {
        // Set up your mock services to return specific data
        val expectedTokenBalancesResponse = TokenBalancesResponse(
            jsonrpc = "2.0",
            id = 1,
            result = TokenBalancesResponseResult(
                address = "0x71c7656ec7ab88b098defb751b7401b5f6d8976f",
                tokenBalances = listOf(
                    TokenBalance(
                        contractAddress = "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48",
                        tokenBalance = "0x00000000000000000000000000000000000000000000000000000006d93c3d6d"
                    ),
                    TokenBalance(
                        contractAddress = "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2",
                        tokenBalance = "0x0000000000000000000000000000000000000000000000000006edcf813beeda"
                    ),
                    TokenBalance(
                        contractAddress = "0x6982508145454ce325ddbe47a25d4ec3d2311933",
                        tokenBalance = "0x0000000000000000000000000000000000000000000000000000000000000000"
                    ),
                    TokenBalance(
                        contractAddress = "0x6b175474e89094c44da98b954eedeac495271d0f",
                        tokenBalance = "0x0000000000000000000000000000000000000000000000056bec8e9aa7bc3800"
                    )
                )
            )
        )
        whenever(mockAlchemyApiService.getTokenBalances(any())).thenReturn(Response.success(expectedTokenBalancesResponse))

        // Call the function you want to test
        walletViewModel.searchWallet("test_wallet")

        // Check the results using assertions
        val actualValue = walletViewModel.walletState.value
        assertTrue(actualValue is WalletViewModel.UIState.Success)
        assertEquals(expectedTokenBalancesResponse.result.tokenBalances, (actualValue as WalletViewModel.UIState.Success).tokens)
    }
}