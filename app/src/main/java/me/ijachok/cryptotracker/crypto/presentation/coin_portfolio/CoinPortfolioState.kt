package me.ijachok.cryptotracker.crypto.presentation.coin_portfolio

import androidx.compose.foundation.lazy.LazyListState
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi
import me.ijachok.cryptotracker.crypto.domain.CoinUi

data class CoinPortfolioState(
    val isLoading:Boolean = false,
    val inEditMode:Boolean = false,
    val portfolio:List<CoinPortfolioUi> = emptyList(),
    val selectedCoin: CoinUi? = null,
    val listState: LazyListState = LazyListState()
)