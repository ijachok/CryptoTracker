package me.ijachok.cryptotracker.crypto.presentation.coin_search

import me.ijachok.cryptotracker.crypto.domain.CoinUi

sealed interface CoinSearchAction {
    data class OnCoinSearch(val query: String) : CoinSearchAction
    data class OnCoinSearchPreview(val query: String, val limit:Int) : CoinSearchAction
    data class OnQueryChange(val newQuery: String) : CoinSearchAction
    data class OnCoinClick(val coinUi: CoinUi) : CoinSearchAction

}