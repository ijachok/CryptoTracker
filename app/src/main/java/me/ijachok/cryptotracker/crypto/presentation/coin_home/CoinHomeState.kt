package me.ijachok.cryptotracker.crypto.presentation.coin_home

import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi
import me.ijachok.cryptotracker.crypto.domain.CoinUi
import me.ijachok.cryptotracker.crypto.domain.toCoinUi
import me.ijachok.cryptotracker.crypto.presentation.coin_list.CoinListState

data class CoinHomeState(
    val isLoading: Boolean = false,
    val portfolio: List<CoinPortfolioUi> = emptyList(),
    val selectedCoin: CoinUi? = null,
) {
    val balance: Double
        get() = portfolio.sumOf { it.amount.value * it.priceUsd.value }
}

fun CoinHomeState.toListState(): CoinListState {
    return CoinListState(
        isLoading = isLoading,
        coins = portfolio.map { it.toCoinUi() },
        selectedCoin = selectedCoin,
    )
}