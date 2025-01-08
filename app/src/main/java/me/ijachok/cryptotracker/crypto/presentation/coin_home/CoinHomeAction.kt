package me.ijachok.cryptotracker.crypto.presentation.coin_home

import me.ijachok.cryptotracker.crypto.domain.CoinAmount
import me.ijachok.cryptotracker.crypto.domain.CoinUi

sealed interface CoinHomeAction {
    data class OnCoinClick(val coinUI: CoinUi):CoinHomeAction
    data object OnPortfolioOpen:CoinHomeAction
    data class OnPortfolioEdit(val coinAmounts:List<CoinAmount>):CoinHomeAction
}