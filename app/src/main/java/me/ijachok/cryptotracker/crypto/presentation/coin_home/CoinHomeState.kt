package me.ijachok.cryptotracker.crypto.presentation.coin_home

import me.ijachok.cryptotracker.crypto.domain.Coin
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi
import me.ijachok.cryptotracker.crypto.domain.DisplayableNumber

data class CoinHomeState(
    val isLoading: Boolean = false,
    val portfolio:List<CoinPortfolioUi> = emptyList()
){
    val balance: Double
        get() = portfolio.sumOf { it.amount.value * it.priceUsd.value }
}