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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.jitendra.cryptowallet.WalletViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import com.jitendra.cryptowallet.data.Token


@Composable
fun WalletDisplay(
    onCryptoClicked: (String) -> Unit,
    viewModel : WalletViewModel = hiltViewModel()
) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val walletState = viewModel.walletState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
//    val cryptoState = rememberSaveable { mutableStateOf(prices(Dai(0.0),Pepe(0.0),UsdCoin(0.0),Weth(0.0))) }

    Column {
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text(text = "Wallet Address") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.searchWallet(searchQuery.value)
                keyboardController?.hide()
            })
        )
        Button(
            onClick = {
//                cryptoState.value = prices(Dai(0.0), Pepe(0.0), UsdCoin(0.0), Weth(0.0))
                viewModel.searchWallet(searchQuery.value)
                keyboardController?.hide()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Get Wallet Data")
        }
        when (walletState.value) {
            is WalletViewModel.UIState.Success -> {
//                cryptoState.value = prices(Dai(0.0), Pepe(0.0), UsdCoin(0.0), Weth(0.0))
                val cryptoValues = (walletState.value as WalletViewModel.UIState.Success).tokens
                DisplayTokenList(cryptoValues, viewModel)
            }
            is WalletViewModel.UIState.Error -> {
                Text(text = (walletState.value as WalletViewModel.UIState.Error).message)
            }

            WalletViewModel.UIState.Loading -> Unit
            WalletViewModel.UIState.Initializing -> Unit
        }
    }
}

@Composable
fun DisplayTokenList(tokens: List<Token>, viewModel: WalletViewModel) {
    CryptoListItemHeader()
    LazyColumn {
        items(tokens.size) { token ->
            val tokenItem = tokens[token]
            if(tokenItem.balance.toDouble() > 0.0){
                CryptoListItem(tokenItem.logoURL, tokenItem.name, viewModel.formatNumber(tokenItem.price.toDouble()), viewModel.formatNumber(tokenItem.balance.toDouble()), viewModel.formatNumber(tokenItem.usdValue))
            }
        }
    }
}

//@Composable
//fun DisplayTokenTransactions(tokenTransactions: List<Transactions>) {
//    LazyColumn {
//        items(tokens.size) { token ->
//            val tokenItem = tokens[token]
//            CryptoListItem(tokenItem.logoURL, tokenItem.name, tokenItem.balance.takeLast(10), tokenItem.price.toString())
//        }
//    }
//}

@Composable
fun CryptoListItemHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ItemComposable("Logo")
        ItemComposable("Name")
        ItemComposable("Price")
        ItemComposable("Balance")
        ItemComposable("BalanceUSD")
    }
}
@Composable
fun CryptoListItem(logo: String, name: String, price:String, balance: String, balanceUSD: String,) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ImageFromUrl(url = logo)
        ItemComposable(name)
        ItemComposable(price)
        ItemComposable(balance)
        ItemComposable(balanceUSD)
    }
}

@Composable
fun ImageFromUrl(url: String) {
    val imageLoader = rememberImagePainter(data = url)

    Image(
        painter = imageLoader,
        contentDescription = null,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun ItemComposable(itemName: String) {
    Text(text = itemName, modifier = Modifier.padding(8.dp))
}

@Preview
@Composable
fun PreviewCryptoGrid() {
    MaterialTheme {
//        CryptoGrid(prices = Prices(Dai(1.0), pepe = Pepe(2.0), usdCoin = UsdCoin(3.0), weth = Weth(4.0)), onCryptoClicked = {})
    }
}

//@Composable
//fun CryptoListItem(imageUrl: String, onPhotoClicked: (String) -> Unit) {
//    val context = LocalContext.current
//    val size = with(LocalDensity.current) { 180.dp.roundToPx() }
////    val request = ImageRequest.Builder(context)
////        .data(imageUrl)
////        .size(size)
////        .build()
////    val painter = rememberAsyncImagePainter(request)
//
//    Image(
//        painter = painter,
//        contentDescription = null,
//        modifier = Modifier
//            .padding(4.dp)
//            .size(180.dp)
//            .clip(MaterialTheme.shapes.medium)
//            .clickable { onPhotoClicked(imageUrl) }
//    )
//}