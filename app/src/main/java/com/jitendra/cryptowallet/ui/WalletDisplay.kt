package com.jitendra.cryptowallet.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.jitendra.cryptowallet.WalletViewModel
import com.jitendra.cryptowallet.data.CryptoTransacation
import com.jitendra.cryptowallet.data.Token

@Suppress("ktlint:standard:function-naming")
@Composable
fun WalletDisplay(viewModel: WalletViewModel = hiltViewModel()) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val walletState = viewModel.walletState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            label = { Text(text = "Wallet Address") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions =
                KeyboardActions(onSearch = {
                    viewModel.searchWallet(searchQuery.value)
                    keyboardController?.hide()
                }),
        )
        Button(
            onClick = {
//                cryptoState.value = prices(Dai(0.0), Pepe(0.0), UsdCoin(0.0), Weth(0.0))
                viewModel.searchWallet(searchQuery.value)
                keyboardController?.hide()
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Text(text = "Get Wallet Data")
        }
        when (walletState.value) {
            is WalletViewModel.UIState.Success -> {
                val cryptoValues = (walletState.value as WalletViewModel.UIState.Success).tokens
                DisplayTokenList(cryptoValues, viewModel::formatNumber)
                DisplayTransactionButton(viewModel::getTransactions, searchQuery.value)
            }
            is WalletViewModel.UIState.Error -> {
                Text(text = (walletState.value as WalletViewModel.UIState.Error).message)
            }

            WalletViewModel.UIState.Loading -> Unit
            WalletViewModel.UIState.Initializing -> Unit
            is WalletViewModel.UIState.SuccessTransactions -> {
                val cryptoValues = (walletState.value as WalletViewModel.UIState.SuccessTransactions).tokens
                DisplayTokenList(cryptoValues, viewModel::formatNumber)

                val transactions = (walletState.value as WalletViewModel.UIState.SuccessTransactions).transactions
                DisplayTransactions(transactions, viewModel::formatNumber)

                DisplayTransactionButton(viewModel::getTransactions, searchQuery.value)
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun DisplayTokenList(
    tokens: List<Token>,
    formatNumber: (Double) -> String,
) {
    CryptoListItemHeader()
    LazyColumn {
        items(tokens.size) { token ->
            val tokenItem = tokens[token]
            if (tokenItem.balance.toDouble() > 0.0) {
                CryptoListItem(
                    tokenItem.logoURL,
                    tokenItem.name,
                    formatNumber(tokenItem.price),
                    formatNumber(tokenItem.balance.toDouble()),
                    formatNumber(tokenItem.usdValue),
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun DisplayTransactionButton(
    getTransaction: (String) -> Unit,
    wallet: String,
) {
    Button(
        onClick = {
            getTransaction(wallet)
        },
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        Text(text = "Get Transaction Data")
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun DisplayTransactions(
    transactions: List<CryptoTransacation>,
    formatNumber: (Double) -> String,
) {
    CryptoTrasactionItemHeader()
    LazyColumn {
        items(transactions.size) { transaction ->
            val transactionItem = transactions[transaction]
            CryptoTransactionItem(
                transactionItem.type,
                transactionItem.assetName,
                formatNumber(transactionItem.tokenAmount.toDouble()),
                transactionItem.Date.toString(),
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun CryptoListItemHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ItemComposable("Logo")
        ItemComposable("Name")
        ItemComposable("Price")
        ItemComposable("Balance")
        ItemComposable("BalanceUSD")
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun CryptoTrasactionItemHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ItemComposable("Type")
        ItemComposable("Name")
        ItemComposable("Balance")
        ItemComposable("Date")
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun CryptoListItem(
    logo: String,
    name: String,
    price: String,
    balance: String,
    balanceUSD: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ImageFromUrl(url = logo, modifier = Modifier.weight(1f))
        ItemComposable(name, modifier = Modifier.weight(1f))
        ItemComposable(price, modifier = Modifier.weight(1f))
        ItemComposable(balance, modifier = Modifier.weight(1f))
        ItemComposable(balanceUSD, modifier = Modifier.weight(1f))
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun CryptoTransactionItem(
    type: String,
    assetName: String,
    tokenAmount: String,
    Date: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ItemComposable(type, modifier = Modifier.weight(1f))
        ItemComposable(assetName, modifier = Modifier.weight(1f))
        ItemComposable(tokenAmount, modifier = Modifier.weight(1f))
        ItemComposable(Date, modifier = Modifier.weight(1f))
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ImageFromUrl(
    url: String,
    modifier: Modifier = Modifier,
) {
    val imageLoader = rememberImagePainter(data = url)

    Image(
        painter = imageLoader,
        contentDescription = null,
        modifier =
            modifier
                .size(20.dp),
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ItemComposable(
    itemName: String,
    modifier: Modifier = Modifier,
) {
    Text(text = itemName, modifier = modifier.padding(8.dp), textAlign = TextAlign.Start)
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
fun PreviewCryptoGrid() {
    MaterialTheme {
//        CryptoGrid(prices = Prices(Dai(1.0), pepe = Pepe(2.0), usdCoin = UsdCoin(3.0), weth = Weth(4.0)), onCryptoClicked = {})
    }
}
