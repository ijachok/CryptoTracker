package me.ijachok.cryptotracker.crypto.presentation.coin_list

import androidx.compose.foundation.lazy.LazyListState
import me.ijachok.cryptotracker.crypto.domain.CoinUi

data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<CoinUi> = emptyList(),
    val selectedCoin: CoinUi? = null,
    val listState: LazyListState = LazyListState()
)
