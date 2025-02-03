package me.ijachok.cryptotracker.crypto.presentation.coin_portfolio.components

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import me.ijachok.cryptotracker.R
import me.ijachok.cryptotracker.crypto.domain.Coin
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio
import me.ijachok.cryptotracker.crypto.domain.toCoinPortfolioUI
import me.ijachok.cryptotracker.crypto.domain.toCoinUi
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.CoinListItemMini
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.CoinToBeAdded
import me.ijachok.cryptotracker.crypto.presentation.components.CryptoOutlinedButton
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme
import me.ijachok.cryptotracker.ui.theme.displayFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCoinDialog(
    modifier: Modifier = Modifier,
    coinHintList: List<Coin>,
    onConfirm: (coinPortfolio:CoinPortfolio) -> Unit,
    onCancel: () -> Unit,
    searchCoin: (query: String) -> Unit,
) {
    var coinQuery by remember { mutableStateOf("") }
    var amountQuery by remember { mutableStateOf("") }
    var showAmountError by remember { mutableStateOf(false) }
    var previewCoinPortfolio by remember {
        mutableStateOf(
            CoinPortfolio(
                id = "",
                rank = 0,
                name = "",
                symbol = "",
                amountOwned = 0.0,
                marketCapUsd = null,
                priceUsd = 0.0,
                changePercent24Hr = null
            )
        )
    }
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Surface(
            modifier,
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(Modifier.padding(16.dp)) {
                LaunchedEffect(coinQuery) {
                    if (coinQuery.isNotEmpty() && coinQuery != previewCoinPortfolio.symbol) {
                        delay(500)
                        Log.d("abba", "AddCoinDialog: query: $coinQuery")
                        searchCoin(coinQuery)
                    }else{
                        searchCoin("")
                    }
                }

                Text(
                    text = stringResource(R.string.add_coin),
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = displayFontFamily,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(12.dp))

                CoinToBeAdded(Modifier, previewCoinPortfolio.toCoinPortfolioUI())

                Spacer(Modifier.height(12.dp))

                Box {
                    var textFieldHeight by remember { mutableIntStateOf(0) }
                    OutlinedTextField(
                        modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                            textFieldHeight = layoutCoordinates.size.height
                        },
                        value = coinQuery,
                        onValueChange = { coinQuery = it },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        label = { Text(stringResource(R.string.search_coin_name)) },
                        singleLine = true,
                        maxLines = 1

                    )
                    Popup(
                        alignment = Alignment.TopStart,
                        offset = IntOffset(0, textFieldHeight)
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = coinHintList.isNotEmpty() && coinQuery.isNotEmpty(),
                            enter = fadeIn(tween(200)) + slideInVertically(),
                            exit = fadeOut(tween(200)) + slideOutVertically(),
                        ) {
                            Column(
                                Modifier
                                    .width(280.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                coinHintList.forEach { coin ->
                                    CoinListItemMini(Modifier, coin.toCoinUi()) {
                                        coinQuery = coin.symbol
                                        previewCoinPortfolio = previewCoinPortfolio.copy(
                                            id = coin.id,
                                            rank = coin.rank,
                                            name = coin.name,
                                            symbol = coin.symbol,
                                            marketCapUsd = coin.marketCapUsd,
                                            priceUsd = coin.priceUsd,
                                            changePercent24Hr = coin.changePercent24Hr
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier,
                    value = amountQuery,
                    onValueChange = {
                        amountQuery = it
                        try {
                            previewCoinPortfolio =
                                previewCoinPortfolio.copy(amountOwned = amountQuery.toDouble())
                            Log.d("abba", "AddCoinDialog: $previewCoinPortfolio")
                            showAmountError = false
                        } catch (e: NumberFormatException) {
                            showAmountError = true
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    label = { Text(stringResource(R.string.amount_owned)) },
                    supportingText = { if (showAmountError) Text(stringResource(R.string.not_a_valid_number)) },
                    isError = showAmountError,
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(Modifier.align(Alignment.End)) {
                    TextButton(
                        modifier = Modifier, onClick = onCancel
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    CryptoOutlinedButton(
                        modifier = Modifier,
                        text = stringResource(R.string.add),
                        onClick = { onConfirm(previewCoinPortfolio) },
                        enabled = !showAmountError && previewCoinPortfolio.id.isNotEmpty()
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ACDPrev() {
    CryptoTrackerTheme {
        AddCoinDialog(modifier = Modifier.fillMaxWidth(),
            coinHintList = listOf(),
            onConfirm = {},
            onCancel = {},
            searchCoin = {}
        )
    }
}