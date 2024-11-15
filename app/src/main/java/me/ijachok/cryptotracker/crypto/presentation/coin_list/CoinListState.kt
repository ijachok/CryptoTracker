package me.ijachok.cryptotracker.crypto.presentation.coin_list

import androidx.compose.runtime.Immutable
import me.ijachok.cryptotracker.crypto.presentation.coin_list.models.CoinUi

@Immutable
data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<CoinUi> = emptyList(),
    val selectedCoin: CoinUi? = null
)
