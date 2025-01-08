package me.ijachok.cryptotracker.crypto.presentation.coin_home

import me.ijachok.cryptotracker.crypto.domain.Coin
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi
import me.ijachok.cryptotracker.crypto.domain.DisplayableNumber

data class CoinHomeState(
    val isLoading: Boolean = false,
    val balance:Double = 0.0,
    val portfolio:List<CoinPortfolioUi> = emptyList()
)