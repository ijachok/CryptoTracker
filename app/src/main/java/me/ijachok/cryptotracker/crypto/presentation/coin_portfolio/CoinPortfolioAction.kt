package me.ijachok.cryptotracker.crypto.presentation.coin_portfolio

import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi

sealed interface CoinPortfolioAction {
    data class OnCoinClick(val coinPortfolioUi: CoinPortfolioUi) : CoinPortfolioAction
    data class OnCoinEdit(val coinPortfolioUi: CoinPortfolioUi) : CoinPortfolioAction
    data class OnCoinAdd(val coinPortfolio: CoinPortfolio) : CoinPortfolioAction
    data class OnCoinDelete(val coinId:String) : CoinPortfolioAction
    data class OnCoinSearchHitList(val query: String) : CoinPortfolioAction
    data object OnPortfolioEdit : CoinPortfolioAction
    data object OnPortfolioFinishEdit : CoinPortfolioAction
    data object OnPortfolioRefresh : CoinPortfolioAction
}