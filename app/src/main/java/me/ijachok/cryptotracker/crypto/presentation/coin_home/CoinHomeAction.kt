package me.ijachok.cryptotracker.crypto.presentation.coin_home

import me.ijachok.cryptotracker.crypto.domain.CoinAmount
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio
import me.ijachok.cryptotracker.crypto.domain.CoinUi
import me.ijachok.cryptotracker.crypto.presentation.coin_portfolio.CoinPortfolioAction

sealed interface CoinHomeAction {
    data class OnCoinClick(val coinUI: CoinUi):CoinHomeAction
    data class OnPortfolioEdit(val coinAmounts:List<CoinAmount>):CoinHomeAction
    data class OnCoinAdd(val coinPortfolio: CoinPortfolio) : CoinHomeAction
    data class OnCoinSearchHitList(val query: String) : CoinHomeAction
}